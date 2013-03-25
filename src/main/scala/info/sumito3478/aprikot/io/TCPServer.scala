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

import java.net._
import java.nio.channels._
import java.util.concurrent._

import aprikot.time._

trait TCPServer {
  def port: Int

  lazy val group = AsynchronousChannelGroup.withThreadPool(
    Executors.newCachedThreadPool)

  private[this] lazy val channel =
    AsynchronousServerSocketChannel.open(group).
      bind(new InetSocketAddress(port))

  def handle(ctx: TCPContext): Unit

  private[this] val loop: AsynchronousSocketChannel => Unit = {
    c: AsynchronousSocketChannel =>
      channel.accept(loop)
      handle(new TCPContext {
        def bufferSize = 0x2000

        def channel = c
      })
  }

  def start: Unit = {
    channel.accept(loop)
  }

  def shutdown(await: Duration): Unit = {
    group.shutdown
    group.awaitTermination(await.toNanos, TimeUnit.NANOSECONDS)
    group.shutdownNow
    channel.close
  }

  def shutdown: Unit = shutdown(Duration.seconds(0))
}