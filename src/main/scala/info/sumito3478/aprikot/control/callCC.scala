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
package aprikot.control

import scala.util.continuations._

/**
 * Calls a function with current continuation.
 *
 * This function is identical to shift[A, Unit, Unit].
 *
 * Exmaple Usage:
 * {{{
 * import scala.util.continuations._
 * import aprikot.control.callCC
 *
 * // A continuation passing style method.
 * def someMethod(i: Int, continuation: Int => Unit): Unit = {
 *   continuation(i * 2)
 * }
 *
 * // Use Scala's delimited continuation!
 * def someMethod(i: Int): Int @suspendable = callCC(someMethod(i, _))
 *
 * // ...
 *
 * reset {
 *   val n = someMethod(1)
 *   // n == 2
 * }
 * }}}
 */
object callCC {
  def apply[A](f: (A => Unit) => Unit): A @suspendable =
    shift[A, Unit, Unit](f)
}