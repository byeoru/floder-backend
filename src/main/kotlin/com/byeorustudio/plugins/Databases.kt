package com.byeorustudio.plugins

import com.byeorustudio.domain.tables.Foods
import com.byeorustudio.domain.tables.Restaurants
import com.byeorustudio.domain.tables.Users
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val url = environment.config.property("db.url").getString()
    val driver = environment.config.property("db.driver").getString()
    val user = environment.config.property("db.user").getString()
    val pw = environment.config.property("db.password").getString()
    Database.connect(
        url,
        driver = driver,
        user = user,
        password = pw,
    )

    transaction {
//        exec("DROP TABLE IF EXISTS users CASCADE;")
        // Table 제거
        SchemaUtils.drop()

        // Table 생성
        SchemaUtils.create(
            Users, Restaurants, Foods
        )
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
