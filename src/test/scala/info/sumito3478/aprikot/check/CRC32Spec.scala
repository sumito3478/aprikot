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
import info.sumito3478.aprikot.unsafe.{ ArrayOfByteW, Memory, benchmark, IntW }
import org.scalatest.FunSpec
import java.util.zip.{ CRC32 => JCRC32 }
import scala.Array.canBuildFrom

class CRC32Spec extends FunSpec {
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

  val test1 = "hello world".getBytes("UTF-8")

  val test1ret = 0x0d4a1185

  val test1buffer = {
    val ret = Memory(test1.length + 4)
    test1.memcpy(ret.pointer)
    (ret.pointer + test1.length).int = (~test1ret).toLE
    ret
  }

  val test2 =
    "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
      getBytes("UTF-8")

  val test2ret = 0x2c78b2f6

  val test2buffer = {
    val ret = Memory(test2.length + 4)
    test2.memcpy(ret.pointer)
    (ret.pointer + test2.length).int = (~test2ret).toLE
    ret
  }

  describe("CRC32.apply(Pointer, Long, Int)") {
    it("should calculate the CRC32 value of test1.") {
      assert(CRC32(test1buffer.pointer, test1.length, 0) === test1ret)
    }

    it("should return 0 if the CRC32 of the test1 is appended to that.") {
      assert((~CRC32(test1buffer.pointer, test1.length + 4, 0)) === 0)
    }
    it("should calculate the CRC32 value of test2.") {
      assert(CRC32(test2buffer.pointer, test2.length, 0) === test2ret)
    }

    it("should return 0 if the CRC32 of test2 is appended to that.") {
      assert((~CRC32(test2buffer.pointer, test2.length + 4, 0)) === 0)
    }
  }
}