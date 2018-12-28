package com.rosskerr

import io.micronaut.runtime.Micronaut

object TaskApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        val props = mutableMapOf<String, Any>()
        props["micronaut.server.port"] = 8086
        Micronaut.build()
                .packages("com.rosskerr.tasks")
                .mainClass(TaskApplication.javaClass)
                .properties(props)
                .start()
    }
}