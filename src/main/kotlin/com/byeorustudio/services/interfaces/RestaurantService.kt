package com.byeorustudio.services.interfaces

import com.byeorustudio.domain.dtos.FoodResisterDto
import com.byeorustudio.domain.dtos.RestaurantResisterDto
import net.postgis.jdbc.geometry.Point

interface RestaurantService {
    suspend fun register(requestDto: RestaurantResisterDto): Long
    suspend fun getRestaurants(pageSize: Int, pageNumber: Int, customerLocation: Point, maxDistanceMeter: Long): List<Map<String, Any>>
    suspend fun addFood(requestDto: FoodResisterDto, restaurantPk: Long): Long
}