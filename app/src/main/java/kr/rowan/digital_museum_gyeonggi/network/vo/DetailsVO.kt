package kr.rowan.digital_museum_gyeonggi.network.vo

data class DetailsVO(
    var title: String,
    var images: ArrayList<ImageVO>,
    var year: String?,
    var month: String?,
    var date: String?,
    var quantity: Int,
    var size: String,
    var texture: String,
    var kind: String,
    var contributor: String,
    var origin: String,
    var reference: String,
    var language: String,
    var book_stand: String,
    var box_number: String
)
