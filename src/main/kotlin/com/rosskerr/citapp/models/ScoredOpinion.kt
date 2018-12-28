package com.rosskerr.citapp.models

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*

@NamedStoredProcedureQuery(
        name="getOpinionsBySimpleSearch",
        procedureName="OPINIONS.OPINION_QUERY_PACKAGE.GET_OPINIONS_BY_SIMPLE_SEARCH",
        resultClasses = [ ScoredOpinion::class ],
        parameters=[
            StoredProcedureParameter(name="criteria", type=String::class, mode=ParameterMode.IN),
            StoredProcedureParameter(name="quality", type=BigDecimal::class, mode=ParameterMode.IN),
            StoredProcedureParameter(name="outcursor", type=Unit::class, mode=ParameterMode.REF_CURSOR)
        ])

@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity
@Table(name = "SCORED_OPINION_LIST")
data class ScoredOpinion (
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="opinion_sequence")
        @SequenceGenerator(
                name="opinion_sequence",
                sequenceName="opinionId_seq",
                allocationSize=20
        )
        val id: BigDecimal? = null,

        @Column(name = "title")
        val title: String? = null,

        @Column(name = "date_loaded")
        val loaded: Timestamp? = null,

        @Column(name = "majority_by")
        val majorityBy: String? = null,

        @Column(name = "date_opinion")
        val opinionDate: Timestamp? = null,

        @Column(name = "opinion_number")
        val opinionNumber: String? = null,

        @Column(name = "original_pdf_url")
        val pdfUrl: String? = null,

        @Column(name = "score")
        val score: BigDecimal? = null)
