package kr.rowan.digital_museum_gyeonggi.network.vo

import android.os.Parcel
import android.os.Parcelable

class SmallVO : Parcelable {
    var title: String?
    var code: String?
    var image: String?

    constructor(title: String?, code: String?, image: String?) {
        this.title = title
        this.code = code
        this.image = image
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        code = `in`.readString()
        image = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(code)
        parcel.writeString(image)
    }

    companion object CREATOR : Parcelable.Creator<SmallVO> {
        override fun createFromParcel(parcel: Parcel): SmallVO {
            return SmallVO(parcel)
        }

        override fun newArray(size: Int): Array<SmallVO?> {
            return arrayOfNulls(size)
        }
    }
}
