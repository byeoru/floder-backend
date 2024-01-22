package com.byeorustudio.modules

import com.byeorustudio.repositories.RestaurantRepositoryImpl
import com.byeorustudio.services.RestaurantServiceImpl
import org.koin.dsl.module

val restaurantModule = module {
    single<RestaurantServiceImpl> { RestaurantServiceImpl() }
    single<RestaurantRepositoryImpl> { RestaurantRepositoryImpl() }
}