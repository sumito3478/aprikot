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

package info.sumito3478.aprikot.hashing

import org.scalatest.FunSpec
import info.sumito3478.aprikot.unmanaged._
import info.sumito3478.aprikot.numeric.Cent

class CityHashSpec extends FunSpec {
  /*
   * the expected values are computed by the original CityHash *v1.1*
   * implementation in C++.
   */
  val data = {
    var str = "Mis, Mis, Mister, Drill, Driller, I'll do my best, I cant lose!"
    0 until 4 foreach {
      i =>
        str = str + " " + str
    }
    val data = str.getBytes()
    val ret = Memory(data.length)
    data.memcpy(ret.pointer)
    ret
  }

  val p = data.pointer

  describe("CityHash.cityHash64WithSeed") {

    val seed = 0xdeadbeefcafebabeL

    it("return expected value for 8-byte (0 < len =< 16) input") {
      assert(CityHash.cityHash64WithSeed(p, 8, seed) === 0x3e25b800e2ef42d8L)
    }

    it("return expected value for 23-byte (16 < len =< 32) input") {
      assert(CityHash.cityHash64WithSeed(p, 23, seed) === 0x016a7d2b323985e5L)
    }

    it("return expected value for 63-byte (32 < len =< 64) input") {
      assert(CityHash.cityHash64WithSeed(p, 63, seed) === 0x460f91c680702646L)
    }

    it("return expected value for 126-byte (len > 64) input") {
      assert(CityHash.cityHash64WithSeed(p, 126, seed) === 0x9c64e03a5baf46f7L)
    }
  }

  describe("CityHash.cityHash128WithSeed") {
    val seed = new Cent(0xdeadbeefcafebabeL, 0xfacefeedfee1deadL)

    it("return expected value for 126-byte (len < 900) input") {
      CityHash.cityHash128WithSeed(p, 126, seed) === new Cent(0x64b62e405a3f61ffL, 0x0308db7b4e08a771L)
    }

    it("return expected value for 1000-byte (len > 900) input") {
      CityHash.cityHash128WithSeed(p, 1000, seed) === new Cent(0xcca027d3b1572ff0L, 0xdbbd8f4445776082L)
    }
  }
}