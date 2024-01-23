package com.byeorustudio.repositories

import com.byeorustudio.domain.tables.EUserType
import com.byeorustudio.domain.tables.User
import com.byeorustudio.domain.tables.Users
import com.byeorustudio.plugins.dbQuery
import com.byeorustudio.repositories.interfaces.UserRepository
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.and

class UserRepositoryImpl: UserRepository {
        override suspend fun save(username: String, email: String, password: String, userType: EUserType): Long = dbQuery {
        User.new {
            this.username = username
            this.email = email
            this.password = password
            this.userType = userType
        }.id.value
    }

    override suspend fun findByPk(pk: Long): User = dbQuery {

        User.findById(pk)
    } ?: throw NotFoundException("User not found")

    override suspend fun findByEmailPassword(email: String, password: String): User = dbQuery {
        User.find { (Users.email eq email) and (Users.password eq password) }.singleOrNull()
    } ?: throw NotFoundException("User not found")
}