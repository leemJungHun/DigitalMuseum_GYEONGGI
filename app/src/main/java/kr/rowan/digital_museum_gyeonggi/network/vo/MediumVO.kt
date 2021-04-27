package kr.rowan.digital_museum_gyeonggi.network.vo

import android.os.Parcel
import android.os.Parcelable

class MediumVO : Parcelable {
    var title: String? = null
    var code: String? = null
    var image: String? = null

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

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(code)
    }

    companion object CREATOR : Parcelable.Creator<MediumVO> {
        override fun createFromParcel(parcel: Parcel): MediumVO {
            return MediumVO(parcel)
        }

        override fun newArray(size: Int): Array<MediumVO?> {
            return arrayOfNulls(size)
        }
    }
}
