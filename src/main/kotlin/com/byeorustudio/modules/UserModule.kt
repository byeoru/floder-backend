package com.byeorustudio.modules

import com.byeorustudio.domain.tables.User
import com.byeorustudio.repositories.UserRepositoryImpl
import com.byeorustudio.services.UserServiceImpl
import org.koin.dsl.module

lateinit var loginUser: User

val userModule = module {
    single<UserServiceImpl> { UserServiceImpl() }
    single<UserRepositoryImpl> { UserRepositoryImpl() }
}