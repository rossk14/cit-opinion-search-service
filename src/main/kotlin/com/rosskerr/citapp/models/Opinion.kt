package com.rosskerr.citapp.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*

@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity
@Table(name = "OPINION")
data class Opinion (

        @Column(name = "id")
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

        @Id
        @Column(name = "opinion_number")
        val opinionNumber: String? = null,

        @Column(name = "original_pdf_url")
        val pdfUrl: String? = null,

        @Lob
        @JsonIgnore
        @Column(name = "pdf", columnDefinition = "BLOB")
        val pdf: ByteArray? = null)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Opinion

        if (opinionNumber != other.opinionNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opinionNumber?.hashCode() ?: 0
        return result
    }

}
