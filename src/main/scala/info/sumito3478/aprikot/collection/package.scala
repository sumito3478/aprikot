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

  /**
   * Pimp-my-library implicit class that adds methods to
   * [[scala.collection.BufferedIterator]].
   */
  implicit class BufferedIteratorW[A](val underlined: BufferedIterator[A]) extends AnyVal {
    /**
     * Takes longest prefix of values produced by this iterator that satisfy
     * a predicate, that tests element and the next element if exists.
     *
     * @param p
     *   The predicate used to test elements.
     *
     * @return
     *   An iterator returning the values produced by this iterator,
     *   until this iterator produces a value that does not satisfy the
     *   predicate p.
     *
     * @note
     *   Reuse: After calling this method, one should discard the iterator it
     *   was called on, and use only the iterator that was returned. Using
     *   the old iterator is undefined, subject to change, and may result in
     *   changes to the new iterator as well.
     */
    def takeWhile(p: (A, Option[A]) => Boolean): BufferedIterator[A] = {
      bufferedTakeWhile(underlined, p)
    }
  }

  /**
   * Pimp-my-library implicit class that adds methods to
   * [[scala.collection.TraversableOnce]].
   */
  implicit class TraversableOnceW[A](
    val underlined: TraversableOnce[A]) extends AnyVal {
    /**
     * Appends all elements of this traversable to a builder using start, end,
     * and separator traversables. The written elements begins with the
     * traversable `start` and ends with the traversable `end`. Inside,
     * all elements of this traversable are separated by the traversable `sep`.
     *
     * @param builder
     *   the builder to which elements are appended
     * @param start
     *   the starting traversable
     * @param sep
     *   the separator traversable
     * @param end
     *   the ending traversable
     *
     * @see [[GenTraversableOnce#addString]]
     */
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

    /**
     * Appends all elements of this traversable to a builder using the specified
     * separator traversable. The written elements of this traversable are
     * separated by the traversable `sep`.
     *
     * @param builder
     *   the builder to which elements are appended
     * @param sep
     *   the separator traversable
     *
     * @see [[GenTraversableOnce#addString]]
     */
    def addTraversableOnce[B, C](
      builder: Builder[B, C],
      sep: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): Unit = {
      addTraversableOnce(builder, List(), sep, List())(asTraversable)
    }

    /**
     * Appends all elements of this traversable to a builder.
     *
     * @param builder
     *   the builder to which elements are appended
     *
     * @see [[GenTraversableOnce#addString]]
     */
    def addTraversableOnce[B, C](
      builder: Builder[B, C])(
        implicit asTraversable: (A) => TraversableOnce[B]): Unit = {
      addTraversableOnce[B, C](builder, List(), List(), List())(asTraversable)
    }

    /**
     * Produces a collection with all elements of this traversalbe using
     * the specified builder and start, end, separator traversables.
     *
     * @param start
     *   the starting traversable
     * @param sep
     *   the separator traversable
     * @param end
     *   the ending traversable
     *
     * @return
     *   A collection that contains all elements of this traversable.
     *   The resulting collection begins with the elements of the traversable
     *   `start` and ends with the traversable `end`. Inside, all elements
     *   of this traversable are separated by the traversable sep.
     *
     * @note
     *   This is a generic version of
     *   [[scala.collection.GenTraversableOnce#mkString]].
     */
    def mkBuildable[B, C](
      builder: Builder[B, C],
      start: TraversableOnce[B],
      sep: TraversableOnce[B],
      end: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      addTraversableOnce(builder, start, sep, end)
      builder.result
    }

    /**
     * Produces a collection with all elements of this traversalbe using
     * the specified builder and the separator traversables.
     *
     * @param sep
     *   the separator traversable
     *
     * @return
     *   A collection that contains all elements of this traversable.
     *   Inside, All elements of this traversable are separated by the
     *   traversable sep.
     *
     * @note
     *   This is a generic version of
     *   [[scala.collection.GenTraversableOnce#mkString]].
     */
    def mkBuildable[B, C](
      builder: Builder[B, C],
      sep: TraversableOnce[B])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      mkBuildable(builder, List(), sep, List())(asTraversable)
    }

    /**
     * Produces a collection with all elements of this traversalbe using
     * the specified builder.
     *
     * @return
     *   A collection that contains all elements of this traversable.
     *
     * @note
     *   This is a generic version of
     *   [[scala.collection.GenTraversableOnce#mkString]].
     */
    def mkBuildable[B, C](
      builder: Builder[B, C])(
        implicit asTraversable: (A) => TraversableOnce[B]): C = {
      mkBuildable[B, C](builder, List(), List(), List())(asTraversable)
    }
  }

  implicit class IteratorW[A](val underlined: Iterator[A]) extends AnyVal {
  }
}