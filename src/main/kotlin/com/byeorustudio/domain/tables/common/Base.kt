package com.byeorustudio.domain.tables.common

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.time.ZoneId

abstract class BaseLongIdTable(name: String, idName: String = "pk") : LongIdTable(name, idName) {
    private val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    val createdAt = datetime("created_at").clientDefault { now }
    val updatedAt = datetime("updated_at").clientDefault { now }
}

abstract class BaseEntity(id: EntityID<Long>, table: BaseLongIdTable) : LongEntity(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}

abstract class BaseEntityClass<E : BaseEntity>(table: BaseLongIdTable) : LongEntityClass<E>(table) {
    private val log = exposedLogger
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                try {
                    action.toEntity(this)?.updatedAt = LocalDateTime.now()
                } catch (e: Exception) {
                    log.warn("Failed to update entity $this updatedAt ${e.message}")
                }
            }
        }
    }
}