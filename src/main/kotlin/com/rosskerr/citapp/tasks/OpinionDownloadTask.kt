package com.rosskerr.citapp.tasks

import com.rosskerr.citapp.OpinionManagementService
import com.rosskerr.citapp.models.Opinion
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.scheduling.annotation.Scheduled
import io.reactivex.Flowable
import org.jsoup.Jsoup
import java.sql.Timestamp
import java.time.Instant
import javax.inject.Singleton

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

@Singleton
class OpinionDownloadTask (val managementService: OpinionManagementService,
                           @Client("citopinions") val citClient: RxHttpClient,
                           @Value("https://www.cit.uscourts.gov") val basePdfUrl: String)
{

    val LOG: Logger = LoggerFactory.getLogger(OpinionDownloadTask::class.java)

    @Scheduled(fixedRate = "1440m", initialDelay = "5s") // 60 x 24 x 1 days = 1440
    fun updateOpinionLibrary() {
        LOG.info("Preparing to download opinions...")

        val opinions = downloadNewOpinions()
        LOG.info("Preparing to save ${opinions.size} opinions")
        LOG.info("Updating indexes...")
        managementService.updateIndexes()
        LOG.info("Finished updating indexes.")
    }

    fun downloadNewOpinions(): List<Opinion> {
        // set up context
        val context = managementService.getImportContext()

        val maxRows = 6000
        val currentYearOpinionPage = "https://www.cit.uscourts.gov/SlipOpinions/index.html"
        val opinionList = Jsoup.connect(currentYearOpinionPage).get()

        // get all our opinion rows
        // we can do this because there's only one table on the page
        val documentRows = opinionList.select("tr")

        // remove header row (does this even work?)
        documentRows[0].remove()

        LOG.info("Preparing to process ${documentRows.size} opinion rows...")

        var results = mutableListOf<Opinion>()

        // could have just mapped result, but want to serial retrieve pdfs to not run afoul of any DDoS filter
        documentRows.take(maxRows).forEach {
            var tds = it.select("td")
            if (tds.size > 0) {
                var title = tds[1].text()
                var number = if (tds[0].selectFirst("a")?.text() == null) tds[0].text() else tds[0].selectFirst("a").text()
                var url: String? = tds[0]?.selectFirst("a")?.attr("href")

                // try to add opinion to our context, but only if PDF exists
                if (url != null) {
                    context.addOpinionIfNotExists(Opinion(id = null,
                            title = title,
                            loaded = Timestamp.from(Instant.now()),
                            opinionDate = null,
                            opinionNumber = number,
                            pdfUrl = url,
                            pdf = null))

                }
            }
        }

        // set the pdf files for the new opinions in our context
        context.opinionsToImport.forEach {
            // save opinion if the PDF is available
            LOG.info("Trying to get PDF from ${basePdfUrl + it.pdfUrl}")
            val call: Flowable<HttpResponse<ByteArray>> = citClient.exchange(HttpRequest.GET<ByteArray>(URI(it.pdfUrl)), ByteArray::class.java)

            val response = call.blockingFirst()
            LOG.info("Response ${response.status.code} ${response.status.reason}")

            if (response.status.code == 200 && response.body.isPresent) {
                val pdfData = response.body.get()
                // the following keeps our Opinion immutable, can simplify by making pdfData a var
                val opinion = Opinion(
                        id = it.id,
                        opinionDate = it.opinionDate,
                        opinionNumber = it.opinionNumber,
                        majorityBy = it.majorityBy,
                        title = it.title,
                        pdfUrl = it.pdfUrl,
                        loaded = it.loaded,
                        pdf = pdfData
                )
                LOG.info("Adding ${it.opinionNumber} to opinion list")
                context.opinionsToImport[context.opinionsToImport.indexOf(it)] = opinion

            } else {
                LOG.warn("PDFData for ${it.opinionNumber} is not available, not saving...")
            }
        }

        // save the new opinions to the db through our context
        context.persist()

        return context.opinionsToImport
    }

}