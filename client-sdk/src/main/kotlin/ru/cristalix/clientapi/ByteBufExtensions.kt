package ru.cristalix.clientapi

import com.mojang.authlib.GameProfile
import dev.xdark.feder.NetUtil
import io.netty.buffer.ByteBuf
import java.util.Date
import java.util.UUID

fun ByteBuf.readVarInt(): Int = NetUtil.readVarInt(this)
fun ByteBuf.writeVarInt(value: Int): Unit = NetUtil.writeVarInt(value, this)

fun ByteBuf.readVarLong(): Long = NetUtil.readVarLong(this)
fun ByteBuf.writeVarLong(value: Long): Unit = NetUtil.writeVarLong(value, this)

fun ByteBuf.readId(): UUID = NetUtil.readId(this)
fun ByteBuf.writeId(id: UUID): Unit = NetUtil.writeId(id, this)

fun ByteBuf.readArray(limit: Int = this.readableBytes()): ByteArray =
    NetUtil.readArray(this, limit)

fun ByteBuf.writeArray(array: ByteArray, start: Int = 0, end: Int = array.size): Unit =
    NetUtil.writeArray(array, start, end, this)

fun ByteBuf.readBuffer(limit: Int): ByteBuf = NetUtil.readBuffer(this, limit)
fun ByteBuf.writeBuffer(buf: ByteBuf): Unit = NetUtil.writeBuffer(buf, this)
fun ByteBuf.writeBuffer(buf: ByteBuf, start: Int, end: Int): Unit = NetUtil.writeBuffer(buf, start, end, this)

fun ByteBuf.readUtf8(limit: Int = Int.MAX_VALUE): String = NetUtil.readUtf8(this, limit)
fun ByteBuf.writeUtf8(s: String): Unit = NetUtil.writeUtf8(s, this)

fun ByteBuf.readAscii(limit: Int = Int.MAX_VALUE): String = NetUtil.readAscii(this, limit)
fun ByteBuf.writeAscii(s: String): Unit = NetUtil.writeAscii(s, this)

fun ByteBuf.readGameProfile(nameLimit: Int = 16): GameProfile = NetUtil.readGameProfile(this, nameLimit)
fun ByteBuf.writeGameProfile(profile: GameProfile): Unit = NetUtil.writeGameProfile(profile, this)

fun ByteBuf.readDate(): Date = NetUtil.readDate(this)
fun ByteBuf.writeDate(date: Date): Unit = NetUtil.writeDate(date, this)

fun ByteBuf.readUtf8s(climit: Int, slimit: Int): List<String> = NetUtil.readUtf8s(this, climit, slimit)
fun ByteBuf.writeUtf8s(strings: List<String>): Unit = NetUtil.writeUtf8s(strings, this)

inline fun <reified T> ByteBuf.readArray(limit: Int = Int.MAX_VALUE, noinline reader: ByteBuf.() -> T): Array<T> =
    NetUtil.readArray(this, limit, { arrayOfNulls(it) }, reader)

fun <T> ByteBuf.writeArray(
    array: Array<T>,
    off: Int = 0,
    len: Int = array.size,
    writer: ByteBuf.(T) -> Unit,
): Unit = NetUtil.writeArray(array, off, len, writer, this)
