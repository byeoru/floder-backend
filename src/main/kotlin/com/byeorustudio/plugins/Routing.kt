package com.byeorustudio.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import jdk.jshell.spi.ExecutionControl.InternalException

fun Application.configureRouting() {
    install(StatusPages) {
        exception<InternalException> { call, internalException ->
            call.respondText(text = "500: $internalException" , status = HttpStatusCode.InternalServerError)
        }
        exception<NotFoundException> { call, notFoundException ->
            call.respondText(text = "${HttpStatusCode.NotFound}: $notFoundException", status = HttpStatusCode.NotFound)
        }
        exception<BadRequestException> { call, badRequestException ->
            call.respondText(text = "${HttpStatusCode.BadRequest}: $badRequestException", status = HttpStatusCode.BadRequest)
        }
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respondText(text = "$status: Token is not valid or has expired", status = HttpStatusCode.Unauthorized)
        }
    }
}
