/* Copyright (C) 2013 sumito3478 <sumito3478@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Note:
 * This implementation is a port to Scala from the public domain implementation
 * written in C.
 * I took that C implementation from XZ Utils (actually, most part of XZ Utils
 * is publish domain):
 * - src/liblzma/check/crc_macros.h
 * - src/liblzma/check/crc64_tablegen.c
 * - src/liblzma/check/crc64_fast.c
 * from http://git.tukaani.org/xz.git
 * with commit 96f94bc925d579a700147fa5d7793b64d69cfc18 (HEAD of master at the
 * time I referenced them).
 * The author of C implementation is Lasse Collin. Thanks a lot!
 */

package info.sumito3478.aprikot.check

import java.lang.{ Long => JLong }
import info.sumito3478.aprikot.unsafe.{ Memory, Pointer, ByteOrder, bswap, ArrayOfByteW }
import java.lang.{ Integer => JInteger }
import info.sumito3478.aprikot.threading.ThreadLocal

trait CRC64 {
  import CRC64._

  def poly: Long

  private[this] val rp = JLong.reverse(poly)

  private[this] val table: Memory = {
    val ret = Memory(8 * 8 * 256)
    val p = ret.pointer
    0 until 4 foreach {
      s =>
        0 until 256 foreach {
          b =>
            var r = if (s == 0) b else (p + ((s - 1) * 256 + b) * 8).long
            0 until 8 foreach {
              i =>
                if ((r & 1) != 0) {
                  r = (r >>> 1) ^ rp
                } else {
                  r >>>= 1
                }
                (p + (s * 256 + b) * 8).long = r
            }
        }
    }
    ret
  }

  private[this] val t = table.pointer

  private[this] def tab(x: Long)(y: Long): Long = {
    (t + (x * 256 + y) * 8).long
  }

  private[this] val be = ByteOrder.isBE

  private[this] def A(x: Int) = if (be) x >>> 24 else x & 0xff

  private[this] def B(x: Int) = if (be) (x >>> 16) & 0xff else (x >>> 8) & 0xff

  private[this] def C(x: Int) = if (be) (x >>> 8) & 0xff else (x >>> 16) & 0xff

  private[this] def D(x: Int) = if (be) x & 0xff else x >>> 24

  private[this] def S8(x: Long) = if (be) x << 8 else x >>> 8

  private[this] def S32(x: Long) = if (be) x << 32 else x >>> 32

  private[this] def A1(x: Long) = if (be) x >>> 56 else A((x & 0xffff).toInt)

  def process(x: Byte, seed: Long) = {
    tab(0)((x & 0xff) ^ A1(seed)) ^ S8(seed)
  }

  def process(tmp: Int, seed: Long) = {
    var crc = seed
    crc = (tab(3)(A(tmp))
      ^ tab(2)(B(tmp))
      ^ S32(crc)
      ^ tab(1)(C(tmp))
      ^ tab(0)(D(tmp)))
    crc
  }

  def apply(p: Pointer, len: Long, seed: Long): Long = {
    var size = len
    var crc = ~seed
    crc = if (be) bswap(crc) else crc
    var buf = p
    if (size > 4) {
      while ((buf.p & 3) != 0) {
        crc = process(buf.byte, crc)
        buf += 1
        size -= 1
      }
      val limit = buf + (size & ~3)
      size &= 3
      while (buf.p < limit.p) {
        val tmp = if (be) {
          (crc >>> 32).toInt ^ buf.int
        } else {
          crc.toInt ^ buf.int
        }
        buf += 4
        crc = process(tmp, crc)
      }
    }
    while (size != 0) {
      crc = process(buf.byte, crc)
      buf += 1
      size -= 1
    }
    crc = if (be) bswap(crc) else crc
    ~crc
  }

  def apply(data: Array[Byte], start: Int, len: Int): Long = {
    val buf = buffer().pointer
    var i = start
    var size = len
    var crc = 0L
    if (len > 0x1000) {
      var limit = i + (size & ~0xfff)
      size &= 0xfff
      while (i < limit) {
        data.memcpy(buf, i, 0x1000)
        i += 0x1000
        crc = apply(buf, 0x1000, crc)
      }
    }
    data.memcpy(buf, i, size)
    crc = apply(buf, size, crc)
    crc
  }

  def apply(data: Array[Byte], start: Int): Long = {
    apply(data, start, data.length - start)
  }

  def apply(data: Array[Byte]): Long = {
    apply(data, 0, data.length)
  }
}

private object CRC64 {
  private val buffer: ThreadLocal[Memory] = ThreadLocal.buffer
}