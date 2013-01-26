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

package info.sumito3478.aprikot.unsafe

import java.lang.System.gc
import java.lang.System.nanoTime

/**
 * A benchmark function.
 *
 * Example Usage:
 * {{{
 * import info.sumito3478.aprikot.io.benchmark
 *
 * // Execute the block for 10000 time and get the time consumed in micro
 * // seconds.
 * val time = benchmark {
 *   // do something...
 * }
 *
 * // Execute the block for 10 * 10000 times and get the time consumed in micro
 * // seconds.
 * val time10 = benchmark(10) {
 *   // do something...
 * }
 * }}}
 */
object benchmark {
  /**
   * Executes the given block for n * 10000 times and returns the time consumed
   * in micro seconds.
   */
  def apply(n: Int)(op: => Unit): Long = {
    0 to 10000 foreach {
      i =>
        op
    }
    var i = 0
    val limit = n * 10000
    gc
    val start = nanoTime
    while (i < limit) {
      op
      i += 1
    }
    (nanoTime - start) / 1000
  }

  /**
   * Executes the give block for 10000 times and return the time consumed in
   * micro seconds.
   */
  def apply(op: => Unit): Long = apply(1)(op)
}