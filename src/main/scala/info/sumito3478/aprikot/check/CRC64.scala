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

package info.sumito3478
package aprikot.check

import java.lang.{ Integer => JInteger, Long => JLong }

import aprikot.unmanaged._
import aprikot.threading._

/**
 * A base trait for CRC64 implementations.
 *
 * To create a new implementation of CRC64,
 * implement `def poly`, which returns a 64bit integer value
 * that each bits represent the coefficient value of
 * the polynominal of CRC, except for the maximum one
 * (e.g.  0x1bL, which equals to 0b1101L,
 * for x^64^ + x^4^ + x^3^ + x + 1).
 *
 * Example Usage:
 * {{{
 * import aprikot.check.CRC64
 *
 * object crc32 extends CRC32 {
 *   def poly: Int = 0x1bL
 * }
 *
 * val testData =
 *   "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
 *     getBytes("UTF-8")
 *
 * val crc64OfTestData = crc64(testData)
 *
 * // crc64OfTestData == 0x5880199a537a151eL
 * }}}
 *
 * @note
 *   Current implementation is based on the slice-by-4 algorithm,
 *   with the table and input data in unmanaged memory.
 *   If the input data is a managed array, it is copied to the thread local
 *   unmanaged memory.
 */
trait CRC64 {
  import CRC64._

  /**
   * Returns a 64bit integer value
   * that each bits represent the coefficient value of
   * the polynominal of CRC, except for the maximum one
   * (e.g.  0x1bL, which equals to 0b1101L,
   * for x^64^ + x^4^ + x^3^ + x + 1).
   *
   * @return A 64bit integer value
   * that each bits represent the coefficient value of
   * the polynominal of CRC, except for the maximum one
   */
  def poly: Long

  private[this] val rp = JLong.reverse(poly)

  private[this] val table: Memory = {
    val ret = Memory(8 * 8 * 256)
    val p = ret.pointer
    for {
      s <- 0 until 4
      b <- 0 until 256
    } {
      (p + (s * 256 + b) * 8).long = (0 until 8).foldLeft(
        if (s == 0) b else (p + ((s - 1) * 256 + b) * 8).long)(
          (r, _) => if ((r & 1) != 0) (r >>> 1) ^ rp else r >>> 1)

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

  /**
   * Returns the CRC code of the given byte and the given seed,
   * without flipping bits.
   *
   * @param x
   *   the value of which CRC code to be calculated
   * @param seed
   *   the seed to be used
   *
   * @return calculated CRC code
   */
  def process(x: Byte, seed: Long) = {
    tab(0)((x & 0xff) ^ A1(seed)) ^ S8(seed)
  }

  /**
   * Returns the CRC code of the given 32-bit value and the given seed,
   * without flipping bits.
   *
   * @param tmp
   *   the value of which CRC code to be calculated
   * @param seed
   *   the seed to be used
   *
   * @return calculated CRC code
   */
  def process(tmp: Int, seed: Long) = {
    var crc = seed
    crc = (tab(3)(A(tmp))
      ^ tab(2)(B(tmp))
      ^ S32(crc)
      ^ tab(1)(C(tmp))
      ^ tab(0)(D(tmp)))
    crc
  }

  /**
   * Returns the CRC code of the memory block that begins from the specified
   * [[Pointer]] with the specified length, using the specified seed value.
   *
   * @param p
   *   the [[Pointer]] that points to the beginning of the memory block
   * @param len
   *   the length of the memory block
   * @param seed
   *   the seed value
   *
   * @return the CRC code of the memory block
   */
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

  /**
   * Returns the CRC code of the array block, which is a part of the specified
   * byte array, beginning at the specified position, with the specified length.
   *
   * @param data
   *   the byte array that contains the array block of which CRC to be
   *   calculated
   * @param start
   *   the beginning position of the array block of which CRC to be
   *   calculated
   * @param len
   *   the length of the array block of which CRC to be calculated
   *
   * @return the CRC code of the array block
   */
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

  /**
   * Returns the CRC code of the array block, which is a part of the specified
   * byte array, beginning at the specified position.
   *
   * @param data
   *   the byte array that contains the array block of which CRC to be
   *   calculated
   * @param start
   *   the beginning position of the array block of which CRC to be
   *   calculated
   *
   * @return the CRC code of the array block
   */
  def apply(data: Array[Byte], start: Int): Long = {
    apply(data, start, data.length - start)
  }

  /**
   * Returns the CRC code of the specified byte array.
   *
   * @param data
   *   the byte array of which CRC to be calculated
   *
   * @return the CRC code of the byte array
   */
  def apply(data: Array[Byte]): Long = {
    apply(data, 0, data.length)
  }
}

private object CRC64 {
  private val buffer: ThreadLocal[Memory] = ThreadLocal.buffer
}