package com.byeorustudio.repositories.interfaces

import com.byeorustudio.domain.tables.Restaurant
import net.postgis.jdbc.geometry.Point

interface RestaurantRepository {
    suspend fun save(name: String, address: String, availableTables: Int, coords: Point): Long
    suspend fun findByPk(pk: Long): Restaurant
    suspend fun findListByDistance(pageSize: Int, pageNumber: Int, customerLocation: Point, maxDistanceMeter: Long): List<Map<String, Any>>
    suspend fun saveFood(name: String, description: String, normalPrice: Int, restaurant: Restaurant): Long
}