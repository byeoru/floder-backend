package com.byeorustudio.routers

import com.byeorustudio.JwtConfig
import com.byeorustudio.domain.dtos.UserLoginDto
import com.byeorustudio.domain.dtos.UserSignUpDto
import com.byeorustudio.services.UserServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.userRouter() {
    val userServiceImpl: UserServiceImpl by inject()

    route("/users") {
        post {
            val requestDto = call.receive<UserSignUpDto>()
            val pk = userServiceImpl.signUp(requestDto)
            call.respond(message = mapOf("pk" to pk), status = HttpStatusCode.Created)
        }
        post("/login") {
            val requestDto = call.receive<UserLoginDto>()
            userServiceImpl.login(requestDto).let {
                val token = JwtConfig.generateToken(it.id.value)
                call.respond(message = mapOf("token" to token), status = HttpStatusCode.OK)
            }
        }
    }
}