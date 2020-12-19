package com.ensarsarajcic.kotlinx.serialization.msgpack

import kotlinx.serialization.modules.SerializersModule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MsgPackDecoderTest {
    @Test
    fun testBooleanDecode() {
        testPairs(
            MsgPackDecoder::decodeBoolean,
            *TestData.booleanTestPairs
        )
    }

    @Test
    fun testNullDecode() {
        val decoder = MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, byteArrayOf(0xc0.toByte()))
        assertEquals(null, decoder.decodeNull())
    }

    @Test
    fun testByteDecode() {
        testPairs(
            MsgPackDecoder::decodeByte,
            *TestData.byteTestPairs
        )
    }

    @Test
    fun testShortDecode() {
        testPairs(
            MsgPackDecoder::decodeShort,
            *TestData.byteTestPairs.map { it.first to it.second.toShort() }.toTypedArray(),
            *TestData.shortTestPairs,
            *TestData.uByteTestPairs
        )
    }

    @Test
    fun testIntDecode() {
        testPairs(
            MsgPackDecoder::decodeInt,
            *TestData.byteTestPairs.map { it.first to it.second.toInt() }.toTypedArray(),
            *TestData.shortTestPairs.map { it.first to it.second.toInt() }.toTypedArray(),
            *TestData.uByteTestPairs.map { it.first to it.second.toInt() }.toTypedArray(),
            *TestData.intTestPairs,
            *TestData.uShortTestPairs,
        )
    }

    @Test
    fun testLongDecode() {
        testPairs(
            MsgPackDecoder::decodeLong,
            *TestData.byteTestPairs.map { it.first to it.second.toLong() }.toTypedArray(),
            *TestData.shortTestPairs.map { it.first to it.second.toLong() }.toTypedArray(),
            *TestData.uByteTestPairs.map { it.first to it.second.toLong() }.toTypedArray(),
            *TestData.intTestPairs.map { it.first to it.second.toLong() }.toTypedArray(),
            *TestData.uShortTestPairs.map { it.first to it.second.toLong() }.toTypedArray(),
            *TestData.longTestPairs,
            *TestData.uIntTestPairs
        )
    }

    @Test
    fun testFloatDecode() {
        TestData.floatTestPairs.forEach { (input, result) ->
            MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, input.hexStringToByteArray()).also {
                // Tests in JS were failing when == comparison was used, so threshold is now used
                val threshold = 0.00001f
                val right = it.decodeFloat()
                assertTrue("Floats should be close enough! (Threshold is $threshold) - Expected: $result - Received: $right") { result - right < threshold }
            }
        }
    }

    @Test
    fun testDoubleDecode() {
        TestData.doubleTestPairs.forEach { (input, result) ->
            MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, input.hexStringToByteArray()).also {
                // Tests in JS were failing when == comparison was used, so threshold is now used
                val threshold = 0.000000000000000000000000000000000000000000001
                val right = it.decodeDouble()
                assertTrue("Doubles should be close enough! (Threshold is $threshold) - Expected: $result - Received: $right") { result - right < threshold }
            }
        }
    }

    @Test
    fun testStringDecode() {
        testPairs(
            MsgPackDecoder::decodeString,
            *TestData.fixStrTestPairs,
            *TestData.str8TestPairs,
            *TestData.str16TestPairs
        )
    }

    private fun <RESULT> testPairs(decodeFunction: MsgPackDecoder.() -> RESULT, vararg pairs: Pair<String, RESULT>) {
        pairs.forEach { (input, result) ->
            MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, input.hexStringToByteArray()).also {
                println(input)
                assertEquals(result, it.decodeFunction())
            }
        }
    }
}