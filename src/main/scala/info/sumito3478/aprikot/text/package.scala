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

import java.text._
import info.sumito3478.aprikot.collection.IteratorW
import scala.collection.mutable.Queue
import scala.collection.immutable.VectorBuilder

package object text {
  implicit class BreakIteratorW(val self: BreakIterator) extends AnyVal {
    def aheadSize = 0x1000

    def readAll(xs: String): IndexedSeq[String] = {
      self.setText(xs)
      val first = self.first
      val builder = new VectorBuilder[Int]
      builder ++= (Iterator.continually(self.next).
        takeWhile(_ != BreakIterator.DONE))
      val retBuilder = new VectorBuilder[String]
      builder.result.foldLeft(first)((first, end) => {
        retBuilder += xs.substring(first, end)
        end
      })
      retBuilder.result
    }

    def readToSecondLast(xs: String): (IndexedSeq[String], Int) = {
      self.setText(xs)
      val first = self.first
      val last = self.last
      val secondLast = self.previous
      if (secondLast == first) {
        throw new NoSuchElementException
      }
      (readAll(xs.substring(0, secondLast)), secondLast)
    }

    def mapIterator(xs: Iterator[Char]): Iterator[String] = {
      val ahead = xs.lookAhead
      val queue = new Queue[String]
      Iterator.continually[Option[String]](
        if (queue.isEmpty && ahead.hasNext) {
          val aheadSize = 0x1000
          val buf = ahead.lookAhead(aheadSize)
          queue.enqueue((
            if (buf.length < aheadSize) {
              readAll((new StringBuilder ++= ahead.forceTake(aheadSize)).result)
            } else {
              val (ret, read) =
                readToSecondLast((new StringBuilder ++= buf).result)
              ahead.forceDrop(read)
              ret
            }): _*)
          Some(queue.dequeue)
        } else if (queue.isEmpty) {
          None
        } else {
          Some(queue.dequeue)
        }).takeWhile(_.isDefined).map(_.get)
    }
  }
}