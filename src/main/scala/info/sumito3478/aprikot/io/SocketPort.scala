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
package aprikot.io

import java.net.Socket
import org.apache.commons.io.IOUtils
import akka.util.ByteString
import akka.util.ByteStringBuilder
import aprikot.unmanaged.Disposable
import java.io.IOException
import org.slf4j._
import java.io._
import scala.annotation.tailrec

/**
 * A bi-directional port underlined with java.net.Socket.
 */
trait SocketPort extends BidirectionalPort with Disposable {
  /* protected[this]? */ def underlined: Socket

  def read(n: Int): Option[ByteString] = {
    val buffer = new Array[Byte](n)
    val in = underlined.getInputStream
    val read = in.read(buffer, 0, n)
    if (read == -1) {
      None
    } else {
      Some(ByteString.fromArray(buffer, 0, read))
    }
  }

  def write(data: ByteString): Unit = {
    val buffer = data.toArray[Byte]
    val out = underlined.getOutputStream
    val written = out.write(buffer)
  }

  def disposeInternal: Unit = {
    if (!underlined.isClosed) {
      try {
        underlined.close
      } catch {
        case e: IOException => {
          LoggerFactory.getLogger(this.getClass).error(
            s"""${e.getMessage}""")
        }
      }
    }
  }
}

object SocketPort {
  def apply(host: String, port: Int): SocketPort = {
    val socket = new Socket(host, port)
    new SocketPort {
      def underlined = socket

      override def finalize: Unit = dispose
    }
  }
}