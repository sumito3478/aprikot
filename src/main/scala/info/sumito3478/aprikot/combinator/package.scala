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

package info.sumito3478.aprikot

package object combinator {
  /**
   * An implicit value class to add combinator methods to [[scala.Any]].
   */
  implicit class AnyW[A](val a: A) extends AnyVal {
    /**
     * T-Combinator operator: `a |>| b == b(a)`.
     *
     * This operator should pass the result of the left side to the function on
     * the right side (forward pipeline operator).
     *
     * Example Usage:
     * {{{
     * import info.sumito3478.aprikot.combinator.{|>|, |<|}
     * val abs = math.abs _
     * val
     * val ret = -1 |>| abs |>|
     * // ret == max(min(1, 2), 0)
     * }}}
     *
     * @note This operator corresponds to the `|>` operator in F#.
     */
    def |>|[B](b: A => B): B = b(a)

    /**
     * T-Combinator operator for Function2: `a |>| b == b(a, _)`.
     *
     * This operator should pass the result of the left side to the function on
     * the right side (forward pipeline operator).
     *
     * Example Usage:
     * {{{
     * import info.sumito3478.aprikot.combinator.{|>|, |<|}
     * val min = math.min _
     * val ret = 1 |>| min |<| 2 |>| max |<| 0
     * // ret == max(min(1, 2), 0)
     * }}}
     *
     * @note This operator corresponds to the `|>` operator in F#.
     */
    def |>|[B, X](b: (A, X) => B): X => B = b(a, _)
  }

  /**
   * An implicit value class to add combinator methods to [[scala.Function1]].
   */
  implicit class Function1W[A, B](val a: B => A) extends AnyVal {
    /**
     * I*-Combinator operator: `a |<| b == a(b)`.
     *
     * This operator should pass the result of the right side to the function
     * on the left side (backward pipeline operator).
     *
     * Example Usage:
     * {{{
     * import info.sumito3478.aprikot.combinator.{|>|, |<|}
     * val min = math.min _
     * val ret = 1 |>| min |<| 2 |>| max |<| 0
     * // ret == max(min(1, 2), 0)
     * }}}
     *
     * @note This operator corresponds to the `<|` operator in F#.
     */
    def |<|(b: B): A = a(b)

    /**
     * S-Combinator operator: `a |*| c == b => c(b, a(b))`.
     *
     * Example Usage:
     * {{{
     * val cos = math.cos _
     * val max = math.max(_: Double, _: Double)
     * val cosSmax = cos |*| max // Returns the max of x and cos(x)!
     * // cosSmax(0.3) === max(0.3, cos(0.3))
     * }}}
     */
    def |*|[C](c: (B, A) => C): B => C = b => c(b, a(b))
  }

  /**
   * An implicit value class to add combinator methods to [[scala.Function1]].
   */
  implicit class Function2W[A, B, C](val a: (C, B) => A) extends AnyVal {
    /**
     * S-Combinator operator: `a |*| b == c => a(c, b(c))`.
     *
     * Example Usage:
     * {{{
     * val cos = math.cos _
     * val max = math.max(_: Double, _: Double)
     * val cosSmax = max |*| cos // Returns the max of x and cos(x)!
     * // cosSmax(0.3) === max(0.3, cos(0.3))
     * }}}
     */
    def |*|(b: C => B): C => A = c => a(c, b(c))
  }
}