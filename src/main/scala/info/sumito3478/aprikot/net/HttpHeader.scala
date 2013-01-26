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
package info.sumito3478.aprikot.net

import scala.collection.immutable.Map
import scala.collection.immutable.TreeMap
import info.sumito3478.aprikot.text.NeutralIgnoreCaseOrder
import java.nio.ByteBuffer
import scala.collection.mutable.ListBuffer
import info.sumito3478.aprikot.collection.BufferedIteratorW

/**
 * This class represents HTTP Header.
 */
class HttpHeader private (
  underlined: TreeMap[String, String]) extends Map[String, String] {
  /**
   * Converts this instance of HttpHeader to a string.
   */
  override def toString: String = {
    (underlined map {
      set: (String, String) =>
        val (k, v) = set
        s"$k: $v\r\n"
    }).mkString
  }

  def +[B1 >: String](kv: (String, B1)): Map[String, B1] = {
    underlined + kv
  }

  def -(key: String): Map[String, String] = {
    underlined - key
  }

  def get(key: String): Option[String] = {
    underlined get key
  }

  override def iterator: Iterator[(String, String)] = {
    underlined.iterator
  }

  override def foreach[U](f: ((String, String)) => U): Unit = {
    underlined foreach f
  }

  override def size: Int = {
    underlined.size
  }
}

object HttpHeader {
  def apply(elems: (String, String)*): HttpHeader = {
    new HttpHeader(TreeMap(elems: _*)(NeutralIgnoreCaseOrder))
  }

  def apply(buffer: Array[Byte], start: Int, length: Int): HttpHeader = {
    val builer = new ListBuffer[(String, String)]
    val startLine = new String(buffer, start, length, "UTF-8")
    apply(builer: _*)
  }
}