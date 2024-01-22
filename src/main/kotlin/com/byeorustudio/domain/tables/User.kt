package com.byeorustudio.domain.tables

import com.byeorustudio.domain.tables.common.*
import org.jetbrains.exposed.dao.id.EntityID

enum class EUserType {
    OWNER, CUSTOMER
}

object Users : BaseLongIdTable("users", "user_pk") {
    val username = varchar("username", 20)
    val email = text("email")
    val password = varchar("password", 20)
    val userType = enumerationByName<EUserType>("user_type", 10)
}

class User(id: EntityID<Long>) : BaseEntity(id, Users) {
    companion object : BaseEntityClass<User>(Users)

    var username by Users.username
    var email by Users.email
    var password by Users.password
    var userType by Users.userType
}