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

package info.sumito3478.aprikot.http

import info.sumito3478.aprikot.io._
import scala.util.continuations._
import info.sumito3478.aprikot.control.callCC
import java.nio.ByteBuffer
import info.sumito3478.aprikot.parsing.ByteBufferReader

import scala.collection.mutable.WrappedArray

trait HttpClientContext extends TCPContext {
  def writeHeader(
    buffer: ByteBuffer,
    response: HttpRequestHeader, continuation: Int => Unit): Unit = {
    buffer.clear()
    buffer.put(response.toBytes.array)
    write(buffer, continuation)
  }

  def writeHeader(
    buffer: ByteBuffer, response: HttpRequestHeader): Int @suspendable =
    callCC(writeHeader(buffer, response, _))

  def readHeader(
    buffer: ByteBuffer, continuation: (HttpResponseHeader, ByteBuffer) => Unit): Unit = {
    read(buffer, b => {
      buffer.flip
      continuation.tupled(HttpResponseHeaderParser(buffer))
    })
  }

  def readHeader(
    buffer: ByteBuffer): (HttpResponseHeader, ByteBuffer) @suspendable =
    callCC(k => readHeader(buffer, (_1, _2) => k(_1, _2)))
}

object HttpClientContext {
  def apply(ctx: TCPContext): HttpClientContext = {
    new HttpClientContext {
      def channel = ctx.channel
    }
  }
}