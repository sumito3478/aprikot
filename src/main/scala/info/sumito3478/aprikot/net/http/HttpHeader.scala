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

package info.sumito3478.aprikot.net.http

import info.sumito3478.aprikot.collection.ToBytesable
import info.sumito3478.aprikot.collection.TraversableOnceW
import scala.collection.immutable.VectorBuilder

trait HttpHeader extends ToBytesable {
  import HttpHeader._

  val startLine: StartLine

  val fields: List[MessageHeader]

  override def toString: String = {
    startLine.toString + "\r\n" + fields.mkString("\r\n") + "\r\n\r\n"
  }

  override def toBytes: Vector[Byte] = {
    val builder = new VectorBuilder[Byte]
    builder ++= startLine.toBytes
    builder ++= sep
    fields.map(_.toBytes).addTraversableOnce(builder, sep)
    builder ++= sep ++= sep
    builder.result
  }
}

object HttpHeader {
  private val sep = List[Byte]('\r', '\n')

  def apply(startLine: StartLine, fields: List[MessageHeader]): HttpHeader = {
    startLine match {
      case r: RequestLine => new HttpRequestHeader(r, fields)
      case s: StatusLine => new HttpResponseHeader(s, fields)
      case _ => sys.error("Unknown kind of HTTP StartLine")
    }
  }
}