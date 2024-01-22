package com.byeorustudio.services.interfaces

import com.byeorustudio.domain.dtos.UserLoginDto
import com.byeorustudio.domain.dtos.UserSignUpDto
import com.byeorustudio.domain.tables.User

interface UserService {
    suspend fun signUp(requestDto: UserSignUpDto): Long
    suspend fun login(requestDto: UserLoginDto): User
    suspend fun findUser(pk: Long): User
}