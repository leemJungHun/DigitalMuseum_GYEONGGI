package kr.rowan.digital_museum_gyeonggi.network.vo

import android.os.Parcel
import android.os.Parcelable

class CategoryVO : Parcelable {
    var name: String? = null
    var uuid: String? = null

    constructor(name: String?, code: String?) {
        this.name = name
        this.uuid = code
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        uuid = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeString(uuid)
    }

    companion object CREATOR : Parcelable.Creator<CategoryVO> {
        override fun createFromParcel(parcel: Parcel): CategoryVO {
            return CategoryVO(parcel)
        }

        override fun newArray(size: Int): Array<CategoryVO?> {
            return arrayOfNulls(size)
        }
    }
}
