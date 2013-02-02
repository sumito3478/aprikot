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

import org.scalatest
import scalatest.FunSpec
import info.sumito3478.aprikot.__

class BreakableSpec extends FunSpec {
  describe("breakable") {
    it("should pass the break function that cause early exit from the block") {
      var i = 0
      val j = true
      breakable[__] {
        break =>
          if (j)
            break
          else
            i = 1
      }
      assert(i === 0)
    }

    it("should return AnyVal value that the block returns") {
      val j = false
      val i = breakable[Int] {
        break =>
          if (j)
            break(0)
          else
            1
      }
      assert(i === 1)
    }

    it("should return AnyRef value that the block returns") {
      val j = false
      val i = breakable[String] {
        break =>
          if (j)
            break("break!")
          else
            "return!"
      }
      assert(i === "return!")
    }

    it("should return AnyVal value that is passed to break") {
      val j = true
      val i = breakable[Int] {
        break =>
          if (j)
            break(0)
          else
            1
      }
      assert(i === 0)
    }

    it("should return AnyRef value that is passed to break") {
      val j = true
      val i = breakable[String] {
        break =>
          if (j)
            break("break!")
          else
            "return!"
      }
      assert(i === "break!")
    }

    it("""should pass the break function that causes non-local exit from the
        |  block""".stripMargin) {
      var i = 0
      val j = true

      def f(break: => Nothing) = {
        if (j)
          break
        else
          i = 1
      }

      breakable[__] {
        break =>
          f(break)
      }
      assert(i === 0)
    }
  }
}