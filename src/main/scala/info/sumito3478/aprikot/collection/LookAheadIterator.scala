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

class LookAheadIterator[+A](
  val underlined: Iterator[A]) extends BufferedIterator[A] {
  private[this] val buffer = new mutable.Queue[A]

  def lookAhead(n: Int): IndexedSeq[A] = {
    val d = n - buffer.length
    if (d > 0) {
      buffer.enqueue(underlined.forceTake(d): _*)
    }
    (new immutable.VectorBuilder[A] ++= buffer.slice(0, n)).result
  }

  def hasNext: Boolean = {
    lookAhead(1).length > 0
  }

  def next: A = {
    if (buffer.isEmpty) {
      underlined.next
    } else {
      buffer.dequeue
    }
  }

  def head: A = {
    lookAhead(1).head
  }
}