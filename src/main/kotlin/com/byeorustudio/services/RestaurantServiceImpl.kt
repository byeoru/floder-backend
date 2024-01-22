package com.byeorustudio.services

import com.byeorustudio.domain.dtos.RestaurantResisterDto
import com.byeorustudio.repositories.RestaurantRepositoryImpl
import com.byeorustudio.services.interfaces.RestaurantService
import net.postgis.jdbc.geometry.Point
import org.koin.java.KoinJavaComponent.inject

class RestaurantServiceImpl: RestaurantService {
    private val restaurantRepositoryImpl : RestaurantRepositoryImpl by inject(clazz = RestaurantRepositoryImpl::class.java)

    override suspend fun register(requestDto: RestaurantResisterDto): Long {
        return restaurantRepositoryImpl.save(
            requestDto.name,
            requestDto.address,
            requestDto.availableTable,
            Point(requestDto.x, requestDto.y)
        )
    }

    override suspend fun getRestaurants(
        pageSize: Int,
        pageNumber: Int,
        customerLocation: Point,
        maxDistanceMeter: Long
    ): List<Map<String, Any>> {
        return restaurantRepositoryImpl.findListByDistance(
            pageSize,
            pageNumber,
            customerLocation,
            maxDistanceMeter
        )
    }
}