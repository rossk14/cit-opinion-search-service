package com.rosskerr.citapp

import com.rosskerr.citapp.models.Opinion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Context object to provide state to our management class for importing.
 * Originally importing would check each opinion with the DB, this context
 * object minimizes the DB calls during import
 */
class OpinionImportContext (val managementService: IManageOpinions) {

    val LOG: Logger = LoggerFactory.getLogger(OpinionImportContext::class.java)
    val existingOpinions = mutableListOf<Opinion>()
    val opinionsToImport = mutableListOf<Opinion>()
    
    fun persist() {
        opinionsToImport.forEach {
            LOG.info("Saving opinion ${it.opinionNumber} \t Title: ${it.title} \t Url: ${it.pdfUrl}")
            managementService.save(it)
        }
    }

    fun withExistingOpinions (opinions: MutableList<Opinion>) : OpinionImportContext {
        existingOpinions.addAll(opinions)
        return this
    }

    fun addOpinionIfNotExists(opinion: Opinion) : Boolean {
        return if (!existingOpinions.contains(opinion)) {
            opinionsToImport.add(opinion)
            true
        } else {
            false
        }
    }
}