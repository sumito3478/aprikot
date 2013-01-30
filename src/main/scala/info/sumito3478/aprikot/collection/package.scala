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

import scala.collection.immutable.VectorBuilder
import scala.collection.TraversableOnce
import scala.collection.mutable.Builder
import scala.reflect.ClassTag
import scala.collection.mutable.ArrayBuilder

package object collection {
  private def bufferedTakeWhile[A](underlined: BufferedIterator[A], p: (A, Option[A]) => Boolean): BufferedIterator[A] = {
    var h: Option[A] = None
    var self = underlined
    new Iterator[A] {
      def hasNext = {
        h.isDefined || self.hasNext && {
          val next = self.next
          val head = if (self.hasNext) Some(self.head) else None
          if (p(next, head)) {
            h = Some(next)
          } else {
            self = Iterator.empty.buffered
          }
          h.isDefined
        }
      }

      def next = {
        if (hasNext) {
          val ret = h.get
          h = None
          ret
        } else {
          self.next
        }
      }
    }.buffered
  }

  implicit class BufferedIteratorW[A](val underlined: BufferedIterator[A]) extends AnyVal {
    def takeWhile(p: (A, Option[A]) => Boolean): BufferedIterator[A] = {
      bufferedTakeWhile(underlined, p)
    }
  }

  implicit class TraversableOnceW[A](
    val underlined: TraversableOnce[A]) extends AnyVal {
    def addTraversableOnce[B, C](
      builder: Builder[B, C],
      start: TraversableOnce[B],
      sep: TraversableOnce[B],
      end: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): Unit = {
      builder ++= start
      var first = true
      underlined foreach {
        a =>
          if (!first) {
            builder ++= sep
          } else {
            first = false
          }
          builder ++= asTraversable(a)
      }
      builder ++= end
    }

    def mkBuildable[B, C](
      builder: Builder[B, C],
      start: TraversableOnce[B],
      sep: TraversableOnce[B],
      end: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      addTraversableOnce(builder, start, sep, end)
      builder.result
    }

    def mkBuildable[B, C](
      builder: Builder[B, C],
      sep: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      mkBuildable(builder, List(), sep, List())(asTraversable)
    }

    def mkBuildable[B, C](
      builder: Builder[B, C])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      mkBuildable[B, C](builder, List(), List(), List())(asTraversable)
    }
  }
}