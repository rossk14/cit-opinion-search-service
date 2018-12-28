package com.rosskerr.citapp.controllers

import com.rosskerr.citapp.OpinionQueryService
import com.rosskerr.citapp.models.MarkedUpOpinion
import com.rosskerr.citapp.models.Opinion
import com.rosskerr.citapp.models.ScoredOpinion
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.server.types.files.StreamedFile
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal

@Controller("/opinionQuery")
open class OpinionQueryController (open val queryService: OpinionQueryService) {

    @Get("/allOpinions")
    fun index(): List<Opinion> {
        return queryService.getAllOpinions()
    }

    @Get("/opinionPdf")
    @Produces("application/pdf")
    fun pdf(@QueryValue opinionId: BigDecimal?) : ByteArray? {
        var result: ByteArray? = queryService.getOpinionPdf(opinionId) ?: ByteArray(0)
        return result
    }

    @Get("/simpleSearch")
    fun simpleSearch(@QueryValue searchCriteria: String) : List<ScoredOpinion> {
        var result: List<ScoredOpinion> = queryService.simpleSearch(searchCriteria)
        return result
    }

    @Get("/getMarkup")
    fun getMarkedUpOpinion(@QueryValue opinionId: BigDecimal?, @QueryValue criteriaForMarkup: String)
            : String?
    {
        return queryService.getMarkedUpOpinion(opinionId, criteriaForMarkup)
    }
}