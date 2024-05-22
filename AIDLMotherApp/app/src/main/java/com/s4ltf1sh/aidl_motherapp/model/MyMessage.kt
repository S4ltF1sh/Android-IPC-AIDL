package com.s4ltf1sh.aidl_motherapp.model

import android.os.Parcel
import android.os.Parcelable

class MyMessage() : Parcelable {
    var content: String = ""
    var time: Long = 0L

    companion object CREATOR : Parcelable.Creator<MyMessage> {
        override fun createFromParcel(parcel: Parcel): MyMessage {
            return MyMessage(parcel)
        }

        override fun newArray(size: Int): Array<MyMessage?> {
            return arrayOfNulls(size)
        }
    }

    private constructor(input: Parcel?) : this() {
        readFromParcel(input)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(content)
        dest.writeLong(time)
    }

    private fun readFromParcel(input: Parcel?) {
        content = input?.readString() ?: "Message null"
        time = input?.readLong() ?: -1L
    }

    override fun describeContents(): Int {
        return 0
    }
}
