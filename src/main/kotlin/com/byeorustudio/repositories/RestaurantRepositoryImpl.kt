package com.byeorustudio.repositories

import com.byeorustudio.domain.tables.Restaurant
import com.byeorustudio.execAndMap
import com.byeorustudio.modules.loginUser
import com.byeorustudio.plugins.dbQuery
import com.byeorustudio.repositories.interfaces.RestaurantRepository
import io.ktor.server.plugins.*
import net.postgis.jdbc.geometry.Point

class RestaurantRepositoryImpl : RestaurantRepository {
    override suspend fun save(name: String, address: String, availableTables: Int, coords: Point): Long = dbQuery {
        Restaurant.new {
            this.name = name
            this.address = address
            this.availableTables = availableTables
            this.coords = coords
            this.owner = loginUser
        }.id.value
    }

    override suspend fun findByPk(pk: Long): Restaurant = dbQuery {
        Restaurant.findById(pk)
    } ?: throw NotFoundException("Restaurant not found")

    override suspend fun findListByDistance(pageSize: Int, pageNumber: Int, customerLocation: Point, maxDistanceMeter: Long): List<Map<String, Any>> = dbQuery {
        val query = """
            select * from Restaurants r 
            where ST_Dwithin(r.coords, 'POINT(${customerLocation.x} ${customerLocation.y})', $maxDistanceMeter, false) = true;
        """.trimIndent()
        val results = query.execAndMap { rs -> mapOf(
            "restaurant_pk" to rs.getLong("restaurant_pk"),
            "name" to rs.getString("name")
        ) }
        return@dbQuery results
    }
}