package com.rosskerr.citapp

import com.rosskerr.citapp.models.Opinion
import com.rosskerr.citapp.models.ScoredOpinion
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional
import java.math.BigDecimal
import javax.inject.Singleton
import javax.persistence.*

@NamedStoredProcedureQuery(
        name="syncTextIndex",
        procedureName="OPINIONS.OPINION_MANAGEMENT_PACKAGE.SYNC_TEXT_INDEX"
        )

@Singleton
open class OpinionManagementService (@CurrentSession @PersistenceContext open val entityManager: EntityManager)
    : IManageOpinions {

    @Transactional(readOnly = true)
    override fun getImportContext(): OpinionImportContext {
        return OpinionImportContext(this)
                .withExistingOpinions(entityManager
                    .createQuery("select op from Opinion op", Opinion::class.java)
                    .resultList)
    }

    @Transactional
    override fun updateIndexes(): Boolean {
        entityManager.createStoredProcedureQuery("syncTextIndex")
        return true
    }

    @Transactional
    override fun save(op: Opinion): Boolean {
        val existingOpinion = entityManager.find(Opinion::class.java, op.opinionNumber)
        if (existingOpinion != null) {
            return false
        }

        entityManager.persist(op)
        return true
    }

    @Transactional
    override fun opinionExists(op: Opinion): Boolean {
        val existingOpinion = entityManager.find(Opinion::class.java, op.opinionNumber)
        return existingOpinion != null
    }

}