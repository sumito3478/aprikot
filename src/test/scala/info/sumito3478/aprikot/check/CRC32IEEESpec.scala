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

package info.sumito3478
package aprikot.check

import java.util.zip.{ CRC32 => JCRC32 }

import aprikot.unmanaged._

class CRC32IEEESpec extends CRC32Spec {
  def crc32: CRC32 = CRC32IEEE

  def name: String = "CRC32IEEE"

  def test1ret: Int = 0x0d4a1185

  def test2ret: Int = 0x2c78b2f6

  describe("benchmark") {
    it("benchmark") {
      val num = 0xffff
      val data = new Array[Byte](num)
      0 until num foreach {
        i =>
          data(i) = (i & 0xff).toByte
      }
      val mem = Memory(num)
      data.memcpy(mem.pointer)
      import java.util.zip.{ CRC32 => JCRC32 }
      val ret1 = benchmark {
        val j = new JCRC32()
        j.update(data)
        val r = j.getValue
      }
      println(f"java.util.zip.CRC32: ${ret1.toDouble / 1000000} sec")
      val ret2 = benchmark {
        val r = CRC32IEEE(mem.pointer, num)
      }
      println(
        f"info.sumito3478.aprikot.math.CRC32IEEE: ${ret2.toDouble / 1000000} sec")
      val ret3 = benchmark {
        val r = CRC32IEEE(data)
      }
      println(
        f"info.sumito3478.aprikot.math.CRC32IEEE(given Array[Byte]): ${ret3.toDouble / 1000000} sec")
    }
  }
}