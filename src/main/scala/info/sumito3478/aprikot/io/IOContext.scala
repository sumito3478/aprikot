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

import info.sumito3478.aprikot.unsafe.Memory
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel

import scala.util.continuations._
import info.sumito3478.aprikot.control.callCC

trait IOContext {
  def read(buffer: ByteBuffer, continuation: Int => Unit): Unit

  def read(buffer: ByteBuffer): Int @suspendable = callCC(read(buffer, _))

  def write(buffer: ByteBuffer, continuation: Int => Unit): Unit

  def write(buffer: ByteBuffer): Int @suspendable = callCC(write(buffer, _))
}