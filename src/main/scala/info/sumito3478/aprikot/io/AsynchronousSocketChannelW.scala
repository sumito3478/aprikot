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
package info.sumito3478.aprikot.io

import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.CompletionHandler
import java.nio.ByteBuffer
import info.sumito3478.aprikot.time.Duration
import java.util.concurrent.TimeUnit
import java.lang.{ Integer => JInteger }
import java.nio.channels.AsynchronousSocketChannel

class AsynchronousSocketChannelW(
  val underlined: AsynchronousSocketChannel) extends AnyVal {
  import AsynchronousSocketChannelW._

  def read(dst: ByteBuffer, timeout: Duration)(f: Int => Unit): Unit = {
    underlined.read[Unit](
      dst, timeout.toNanos, TimeUnit.NANOSECONDS, (), completionHandler(f))
  }

  def write(src: ByteBuffer, timeout: Duration)(f: Int => Unit): Unit = {
    underlined.write[Unit](
      src, timeout.toNanos, TimeUnit.NANOSECONDS, (), completionHandler(f))
  }
}

object AsynchronousSocketChannelW {
  private def completionHandler(
    f: Int => Unit): CompletionHandler[JInteger, Unit] = {
    new CompletionHandler[JInteger, Unit] {
      def completed(result: JInteger, attachment: Unit) = {
        f(result)
      }

      def failed(exc: Throwable, attachment: Unit) = {
        throw new RuntimeException(exc)
      }
    }
  }

  class Dummy0

  implicit object Dummy0 extends Dummy0

  implicit def apply(x: AsynchronousSocketChannel): AsynchronousSocketChannelW = {
    new AsynchronousSocketChannelW(x)
  }

  implicit def apply(x: AsynchronousSocketChannelW)(dummy: Dummy0): AsynchronousSocketChannel = {
    x.underlined
  }
}