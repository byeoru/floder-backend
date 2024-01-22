package com.byeorustudio.domain.dtos

import com.byeorustudio.domain.tables.EUserType
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpDto(
    val username: String,
    val email: String,
    val password: String,
    val userType: EUserType,
)

@Serializable
data class UserLoginDto(
    val email: String,
    val password: String
)