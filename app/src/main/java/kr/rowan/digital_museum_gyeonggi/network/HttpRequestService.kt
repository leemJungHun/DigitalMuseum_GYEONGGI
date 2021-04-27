package kr.rowan.digital_museum_gyeonggi.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface HttpRequestService {
    companion object {
        //String URL = "http://10.144.27.5:8080"; //학교
        //String URL = "http://10.20.170.234:8080"; //회사(우재님 서버)
        var URL = "http://10.20.170.240:8080" //회사(인재님 서버)
    }

    @POST("kyodong/v1/get/medium/{code}/list")
    fun getSmallList(@Path("code") code: String?): Call<JsonObject?>?

    @POST("kyodong/v1/get/small/{code}/list")
    fun getContentsList(@Path("code") code: String?): Call<JsonObject?>?

    @POST("kyodong/v1/get/data/{code}/")
    fun getData(@Path("code") code: String?): Call<JsonObject?>?
}