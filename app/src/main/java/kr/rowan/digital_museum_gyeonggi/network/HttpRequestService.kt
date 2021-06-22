package kr.rowan.digital_museum_gyeonggi.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.rowan.digital_museum_gyeonggi.network.request.UuidRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HttpRequestService {
    companion object {
        var URL = "http://10.20.170.240" //회사(인재님 서버)
        //var URL = "http://192.168.0.1:8080" // 로컬 설정
    }

    @POST("api/get/top/category")
    fun getCategory(): Call<JsonArray?>?

    @POST("api/get/items")
    fun getItems(@Body uuidRequest: UuidRequest): Call<JsonArray?>?

    @POST("api/get/detail")
    fun getDetail(@Body uuidRequest: UuidRequest): Call<JsonObject?>?
}