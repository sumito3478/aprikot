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

package info.sumito3478.aprikot.check
import info.sumito3478.aprikot.unsafe.{ ArrayOfByteW, Memory, benchmark }
import org.scalatest.FunSpec
import java.util.zip.{ CRC32 => JCRC32 }
import scala.Array.canBuildFrom

class CRC32Spec extends FunSpec {
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
      // `~CRC32(above Data)`. This makes `~CRC32(testData)` result to 0.
      0x99, 0x5e, 0x68, 0x37).map(_.toByte)
  }

  val testBuffer: Memory = {
    val ret = Memory(52)
    testData.memcpy(ret.pointer)
    ret
  }

  describe("CRC32.apply(Pointer, Long, Int)") {
    it("should calculate the CRC32 value.") {
      assert(CRC32(testBuffer.pointer, 48, 0) === 0xc897a166)
    }

    it("should return 0 if the CRC32 of the input is appended to that.") {
      assert((~CRC32(testBuffer.pointer, 52, 0)) === 0)
    }
  }

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
      System.gc()
      import java.util.zip.{ CRC32 => JCRC32 }
      val ret1 = benchmark {
        val j = new JCRC32()
        j.update(data)
        val r = j.getValue
      }
      println(f"java.util.zip.CRC32: ${ret1.toDouble / 1000000} sec")
      System.gc()
      val ret2 = benchmark {
        val r = CRC32C(mem.pointer, num, 0)
      }
      println(f"info.sumito3478.aprikot.math.CRC32: ${ret2.toDouble / 1000000} sec")
    }
  }
}