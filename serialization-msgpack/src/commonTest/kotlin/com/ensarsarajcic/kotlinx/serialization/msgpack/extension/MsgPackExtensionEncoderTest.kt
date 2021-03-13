package com.ensarsarajcic.kotlinx.serialization.msgpack.extension

import com.ensarsarajcic.kotlinx.serialization.msgpack.MsgPackConfiguration
import com.ensarsarajcic.kotlinx.serialization.msgpack.MsgPackEncoder
import com.ensarsarajcic.kotlinx.serialization.msgpack.extensions.MsgPackExtension
import com.ensarsarajcic.kotlinx.serialization.msgpack.toHex
import kotlinx.serialization.modules.SerializersModule
import kotlin.test.Test
import kotlin.test.assertEquals

// TODO More tests
internal class MsgPackExtensionEncoderTest {

    @Test
    fun testExtensionEncode() {
        val input = MsgPackExtension(0xd4.toByte(), 0x01, byteArrayOf(0x01))
        val expectedOutput = "d40101"
        val encoder = MsgPackEncoder(MsgPackConfiguration.default, SerializersModule {})
        val serializer = MsgPackExtension.serializer()
        serializer.serialize(encoder, input)
        assertEquals(expectedOutput, encoder.result.toByteArray().toHex())
    }
}
