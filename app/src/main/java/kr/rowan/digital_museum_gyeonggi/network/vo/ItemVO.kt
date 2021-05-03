package kr.rowan.digital_museum_gyeonggi.network.vo

import android.os.Parcel
import android.os.Parcelable

data class ItemVO(val uuid: String?, val title: String?, val year: String?, val path: String?) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(title)
        parcel.writeString(year)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemVO> {
        override fun createFromParcel(parcel: Parcel): ItemVO {
            return ItemVO(parcel)
        }

        override fun newArray(size: Int): Array<ItemVO?> {
            return arrayOfNulls(size)
        }
    }
}
