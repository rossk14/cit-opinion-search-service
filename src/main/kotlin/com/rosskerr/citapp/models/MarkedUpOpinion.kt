package com.rosskerr.citapp.models

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import javax.persistence.*
import java.sql.Clob

@NamedStoredProcedureQuery(
        name="getMarkedUpOpinion",
        procedureName="OPINIONS.OPINION_QUERY_PACKAGE.GET_OPINION_HTML",
        resultClasses = [ MarkedUpOpinion::class ],
        parameters=[
            StoredProcedureParameter(name="opinion_id", type=BigDecimal::class, mode=ParameterMode.IN),
            StoredProcedureParameter(name="query", type=String::class, mode=ParameterMode.IN),
            StoredProcedureParameter(name="outcursor", type=Unit::class, mode=ParameterMode.REF_CURSOR)
        ])

@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity
@Table(name = "MARKEDUP_OPINIONS")

data class MarkedUpOpinion (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="opinion_sequence")
    @SequenceGenerator(
            name="opinion_sequence",
            sequenceName="opinionId_seq",
            allocationSize=20
    )
    val id: BigDecimal? = null,

    @Column(name = "html")
    val html: Clob? = null)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MarkedUpOpinion

        if (id != other.id) return false
        if (html != null) {
            if (other.html == null) return false
            if (!html.toString().contentEquals(other.html.toString())) return false
        } else if (other.html != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (html?.toString()?.toCharArray()?.contentHashCode() ?: 0)
        return result
    }
}