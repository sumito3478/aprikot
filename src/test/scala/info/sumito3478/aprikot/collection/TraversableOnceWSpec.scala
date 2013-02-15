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
package aprikot.collection

import scala.collection.immutable
import scala.collection.mutable

import org.scalatest._

class TraversableOnceWSpec extends FunSpec {
  describe("TraversableOnceW#mkBuildable") {
    it("should create Vector, flatten with the specified separator") {
      val l = List(Set(1, 2, 3, 4), Set(5, 6))
      assert(l.mkBuildable(new immutable.VectorBuilder[Int], List(0))
        === Vector(1, 2, 3, 4, 0, 5, 6))
    }

    it("should create Array, flatten with the specified separator") {
      val l = List(Set(1, 2, 3, 4), Set(5, 6))
      assert(l.mkBuildable(new mutable.ArrayBuilder.ofInt, List(0))
        === Array(1, 2, 3, 4, 0, 5, 6))
    }
  }
}