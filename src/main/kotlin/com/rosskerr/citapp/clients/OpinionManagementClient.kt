package com.rosskerr.citapp.clients

import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus

@Client("opinionManagement")
interface OpinionManagementClient {

    @Get("/")
    fun index(): HttpStatus
}