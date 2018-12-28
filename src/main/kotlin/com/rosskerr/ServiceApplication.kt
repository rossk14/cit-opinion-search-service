package com.rosskerr

import io.micronaut.runtime.Micronaut

object ServiceApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.rosskerr.controllers")
                .mainClass(ServiceApplication.javaClass)
                .start()
    }
}