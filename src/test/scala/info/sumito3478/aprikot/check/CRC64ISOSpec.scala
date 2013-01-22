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
import info.sumito3478.aprikot.unsafe.{ ArrayOfByteW, Memory, benchmark, LongW }

import org.scalatest.FunSpec

class CRC64ISOSpec extends FunSpec {
  val test1 = "hello world".getBytes("UTF-8")

  val test1ret = 0xb9cf3f572ad9ac3eL

  val test1buffer = {
    val ret = Memory(test1.length + 8)
    test1.memcpy(ret.pointer)
    (ret.pointer + test1.length).long = (~test1ret).toLE
    ret
  }

  val test2 =
    "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
      getBytes("UTF-8")

  val test2ret = 0x5880199a537a151eL

  val test2buffer = {
    val ret = Memory(test2.length + 8)
    test2.memcpy(ret.pointer)
    (ret.pointer + test2.length).long = (~test2ret).toLE
    ret
  }

  describe("CRC64ISO.apply(Pointer, Long, Long)") {
    it("should calculate the CRC64ISO value of test1.") {
      assert(CRC64ISO(test1buffer.pointer, test1.length, 0) === test1ret)
    }

    it("should return 0 if the CRC64ISO of the test1 is appended to that.") {
      assert((~CRC64ISO(test1buffer.pointer, test1.length + 8, 0)) === 0)
    }
    it("should calculate the CRC64ISO value of test2.") {
      assert(CRC64ISO(test2buffer.pointer, test2.length, 0) === test2ret)
    }

    it("should return 0 if the CRC64ISO of test2 is appended to that.") {
      assert((~CRC64ISO(test2buffer.pointer, test2.length + 8, 0)) === 0)
    }
  }
}