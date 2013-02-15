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
package aprikot.text

import scala.collection.immutable
import scala.collection.mutable
import scala.collection.generic

class ByteString(
  underlined: String) extends IndexedSeq[Char] with immutable.StringLike[ByteString] {
  override def newBuilder = ByteString.newBuilder

  override def slice(from: Int, until: Int): ByteString = {
    val start = if (from < 0) 0 else from
    if (until <= start || length <= start) {
      return new ByteString("")
    } else {
      val end = if (length < until) length else until
      new ByteString(underlined.substring(start, end))
    }
  }

  override def length = underlined.length

  override def toString = underlined
}

object ByteString {
  def apply(x: Array[Byte], start: Int, length: Int): ByteString = {
    val builder = new StringBuilder
    builder ++ (start until (start + length) map {
      i =>
        val b = x(i)
        if (b < 0x80) {
          b.toChar
        } else {
          // Use 'Private Use Area' to represent bytes in range [0x80;0x100)
          (0xe000 - 0x80 + b).toChar
        }
    })
    new ByteString(builder.result)
  }

  implicit def canBuildFrom: generic.CanBuildFrom[ByteString, Char, ByteString] = {
    new generic.CanBuildFrom[ByteString, Char, ByteString] {
      def apply(from: ByteString) = newBuilder

      def apply() = newBuilder
    }
  }

  def newBuilder: mutable.Builder[Char, ByteString] = {
    StringBuilder.newBuilder mapResult (new ByteString(_))
  }
}