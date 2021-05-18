@file:Suppress("NOTHING_TO_INLINE")

package ru.cristalix.clientapi

import com.mojang.authlib.GameProfile
import dev.xdark.feder.NetUtil
import io.netty.buffer.ByteBuf
import java.util.*

inline fun ByteBuf.readVarInt(): Int = NetUtil.readVarInt(this)
inline fun ByteBuf.writeVarInt(value: Int): Unit = NetUtil.writeVarInt(value, this)

inline fun ByteBuf.readVarLong(): Long = NetUtil.readVarLong(this)
inline fun ByteBuf.writeVarLong(value: Long): Unit = NetUtil.writeVarLong(value, this)

inline fun ByteBuf.readId(): UUID = NetUtil.readId(this)
inline fun ByteBuf.writeId(id: UUID): Unit = NetUtil.writeId(id, this)

inline fun ByteBuf.readArray(limit: Int = this.readableBytes()): ByteArray =
    NetUtil.readArray(this, limit)

inline fun ByteBuf.writeArray(array: ByteArray, start: Int = 0, end: Int = array.size): Unit =
    NetUtil.writeArray(array, start, end, this)

inline fun ByteBuf.readBuffer(limit: Int): ByteBuf = NetUtil.readBuffer(this, limit)
inline fun ByteBuf.writeBuffer(buf: ByteBuf): Unit = NetUtil.writeBuffer(buf, this)
inline fun ByteBuf.writeBuffer(buf: ByteBuf, start: Int, end: Int): Unit = NetUtil.writeBuffer(buf, start, end, this)

inline fun ByteBuf.readUtf8(limit: Int = Int.MAX_VALUE): String = NetUtil.readUtf8(this, limit)
inline fun ByteBuf.writeUtf8(s: String): Unit = NetUtil.writeUtf8(s, this)

inline fun ByteBuf.readAscii(limit: Int = Int.MAX_VALUE): String = NetUtil.readAscii(this, limit)
inline fun ByteBuf.writeAscii(s: String): Unit = NetUtil.writeAscii(s, this)

inline fun ByteBuf.readGameProfile(nameLimit: Int = 16): GameProfile = NetUtil.readGameProfile(this, nameLimit)
inline fun ByteBuf.writeGameProfile(profile: GameProfile): Unit = NetUtil.writeGameProfile(profile, this)

inline fun ByteBuf.readDate(): Date = NetUtil.readDate(this)
inline fun ByteBuf.writeDate(date: Date): Unit = NetUtil.writeDate(date, this)

inline fun ByteBuf.readUtf8s(climit: Int, slimit: Int): List<String> = NetUtil.readUtf8s(this, climit, slimit)
inline fun ByteBuf.writeUtf8s(strings: List<String>): Unit = NetUtil.writeUtf8s(strings, this)

//inline fun ByteBuf.readVarIntArray(IntArray Into, Int limit): IntArray = NetUtil.readVarIntArray(this)
//inline fun ByteBuf.readVarIntArray(Int limit): IntArray = NetUtil.readVarIntArray(this)
//inline fun ByteBuf.readVarIntArray(IntArray Into): IntArray = NetUtil.readVarIntArray(this)
//inline fun ByteBuf.readVarIntArray(ByteBuf input): IntArray = NetUtil.readVarIntArray(this)
//inline fun ByteBuf.writeVarIntArray(IntArray Ints, Int from, Int to): Unit = NetUtil.writeVarIntArray(this)
//inline fun ByteBuf.writeVarIntArray(IntArray Ints): Unit = NetUtil.writeVarIntArray(this)
//inline fun ByteBuf.readLongArray(LongArray Into, Int limit): LongArray = NetUtil.readLongArray(this)
//inline fun ByteBuf.readLongArray(Int limit): LongArray = NetUtil.readLongArray(this)
//inline fun ByteBuf.readLongArray(LongArray Into): LongArray = NetUtil.readLongArray(this)
//inline fun ByteBuf.readLongArray(ByteBuf input): LongArray = NetUtil.readLongArray(this)
//inline fun ByteBuf.writeLongArray(LongArray Longs, Int from, Int to): Unit = NetUtil.writeLongArray(this)
//inline fun ByteBuf.writeLongArray(LongArray Longs): Unit = NetUtil.writeLongArray(this)
//inline fun <T : Enum<T>> ByteBuf.readEnum(Array<T> values, ByteBuf buf): T = NetUtil.readEnum(this)
//inline fun ByteBuf.readEnum(Class<T> type, ByteBuf buf): <T : Enum<T>> T = NetUtil.readEnum(this)
//inline fun ByteBuf.writeEnum(Enum<?> value , ByteBuf buf): Unit = NetUtil.writeEnum(this)
//inline fun ByteBuf.readOptionalUtf8(Int limit): String = NetUtil.readOptionalUtf8(this)
//inline fun ByteBuf.readOptionalUtf8(ByteBuf input): String = NetUtil.readOptionalUtf8(this)
//inline fun ByteBuf.readOptionalAscii(Int limit): String = NetUtil.readOptionalAscii(this)
//inline fun ByteBuf.readOptionalAscii(ByteBuf input): String = NetUtil.readOptionalAscii(this)
//inline fun ByteBuf.writeOptionalUtf8(String s): Unit = NetUtil.writeOptionalUtf8(this)
//inline fun ByteBuf.writeOptionalAscii(String s): Unit = NetUtil.writeOptionalAscii(this)


