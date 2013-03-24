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
package aprikot

import java.lang.{ Integer => JInteger }
import java.net._
import java.nio._
import java.nio.channels._
import java.util.concurrent._

import aprikot.time._

package object io {
  class Dummy0

  implicit object dummy0 extends Dummy0

  private[this] def completionHandler(
    f: AsynchronousSocketChannel => Unit): CompletionHandler[AsynchronousSocketChannel, Unit] = {
    new CompletionHandler[AsynchronousSocketChannel, Unit] {
      def completed(result: AsynchronousSocketChannel, attachment: Unit) = {
        f(result)
      }

      def failed(exc: Throwable, attachment: Unit) = exc match {
        case exc: AsynchronousCloseException => ()
        case _ => throw new RuntimeException(exc)
      }
    }
  }

  implicit class AsynchronousServerSocketChannelW(
    val underlined: AsynchronousServerSocketChannel) extends AnyVal {

    def accept(f: AsynchronousSocketChannel => Unit): Unit = {
      underlined.accept((), completionHandler(f))
    }
  }

  private[this] def completionHandler[A](
    f: A => Unit)(
      implicit dummy: Dummy0): CompletionHandler[A, Unit] = {
    new CompletionHandler[A, Unit] {
      def completed(result: A, attachment: Unit) = {
        f(result)
      }

      def failed(exc: Throwable, attachment: Unit) = {
        throw new RuntimeException(exc)
      }
    }
  }

  implicit class AsynchronousSocketChannelW(
    val underlined: AsynchronousSocketChannel) extends AnyVal {
    def read(dst: ByteBuffer, timeout: Duration)(f: Int => Unit): Unit = {
      underlined.read[Unit](
        dst, timeout.toNanos, TimeUnit.NANOSECONDS, (),
        completionHandler[JInteger](f(_)))
    }

    def write(src: ByteBuffer, timeout: Duration)(f: Int => Unit): Unit = {
      underlined.write[Unit](
        src, timeout.toNanos, TimeUnit.NANOSECONDS, (),
        completionHandler[JInteger](f(_)))
    }

    def connect(remote: SocketAddress, f: => Unit): Unit = {
      underlined.connect[Unit](remote, (), completionHandler[Void](_ => f))
    }
  }
}