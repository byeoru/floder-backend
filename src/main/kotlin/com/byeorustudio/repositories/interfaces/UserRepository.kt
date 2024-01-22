package com.byeorustudio.repositories.interfaces

import com.byeorustudio.domain.tables.EUserType
import com.byeorustudio.domain.tables.User

interface UserRepository {
    suspend fun save(username: String, email: String, password: String, userType: EUserType) : Long
    suspend fun findByPk(pk: Long) : User
    suspend fun findByEmailPassword(email: String, password: String) : User
}