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

import scala.collection.Iterator
import scala.util.continuations.{ cps, reset, shift }

/**
 * A generator implementation with delimited continuation.
 *
 * Example usage:
 * {{{
 * import info.sumito3478.aprikot.control.generator
 * import generator.susp
 *
 * val it = generator[Int] {
 *   yields =>
 *     yields(0)
 *     yields(1)
 *     yields(2)
 *     yields(3)
 * } // => 0, 1, 2, 3
 *
 * val it2 = generator[Int] {
 *   yields =>
 *     var i = 0
 *     while(i < 4) {
 *       yields(i)
 *     }
 * } // => 0, 1, 2, 3
 *
 * val it3 = generator[Int] {
 *   yields =>
 *     def loop(i: Int): Unit @susp[Int] = {
 *       if(i < 4) {
 *         yields(i)
 *         loop(i + 1)
 *       }
 *     }
 * } // => 0, 1, 2, 3
 * }}}
 *
 */
object generator {
  sealed trait Generation[+A]

  private[this] case class Yield[+A](result: A,
    next: () => Generation[A]) extends Generation[A]

  private[this] case object Done extends Generation[Nothing]

  type susp[A] = cps[Generation[A]]

  def apply[A](body: (A => Unit @susp[A]) => Unit @susp[A]): Iterator[A] = {
    def stream(thunk: () => Generation[A]): Stream[A] = {
      thunk() match {
        case Yield(result, next) => Stream.cons(result, stream(next))
        case Done => Stream.empty
      }
    }

    stream(() => {
      reset[Generation[A], Generation[A]] {
        body {
          result: A =>
            shift {
              cont: (Unit => Generation[A]) =>
                Yield(result, () => cont())
            }
        }
        Done
      }
    }).iterator
  }
}