package com.rosskerr.citapp.controllers

import com.rosskerr.citapp.IManageOpinions
import com.rosskerr.citapp.OpinionManagementService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus


@Controller("/opinionManagement")
open class OpinionManagementController (managementService: IManageOpinions){

    open val managementService: IManageOpinions = managementService

    @Get("/")
    fun index(): HttpStatus {
        return HttpStatus.OK
    }
}