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

package info.sumito3478.aprikot.control

import scala.collection.mutable.Queue

import org.scalatest
import scalatest.FunSpec
import generator.susp

class GeneratorSpec extends FunSpec {
  describe("generator") {
    it("should support yielding") {
      val history = new Queue[Int]
      val it = generator[Int] {
        yields =>
          history.enqueue(0)
          yields(0)
          history.enqueue(1)
          yields(1)
          history.enqueue(2)
          yields(2)
          history.enqueue(3)
          yields(3)
          history.enqueue(4)
      }
      history.enqueue(5)
      it.next
      history.enqueue(6)
      it.next
      history.enqueue(7)
      assert(it.toList === List(2, 3))
      assert(history.toList === List(0, 5, 6, 1, 7, 2, 3, 4))
    }

    it("should support yielding from the while block") {
      val history = new Queue[Int]
      val it = generator[Int] {
        yields =>
          var i = 0
          while (i < 4) {
            history.enqueue(i)
            yields(i)
            i += 1
          }
          history.enqueue(i)
      }
      history.enqueue(5)
      it.next
      history.enqueue(6)
      it.next
      history.enqueue(7)
      assert(it.toList === List(2, 3))
      assert(history.toList === List(0, 5, 6, 1, 7, 2, 3, 4))
    }

    it("should support yielding from the function with @susp") {
      val history = new Queue[Int]
      val it = generator[Int] {
        yields =>
          def loop(i: Int): Unit @susp[Int] = {
            history.enqueue(i)
            if (i < 4) {
              yields(i)
              loop(i + 1)
            }
          }
          loop(0)
      }
      history.enqueue(5)
      it.next
      history.enqueue(6)
      it.next
      history.enqueue(7)
      assert(it.toList === List(2, 3))
      assert(history.toList === List(0, 5, 6, 1, 7, 2, 3, 4))
    }
  }
}