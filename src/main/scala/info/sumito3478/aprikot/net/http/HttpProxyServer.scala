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

import scala.util.continuations._
import java.nio.ByteBuffer
import info.sumito3478.aprikot.io.TCPContext
import java.util.concurrent.atomic.AtomicBoolean
import java.net.URI

trait HttpProxyServer extends HttpServer {
  private[this] def loop1(ctx: TCPContext, server: TCPContext, buffer: ByteBuffer, uri: URI): Unit @suspendable = {
    val b = ctx.read(buffer)
    if (b > 0) {
      server.write(buffer)
      loop1(ctx, server, buffer, uri)
    }
  }
  private[this] def loop2(ctx: TCPContext, server: TCPContext, buffer: ByteBuffer, uri: URI, length: Int): Unit @suspendable = {
    val b = server.read(buffer)
    if (b > 0 && (length - b) > 0) {
      ctx.write(buffer)
      loop2(ctx, server, buffer, uri, length - b)
    }
  }

  def handle(ctx: HttpServerContext): Unit = {
    reset[Unit, Unit] {
      val buffer = ByteBuffer.allocateDirect(0x5000)
      val (request, remaining) = ctx.readHeader(buffer)
      val uri = request.startLine.uri
      require(uri.isAbsolute, "Non absolute URI request to HTTP Proxy.")
      val fields = request.fields - "Connection"
      val request2 = new HttpRequestHeader(
        new RequestLine(request.startLine.method, request.startLine.uri, new HttpVersion(1, 1)), fields)
      val port = uri.getPort
      val server = HttpClientContext(TCPContext(uri.getHost, if (port > 0) port else 80, group))
      val requestBuffer = ByteBuffer.allocate(request2.toBytes.length)
      server.writeHeader(requestBuffer, request2)
      server.write(remaining)
      val (response, resRemaining) = server.readHeader(buffer)
      val responseBuffer = ByteBuffer.allocate(response.toBytes.length)
      val contentLength = response.fields.get("Content-Length")
      val remainingLength = resRemaining.remaining
      ctx.writeHeader(responseBuffer, response)
      ctx.write(resRemaining)
      if (contentLength.isDefined) {
        var length = new String(response.fields("Content-Length").fieldValue, "UTF-8").toInt
        length -= remainingLength
        loop2(ctx, server, buffer, uri, length)
        ctx.close
        server.close
      } else {
        loop1(server, ctx, buffer, uri)
        ctx.close
        server.close
      }
    }
  }
}