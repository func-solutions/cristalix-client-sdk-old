@file:Suppress("NOTHING_TO_INLINE")

package ru.cristalix.clientapi

import com.mojang.authlib.GameProfile
import dev.xdark.feder.NetUtil
import io.netty.buffer.ByteBuf
import java.util.*

inline fun ByteBuf.readVarInt(): Int =
    NetUtil.readVarInt(this)
inline fun ByteBuf.writeVarInt(value: Int): Unit =
    NetUtil.writeVarInt(value, this)

inline fun ByteBuf.readVarLong(): Long =
    NetUtil.readVarLong(this)
inline fun ByteBuf.writeVarLong(value: Long): Unit =
    NetUtil.writeVarLong(value, this)

inline fun ByteBuf.readId(): UUID =
    NetUtil.readId(this)
inline fun ByteBuf.writeId(id: UUID): Unit =
    NetUtil.writeId(id, this)

inline fun ByteBuf.readArray(limit: Int = readableBytes()): ByteArray =
    NetUtil.readArray(this, limit)

inline fun ByteBuf.writeArray(array: ByteArray, start: Int = 0, end: Int = array.size): Unit =
    NetUtil.writeArray(array, start, end, this)

inline fun ByteBuf.readBuffer(limit: Int): ByteBuf =
    NetUtil.readBuffer(this, limit)
inline fun ByteBuf.writeBuffer(buf: ByteBuf): Unit =
    NetUtil.writeBuffer(buf, this)
inline fun ByteBuf.writeBuffer(buf: ByteBuf, start: Int, end: Int): Unit =
    NetUtil.writeBuffer(buf, start, end, this)

inline fun ByteBuf.readUtf8(limit: Int = Int.MAX_VALUE): String =
    NetUtil.readUtf8(this, limit)
inline fun ByteBuf.writeUtf8(s: String): Unit =
    NetUtil.writeUtf8(s, this)

inline fun ByteBuf.readAscii(limit: Int = Int.MAX_VALUE): String =
    NetUtil.readAscii(this, limit)
inline fun ByteBuf.writeAscii(s: String): Unit =
    NetUtil.writeAscii(s, this)

inline fun ByteBuf.readGameProfile(nameLimit: Int = 16): GameProfile =
    NetUtil.readGameProfile(this, nameLimit)
inline fun ByteBuf.writeGameProfile(profile: GameProfile): Unit =
    NetUtil.writeGameProfile(profile, this)

inline fun ByteBuf.readDate(): Date =
    NetUtil.readDate(this)
inline fun ByteBuf.writeDate(date: Date): Unit =
    NetUtil.writeDate(date, this)

inline fun ByteBuf.readUtf8s(climit: Int, slimit: Int): List<String> =
    NetUtil.readUtf8s(this, climit, slimit)
inline fun ByteBuf.writeUtf8s(strings: List<String>): Unit =
    NetUtil.writeUtf8s(strings, this)

inline fun <reified T> ByteBuf.readArray(limit: Int = Int.MAX_VALUE, noinline reader: ByteBuf.() -> T): Array<T> =
    NetUtil.readArray(this, limit, { arrayOfNulls(it) }, reader)

inline fun <T> ByteBuf.writeArray(
    array: Array<T>,
    off: Int = 0,
    len: Int = array.size,
    noinline writer: ByteBuf.(T) -> Unit
): Unit = NetUtil.writeArray(array, off, len, writer, this)
