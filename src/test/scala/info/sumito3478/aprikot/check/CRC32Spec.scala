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

import org.scalatest._

import aprikot.unmanaged._

trait CRC32Spec extends FunSpec {
  def crc32: CRC32

  def name: String

  def test1ret: Int

  def test2ret: Int

  val test1 = "hello world".getBytes("UTF-8")

  val test1buffer = {
    val ret = Memory(test1.length + 4)
    test1.memcpy(ret.pointer)
    (ret.pointer + test1.length).int = (~test1ret).toLE
    ret
  }

  val test2 =
    "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!".
      getBytes("UTF-8")

  val test2buffer = {
    val ret = Memory(test2.length + 4)
    test2.memcpy(ret.pointer)
    (ret.pointer + test2.length).int = (~test2ret).toLE
    ret
  }

  describe(f"${name}.apply(Pointer, Long)") {
    it(f"should calculate the ${name} value of test1.") {
      assert(crc32(test1buffer.pointer, test1.length) === test1ret)
    }

    it(f"should return 0 if the ${name} of the test1 is appended to that.") {
      assert((~crc32(test1buffer.pointer, test1.length + 4)) === 0)
    }
    it(f"should calculate the ${name} value of test2.") {
      assert(crc32(test2buffer.pointer, test2.length) === test2ret)
    }

    it(f"should return 0 if the ${name} of test2 is appended to that.") {
      assert((~crc32(test2buffer.pointer, test2.length + 4)) === 0)
    }
  }

  describe(f"${name}.apply(Array[Byte])") {
    it(f"should calculate the ${name} value of test1.") {
      assert(crc32(test1) === test1ret)
    }

    it(f"should calculate the ${name} value of test2.") {
      assert(crc32(test2) === test2ret)
    }
  }
}