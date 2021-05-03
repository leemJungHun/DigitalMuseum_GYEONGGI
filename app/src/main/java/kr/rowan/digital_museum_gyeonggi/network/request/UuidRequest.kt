package kr.rowan.digital_museum_gyeonggi.network.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UuidRequest(
    @SerializedName("uuid") val uuid: String
): Serializable