package com.rosskerr.citapp

import com.rosskerr.citapp.models.MarkedUpOpinion
import com.rosskerr.citapp.models.Opinion
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional
import com.rosskerr.citapp.models.ScoredOpinion
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.sql.Clob
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.StoredProcedureQuery

@EnableTransactionManagement
open class OpinionQueryService (@CurrentSession @PersistenceContext open val entityManager: EntityManager)
    : IQueryOpinions {

    @Transactional(readOnly = true)
    override fun simpleSearch(criteria: String): List<ScoredOpinion> {
        var query: StoredProcedureQuery = entityManager
                .createNamedStoredProcedureQuery("getOpinionsBySimpleSearch")
                .setParameter("criteria", criteria)
                .setParameter("quality", BigDecimal(0))

        return query.resultList as List<ScoredOpinion>
    }

    @Transactional(readOnly = true)
    open override fun getAllOpinions(): List<Opinion> {
        return entityManager
                .createQuery("select op from Opinion op", Opinion::class.java)
                .resultList
    }

    @Transactional(readOnly = true)
    open override fun getOpinionPdf(opinionId: BigDecimal?): ByteArray? {
        return entityManager
                .createQuery("select op.pdf from Opinion op where id = ?1", ByteArray::class.java)
                .setParameter(1, opinionId)
                .resultList?.get(0)
    }

    @Transactional(readOnly = true)
    open override fun getMarkedUpOpinion(opinionId: BigDecimal?, criteriaForMarkup: String): String? {
        var query: StoredProcedureQuery = entityManager
                .createNamedStoredProcedureQuery("getMarkedUpOpinion")
                .setParameter("query", criteriaForMarkup)
                .setParameter("opinion_id", opinionId)
        var result: Clob? = (query.resultList?.get(0) as MarkedUpOpinion?)?.html
        return result?.getSubString(1, result.length().toInt())
    }
}