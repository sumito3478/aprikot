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

import java.nio.channels.AsynchronousServerSocketChannel
import info.sumito3478.aprikot.io.AsynchronousServerSocketChannelW
import info.sumito3478.aprikot.io.AsynchronousSocketChannelW
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel
import java.nio.ByteBuffer
import info.sumito3478.aprikot.unsafe.Memory
import info.sumito3478.aprikot.time.Duration
import java.nio.channels.AsynchronousChannelGroup
import java.util.concurrent.Executors

trait HttpServer {
  def port: Int

  protected[this] def handle(s: AsynchronousSocketChannel)

  private[this] lazy val buffer = Memory(0x2000)

  private[this] lazy val byteBuffer = buffer.pointer.byteBuffer(0x2000)

  private[this] lazy val group =
    AsynchronousChannelGroup.withCachedThreadPool(
      Executors.newCachedThreadPool, 0)

  private[this] lazy val listener =
    AsynchronousServerSocketChannel.open(group).
      bind(new InetSocketAddress(port))

  private[this] val callback: AsynchronousSocketChannel => Unit = {
    s: AsynchronousSocketChannel =>
      listener.accept(callback)
      s.read(byteBuffer, Duration.seconds(40)) {
        b =>
          byteBuffer.flip
          val header = HttpHeaderParser(byteBuffer.array)
          println(header)
      }
  }

  def start: Unit = {
    listener.accept(callback)
  }

  def stop: Unit = {
    group.shutdown
  }
}