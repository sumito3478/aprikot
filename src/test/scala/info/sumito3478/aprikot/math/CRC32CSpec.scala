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

package info.sumito3478.aprikot.math

import info.sumito3478.aprikot.unsafe.{ ArrayOfByteW, Memory }

import org.scalatest.FunSpec

class CRC32CSpec extends FunSpec {
  val testData: Array[Byte] = {
    Array[Int](
      // Data.
      0x01, 0xC0, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      0x01, 0xFE, 0x60, 0xAC,
      0x00, 0x00, 0x00, 0x08,
      0x00, 0x00, 0x00, 0x04,
      0x00, 0x00, 0x00, 0x09,
      0x25, 0x00, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      0x00, 0x00, 0x00, 0x00,
      // `~CRC32C(above Data)`. This makes `~CRC32C(testData)` result to 0.
      0xeb, 0x75, 0x4f, 0x66).map(_.toByte)
  }

  val testBuffer: Memory = {
    val ret = Memory(52)
    testData.memcpy(ret.pointer)
    ret
  }

  describe("CRC32C.apply(Pointer, Long, Int)") {
    it("should calculate the CRC32C value.") {
      println(f"${CRC32C(testBuffer.pointer, 48, 0)}%x")
      assert(CRC32C(testBuffer.pointer, 48, 0) === 0x99b08a14)
    }

    it("should return 0 if the CRC32C of the input is appended to that.") {
      assert((~CRC32C(testBuffer.pointer, 52, 0)) === 0)
    }
  }
}