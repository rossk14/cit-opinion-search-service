package com.rosskerr.citapp

import com.rosskerr.citapp.models.MarkedUpOpinion
import com.rosskerr.citapp.models.Opinion
import com.rosskerr.citapp.models.ScoredOpinion
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal

interface IQueryOpinions {
    fun getAllOpinions(): List<Opinion>
    fun getOpinionPdf(opinionId: BigDecimal?): ByteArray?
    fun simpleSearch(criteria: String): List<ScoredOpinion>
    fun getMarkedUpOpinion(opinionId: BigDecimal?, criteriaForMarkup: String): String?
}