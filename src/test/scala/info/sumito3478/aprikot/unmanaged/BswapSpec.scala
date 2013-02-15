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
package aprikot.unmanaged

import scala.concurrent.util.Unsafe.{ instance => _unsafe }

import java.nio._

import org.scalatest._

class BswapSpec extends FunSpec {
  describe("bswap") {
    it("should swap bytes in Long") {
      assert(bswap(0xdeadbeefcafebabeL) === 0xbebafecaefbeaddeL)
    }

    it("should swap bytes in Int") {
      assert(bswap(0xcafebabe) === 0xbebafeca)
    }

    it("should swap bytes in Short") {
      assert(bswap(0x1234: Short) === 0x3412)
    }
  }
}