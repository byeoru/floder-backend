package com.byeorustudio.routers

import com.byeorustudio.domain.dtos.FoodResisterDto
import com.byeorustudio.domain.dtos.RestaurantResisterDto
import com.byeorustudio.domain.dtos.RestaurantSimpleDto
import com.byeorustudio.services.RestaurantServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import net.postgis.jdbc.geometry.Point
import org.koin.ktor.ext.inject

fun Routing.restaurantRouter() {
    val restaurantServiceImpl: RestaurantServiceImpl by inject()

    route("/restaurants") {
        // 매장 리스트 불러오기
        get {
            val queryParams = call.request.queryParameters
            val pageSize = queryParams["page-size"]?.toIntOrNull() ?: 20
            val pageNumber = queryParams["page-number"]?.toIntOrNull() ?: return@get call.respondText(
                text = "${HttpStatusCode.BadRequest}: page-number not found or not Int", status = HttpStatusCode.BadRequest
            )
            val x = queryParams["x"]?.toDoubleOrNull() ?: return@get call.respondText(
                text = "${HttpStatusCode.BadRequest}: x not found or not Double", status = HttpStatusCode.BadRequest
            )
            val y = queryParams["y"]?.toDoubleOrNull() ?: return@get call.respondText(
                text = "${HttpStatusCode.BadRequest}: x not found or not Double", status = HttpStatusCode.BadRequest
            )
            val maxDistanceMeter = queryParams["max-distance"]?.toLongOrNull() ?: return@get call.respondText(
                text = "${HttpStatusCode.BadRequest}: max-distance not found or not Long", status = HttpStatusCode.BadRequest
            )

            restaurantServiceImpl.getRestaurants(pageSize, pageNumber, Point(x, y), maxDistanceMeter)
                .let { mapList ->
                    val result = mapList.map { RestaurantSimpleDto.fromMap(it) }
                    call.respond(message = result, status = HttpStatusCode.OK)
                }
        }

        // 매장 등록
        authenticate {
            post {
                val requestDto = call.receive<RestaurantResisterDto>()
                val pk = restaurantServiceImpl.register(requestDto)
                call.respond(message = mapOf("pk" to pk), status = HttpStatusCode.Created)
            }
        }

        // 주소 검색 form
        authenticate {
            get("/address-form") {
                val kakaoKey = this@route.environment!!.config.property("kakao.api_key").getString()
                val apiKey = "KakaoAK $kakaoKey"
                apiKey.let {
                    call.respondHtml(status = HttpStatusCode.OK) {
                        head {
                            meta {
                                charset = "UTF-8"
                            }
                        }
                        body {
                            script {
                                src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
                            }
                            script(type = ScriptType.textJavaScript) {
                                unsafe {
                                    raw("""
                                    new daum.Postcode({
                                        oncomplete: async function(data) {
                                            const address = data.address;
                                            const apiUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
                                            // 헤더 설정
                                            const headers = new Headers();
                                            headers.append('Authorization', "$apiKey");
                                            
                                            const requestOptions = {
                                              method: 'GET',
                                              headers: headers,
                                              // 기타 다른 옵션들 (body, credentials 등)
                                            };
                                            const response = await fetch(apiUrl, requestOptions);
                                            const json = await response.json();
                                            const { x, y } = json.documents[0];
                                            const result = { address, x, y };
                                            console.log(result);
                                        }
                                    }).embed(".body");
                                """.trimIndent())
                                }
                            }
                        }
                    }
                }
            }
        }

        route("/{restaurant_pk?}/foods") {
            // 매장 음식 추가
            authenticate {
                post {
                    val restaurantPk = call.parameters["restaurant_pk"]?.toLongOrNull() ?: return@post call.respondText(
                        "${HttpStatusCode.BadRequest}: Path parameter not found or not number", status = HttpStatusCode.BadRequest
                    )
                    val requestDto = call.receive<FoodResisterDto>()
                    val pk = restaurantServiceImpl.addFood(requestDto, restaurantPk)
                    call.respond(message = mapOf("pk" to pk), status = HttpStatusCode.Created)
                }
            }
        }
    }
}