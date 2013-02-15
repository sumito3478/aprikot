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

package info.sumito3478
package aprikot.check

import java.lang.{ Integer => JInteger }

import aprikot.unmanaged._
import aprikot.threading._

/**
 * A base trait for CRC32 implementations.
 *
 * To create a new implementation of CRC32,
 * implement `def poly`, which returns a 32bit integer value
 * that each bits represent the coefficient value of
 * the polynominal of CRC, except for the maximum one
 * (e.g.  0x04c11db7, which equals to 0b00000100110000010001110110110111,
 * for x^32^ + x^26^ + x^23^ + x^22^ + x^16^ + x^12^ + x^11^ + x^10^ +
 * x^8^ + x^7^ + x^5^ + x^4^ + x^2^ + x + 1).
 *
 * Example Usage:
 * {{{
 * import aprikot.check.CRC32
 *
 * object crc32 extends CRC32 {
 *   def poly: Int = 0x04c11db7
 * }
 *
 * val testData =
 *   "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
 *     getBytes("UTF-8")
 *
 * val crc32OfTestData = crc32(testData)
 *
 * // crc32OfTestData == 0x2c78b2f6
 * }}}
 *
 * @note
 *   Current implementation is based on the slice-by-8 algorithm,
 *   with the table and input data in unmanaged memory.
 *   If the input data is a managed array, it is copied to the thread local
 *   unmanaged memory.
 */
trait CRC32 {
  import CRC32._

  /**
   * Returns a 32bit integer value
   * that each bits represent the coefficient value of
   * the polynominal of CRC, except for the maximum one
   * (e.g.  0x04c11db7, which equals to 0b00000100110000010001110110110111,
   * for x^32^ + x^26^ + x^23^ + x^22^ + x^16^ + x^12^ + x^11^ + x^10^ +
   * x^8^ + x^7^ + x^5^ + x^4^ + x^2^ + x + 1).
   *
   * @return A 32bit integer value
   * that each bits represent the coefficient value of
   * the polynominal of CRC, except for the maximum one
   */
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
  def process(x: Byte, seed: Int) = {
    tab(0)((x & 0xff) ^ A(seed)) ^ S8(seed)
  }

  /**
   * Returns the CRC code of the given two 32-bit values and the given seed,
   * without flipping bits.
   *
   * @param first
   *   the first value of which CRC code to be calculated
   * @param second
   *   the second value of which CRC code to be calculated
   * @param seed
   *   the seed to be used
   *
   * @return calculated CRC code
   */
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
      val limit = buf + (size & ~7)
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

  /**
   * Returns the CRC code of the memory block that begins from the specified
   * [[Pointer]] with the specified length, using 0 as a seed value.
   *
   * @param p the [[Pointer]] that points to the beginning of the memory block
   * @param len the length of the memory block
   *
   * @return the CRC code of the memory block
   */
  def apply(p: Pointer, len: Long): Int = apply(p, len, 0)

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
  def apply(data: Array[Byte], start: Int, len: Int): Int = {
    val buf = buffer().pointer
    var i = start
    var size = len
    var crc = 0
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
  def apply(data: Array[Byte], start: Int): Int = {
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
  def apply(data: Array[Byte]): Int = {
    apply(data, 0, data.length)
  }
}

private object CRC32 {
  private val buffer: ThreadLocal[Memory] = ThreadLocal.buffer
}