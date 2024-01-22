package com.byeorustudio

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.byeorustudio.JwtConfig.readProperties
import com.byeorustudio.modules.loginUser
import com.byeorustudio.services.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import org.koin.ktor.ext.inject
import java.util.*

object JwtConfig {
    private const val VALIDITY_IN_MS = 36_000_00 * 24 * 7 // 7 day
     lateinit var issuer: String
     lateinit var secret: String
     lateinit var algorithm: Algorithm

    fun Application.readProperties() {
        issuer = environment.config.property("jwt.issuer").getString()
        secret = environment.config.property("jwt.secret").getString()
        algorithm = Algorithm.HMAC512(secret)
    }

    /**
     * Produce a token for this combination of name and password
     */
    fun generateToken(userPk: Long): String {

        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("user_pk", userPk)
            .withExpiresAt(getExpiration())  // optional
            .sign(algorithm)
    }
    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)
}



fun Application.jwtInit() {
    val userServiceImpl : UserServiceImpl by inject<UserServiceImpl>()
    readProperties()
    val verifier: JWTVerifier = JWT
        .require(JwtConfig.algorithm)
        .withIssuer(JwtConfig.issuer)
        .build()
    install(Authentication) {

        jwt {
            verifier(verifier)
            validate { credential ->
                val userId = credential.payload.getClaim("user_pk").asLong()
                if (userId != null) {
                    val user = userServiceImpl.findUser(userId)
                    loginUser = user
                    JWTPrincipal(credential.payload)
                } else {
                    throw BadRequestException("Wrong payload in token")
                }
            }
        }
    }
}