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

import info.sumito3478.aprikot.unsafe.{ ArrayOfByteW, Memory, IntW }
import org.scalatest.FunSpec
import info.sumito3478.aprikot.check.CRC32C
import scala.Array.canBuildFrom

class CRC32CSpec extends FunSpec {
  val test1 = "hello world".getBytes("UTF-8")

  val test1ret = 0xc99465aa

  val test1buffer = {
    val ret = Memory(test1.length + 4)
    test1.memcpy(ret.pointer)
    (ret.pointer + test1.length).int = (~test1ret).toLE
    ret
  }

  val test2 =
    "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
      getBytes("UTF-8")

  val test2ret = 0xffc0796d

  val test2buffer = {
    val ret = Memory(test2.length + 4)
    test2.memcpy(ret.pointer)
    (ret.pointer + test2.length).int = (~test2ret).toLE
    ret
  }

  describe("CRC32C.apply(Pointer, Long, Int)") {
    it("should calculate the CRC32C value of test1.") {
      assert(CRC32C(test1buffer.pointer, test1.length, 0) === test1ret)
    }

    it("should return 0 if the CRC32C of the test1 is appended to that.") {
      assert((~CRC32C(test1buffer.pointer, test1.length + 4, 0)) === 0)
    }
    it("should calculate the CRC32C value of test2.") {
      assert(CRC32C(test2buffer.pointer, test2.length, 0) === test2ret)
    }

    it("should return 0 if the CRC32C of test2 is appended to that.") {
      assert((~CRC32C(test2buffer.pointer, test2.length + 4, 0)) === 0)
    }
  }
}