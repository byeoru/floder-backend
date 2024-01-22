package com.byeorustudio

import com.byeorustudio.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) { }
    configureSerialization()
    configureDatabases()
    configureHTTP()
//    configureSecurity()
    configureRouting()
}
