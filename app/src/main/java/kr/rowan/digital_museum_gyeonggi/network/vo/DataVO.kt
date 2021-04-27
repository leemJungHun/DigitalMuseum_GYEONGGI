package kr.rowan.digital_museum_gyeonggi.network.vo

import android.os.Parcel
import android.os.Parcelable

class DataVO : Parcelable {
    var title: String?
    var code: String?
    var productedAt: String?
    var image: String?
    var content: String?

    constructor(
        title: String?,
        code: String?,
        productedAt: String?,
        image: String?,
        content: String?
    ) {
        this.title = title
        this.code = code
        this.productedAt = productedAt
        this.image = image
        this.content = content
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        code = `in`.readString()
        productedAt = `in`.readString()
        image = `in`.readString()
        content = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(code)
        dest.writeString(productedAt)
        dest.writeString(image)
        dest.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataVO> {
        override fun createFromParcel(parcel: Parcel): DataVO {
            return DataVO(parcel)
        }

        override fun newArray(size: Int): Array<DataVO?> {
            return arrayOfNulls(size)
        }
    }
}
