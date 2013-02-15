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

import scala.util.continuations._

import java.nio._

import aprikot.control._
import aprikot.io._
import aprikot.parsing._

trait HttpServerContext extends TCPContext {
  def writeHeader(
    buffer: ByteBuffer,
    response: HttpResponseHeader, continuation: Int => Unit): Unit = {
    buffer.clear
    buffer.put(response.toBytes.array)
    write(buffer, continuation)
  }

  def writeHeader(
    buffer: ByteBuffer, response: HttpResponseHeader): Int @suspendable =
    callCC(writeHeader(buffer, response, _))

  def readHeader(
    buffer: ByteBuffer, continuation: (HttpRequestHeader, ByteBuffer) => Unit): Unit = {
    read(buffer, b => {
      buffer.flip
      continuation.tupled(HttpRequestHeaderParser(buffer))
    })
  }

  def readHeader(
    buffer: ByteBuffer): (HttpRequestHeader, ByteBuffer) @suspendable =
    callCC(k => readHeader(buffer, (_1, _2) => k(_1, _2)))
}

object HttpServerContext {
  def apply(ctx: TCPContext): HttpServerContext = {
    new HttpServerContext {
      def channel = ctx.channel
    }
  }
}