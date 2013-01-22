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
 * - src/liblzma/check/crc32_tablegen.c
 * - src/liblzma/check/crc32_fast.c
 * from http://git.tukaani.org/xz.git
 * with commit 96f94bc925d579a700147fa5d7793b64d69cfc18 (HEAD of master at the
 * time I referenced them).
 * The author of C implementation is Lasse Collin. Thanks a lot!
 */

package info.sumito3478.aprikot.check

import java.lang.{ Integer => JInteger }
import info.sumito3478.aprikot.unsafe.{ Memory, Pointer, ByteOrder, bswap }
import java.lang.{ Integer => JInteger }

trait CRC32 {
  def poly: Int

  private[this] val rp = JInteger.reverse(poly)

  private[this] val table: Memory = {
    val ret = Memory(4 * 8 * 256)
    val p = ret.pointer
    0 until 8 foreach {
      s =>
        0 until 256 foreach {
          b =>
            var r = if (s == 0) b else (p + ((s - 1) * 256 + b) * 4).int
            0 until 8 foreach {
              i =>
                if ((r & 1) != 0) {
                  r = (r >>> 1) ^ rp
                } else {
                  r >>>= 1
                }
                (p + (s * 256 + b) * 4).int = r
            }
        }
    }
    ret
  }

  private[this] val t = table.pointer

  private[this] def tab(x: Int)(y: Int): Int = {
    (t + (x * 256 + y) * 4).int
  }

  private[this] val be = ByteOrder.isBE

  private[this] def A(x: Int) = if (be) x >>> 24 else x & 0xff

  private[this] def B(x: Int) = if (be) (x >>> 16) & 0xff else (x >>> 8) & 0xff

  private[this] def C(x: Int) = if (be) (x >>> 8) & 0xff else (x >>> 16) & 0xff

  private[this] def D(x: Int) = if (be) x & 0xff else x >>> 24

  private[this] def S8(x: Int) = if (be) x << 8 else x >>> 8

  private[this] def S32(x: Int) = if (be) x << 32 else x >>> 32

  def process(x: Byte, seed: Int) = {
    tab(0)((x & 0xff) ^ A(seed)) ^ S8(seed)
  }

  def process(first: Int, second: Int, seed: Int) = {
    var crc = seed
    crc ^= first
    crc = (tab(7)(A(crc))
      ^ tab(6)(B(crc))
      ^ tab(5)(C(crc))
      ^ tab(4)(D(crc)))
    crc = (tab(3)(A(second))
      ^ tab(2)(B(second))
      ^ crc
      ^ tab(1)(C(second))
      ^ tab(0)(D(second)))
    crc
  }

  def apply(p: Pointer, len: Long, seed: Int): Int = {
    var size = len
    var crc = ~seed
    crc = if (be) bswap(crc) else crc
    var buf = p
    if (size > 8) {
      // Fix alignment, if needed. The if statement above ensures that this
      // won't read past the end of the buffer.
      while ((buf.p & 7) != 0) {
        crc = process(buf.byte, crc)
        buf += 1
        size -= 1
      }
      // Calculate the position where to stop.
      val limit = p + (size & ~7)
      // Calculate how many bytes must be calculated separately before returning
      // the result.
      size &= 7
      // Calculate the CRC32 using the slice-by-eight algorithm.
      while (buf.p < limit.p) {
        val first = buf.int
        buf += 4
        val second = buf.int
        buf += 4
        crc = process(first, second, crc)
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
}

object CRC32 extends CRC32 {
  def poly = 0x04c11db7
}