inline fun <reified T> ByteBuf.readArray(limit: Int = Int.MAX_VALUE, noinline reader: ByteBuf.() -> T): Array<T> =
    NetUtil.readArray(this, limit, { arrayOfNulls(it) }, reader)

inline fun <T> ByteBuf.writeArray(
    array: Array<T>,
    off: Int = 0,
    len: Int = array.size,
    noinline writer: ByteBuf.(T) -> Unit
): Unit =
    NetUtil.writeArray(array, off, len, writer, this)

//inline fun ByteBuf.writeArray(TArray array, BiConsumer<ByteBuf, T> writer): <T> Unit = NetUtil.writeArray(this)
//inline fun ByteBuf.readCollection(Int limit, IntFunction<C> collection, Function<ByteBuf, T> reader): <T, C : Collection<T>> C = NetUtil.readCollection(this)
//inline fun ByteBuf.readCollection(IntFunction<C> collection, Function<ByteBuf, T> reader): <T, C : Collection<T>> C = NetUtil.readCollection(this)
//inline fun ByteBuf.writeCollection(Collection<T> collection, BiConsumer<ByteBuf, T> writer): <T> Unit = NetUtil.writeCollection(this)
//inline fun ByteBuf.writeList(List<T> list, Int off, Int length, BiConsumer<ByteBuf, T> writer): <T> Unit = NetUtil.writeList(this)
//inline fun ByteBuf.writeList(List<T> list, BiConsumer<ByteBuf, T> writer): <T> Unit = NetUtil.writeList(this)
//inline fun ByteBuf.readMap(Int limit, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader, TriConsumer<M, K, V> put): <K, V, M : Map<K, V>> M = NetUtil.readMap(this)
//inline fun ByteBuf.readMap(IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader, TriConsumer<M, K, V> put): <K, V, M : Map<K, V>> M = NetUtil.readMap(this)
//inline fun ByteBuf.readMap(Int limit, IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader): <K, V, M : Map<K, V>> M = NetUtil.readMap(this)
//inline fun ByteBuf.readMap(IntFunction<M> map, Function<ByteBuf, K> keyReader, Function<ByteBuf, V> valueReader): <K, V, M : Map<K, V>> M = NetUtil.readMap(this)
//inline fun ByteBuf.writeMap(Map<K, V> map, BiConsumer<ByteBuf, K> keyWriter, BiConsumer<ByteBuf, V> valueWriter): <K, V> Unit = NetUtil.writeMap(this)
//inline fun ByteBuf.varIntSize(Int varInt): Int = NetUtil.varIntSize(this)
//inline fun ByteBuf.hexDump(ByteBuf buf, Int maxLen): String = NetUtil.hexDump(this)
//inline fun ByteBuf.close(Channel ch): Unit = NetUtil.close(this)
//inline fun ByteBuf.close(Channel ch, Throwable t) throws T: <T : Throwable> Unit = NetUtil.close(this)
//inline fun ByteBuf.inEventLoop(EventLoop eventLoop, Runnable command): Unit = NetUtil.inEventLoop(this)
//inline fun ByteBuf.directWriteAndFlush(Channel ch, ByteBuf buf, boolean invokeFlush): Unit = NetUtil.directWriteAndFlush(this)