package com.rosskerr.citapp

import com.rosskerr.citapp.models.Opinion

interface IManageOpinions {
    fun save(op: Opinion): Boolean
    fun updateIndexes(): Boolean
    fun opinionExists(op: Opinion): Boolean
    fun getImportContext() : OpinionImportContext
}