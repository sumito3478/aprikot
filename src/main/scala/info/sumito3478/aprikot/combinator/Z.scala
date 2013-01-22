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

package info.sumito3478.aprikot.combinator

/**
 * An implementation of z-combinator - Z(a) == {x => a(Z(a))(x)}.
 *
 * It generates a function with anonymous recursion.
 *
 * Example Usage:
 * {{{
 * import info.sumito3478.aprikot.combinator.{|>|, Z}
 *
 * val i = ({
 *   f: (Int => Int) =>
 *     {
 *       n : Int =>
 *         n match {
 *           case m if m < 2 => m
 *           case _ => f(n - 1) + f(n - 2)
 *         }
 *     }
 * } |>| Z[Int, Int])(7)
 * // i == 13
 * }}}
 */
object Z {
  def apply[X, R](a: (X => R) => (X => R)): X => R = (x: X) => a(Z(a))(x)
}