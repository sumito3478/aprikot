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
package aprikot.http

import info.sumito3478.aprikot.io._
import java.net.Socket

import akka.util._

trait HttpClientPort extends SocketPort {
  def writeHeader(header: HttpRequestHeader): Unit = {
    write(ByteString(header.toBytes.array))
  }

  def readHeader(n: Int): (HttpResponseHeader, ByteString) = {
    val bytes = read(n).get
    HttpResponseHeaderParser(bytes)
  }

  def readChunk: ByteString = {
    Iterator.continually {
      val r = readUntil('\n')
      if (r.isEmpty) {
        None
      } else {
        val s = r.get.utf8String
        val num = java.lang.Integer.parseInt(s.slice(0, s.size - 2), 16)
        val data = read(num)
        read(2)
        data
      }
    }.takeWhile(_.isDefined).map(_.get).foldLeft(ByteString.empty) {
      (acc, elem) =>
        acc ++ elem
    }
  }
}

object HttpClientPort {
  def apply(host: String, port: Int): HttpClientPort = {
    val socket = new Socket(host, port)
    new HttpClientPort {
      def underlined = socket
    }
  }
}