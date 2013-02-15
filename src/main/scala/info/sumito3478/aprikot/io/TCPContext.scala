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

import scala.util.continuations._

import java.net._
import java.nio._
import java.nio.channels._

import aprikot.control._
import aprikot.time._

trait TCPContext extends IOContext {
  def channel: AsynchronousSocketChannel

  def read(buffer: ByteBuffer, continuation: Int => Unit): Unit = {
    buffer.clear
    println("try starting read operation!")
    channel.read(buffer, Duration.seconds(40))(continuation)
  }

  def write(buffer: ByteBuffer, continuation: Int => Unit): Unit = {
    buffer.flip
    channel.write(buffer, Duration.seconds(40))(continuation)
  }

  def close: Unit = {
    channel.close
  }
}

object TCPContext {
  def apply(host: String, port: Int, group: AsynchronousChannelGroup, continuation: TCPContext => Unit): Unit = {
    val c = AsynchronousSocketChannel.open
    c.connect(new InetSocketAddress(host, port), {
      continuation(new TCPContext {
        def channel = c
      })
    })
  }

  def apply(host: String, port: Int, group: AsynchronousChannelGroup): TCPContext @suspendable =
    callCC(apply(host, port, group, _))
}