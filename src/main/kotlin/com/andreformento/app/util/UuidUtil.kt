package com.andreformento.app.util

import java.nio.ByteBuffer
import java.util.*


fun UUID.asBytes(): ByteArray {
    val b = ByteBuffer.wrap(ByteArray(16))
    b.putLong(mostSignificantBits)
    b.putLong(leastSignificantBits)
    return b.array()
}

fun ByteArray.asUUID(): UUID {
    val byteBuffer = ByteBuffer.wrap(this)
    val high = byteBuffer.long
    val low = byteBuffer.long
    return UUID(high, low)
}
