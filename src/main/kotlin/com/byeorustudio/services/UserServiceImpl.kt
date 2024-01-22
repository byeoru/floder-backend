package com.byeorustudio.services

import com.byeorustudio.domain.dtos.UserLoginDto
import com.byeorustudio.domain.dtos.UserSignUpDto
import com.byeorustudio.domain.tables.User
import com.byeorustudio.repositories.UserRepositoryImpl
import com.byeorustudio.services.interfaces.UserService
import org.koin.java.KoinJavaComponent.inject

class UserServiceImpl: UserService {
    private val userRepositoryImpl: UserRepositoryImpl by inject(clazz = UserRepositoryImpl::class.java)

    override suspend fun signUp(requestDto: UserSignUpDto): Long {
        return userRepositoryImpl.save(requestDto.username, requestDto.email, requestDto.password, requestDto.userType)
    }

    override suspend fun login(requestDto: UserLoginDto): User {
        return userRepositoryImpl.findByEmailPassword(requestDto.email, requestDto.password)
    }

    override suspend fun findUser(pk: Long): User {
        return userRepositoryImpl.findByPk(pk)
    }
}