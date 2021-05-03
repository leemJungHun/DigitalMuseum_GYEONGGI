package kr.rowan.digital_museum_gyeonggi.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface HttpRequestService {
    companion object {
        var URL = "http://10.20.170.240:8080" //회사(인재님 서버)
    }

    @POST("api/get/top/category")
    fun getCategory(): Call<JsonArray?>?

    @POST("api/get/items")
    fun getItems(@Body uuidRequest: UuidRequest): Call<JsonArray?>?

    @POST("api/get/detail")
    fun getDetail(@Body uuidRequest: UuidRequest): Call<JsonObject?>?
}