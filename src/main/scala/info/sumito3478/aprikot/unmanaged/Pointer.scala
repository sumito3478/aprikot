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

package info.sumito3478.aprikot.unmanaged

import java.nio.ByteBuffer

import scala.concurrent.util.Unsafe.{ instance => _unsafe }

import com.sun.jna.{ Pointer => JPointer }

/**
 * A class that represents a pointer of unmanaged memory.
 */
class Pointer(val p: Long) extends AnyVal {
  def +(n: Long): Pointer = {
    new Pointer(p + n)
  }

  def -(n: Long): Pointer = {
    new Pointer(p - n)
  }

  def byte: Byte = {
    _unsafe.getByte(p)
  }

  def byte_=(x: Byte): Unit = {
    _unsafe.putByte(p, x)
  }

  def short: Short = {
    _unsafe.getShort(p)
  }

  def short_=(x: Short): Unit = {
    _unsafe.putShort(p, x)
  }

  def int: Int = {
    _unsafe.getInt(p)
  }

  def int_=(x: Int): Unit = {
    _unsafe.putInt(p, x)
  }

  def long: Long = {
    _unsafe.getLong(p)
  }

  def long_=(x: Long): Unit = {
    _unsafe.putLong(p, x)
  }

  def float: Float = {
    _unsafe.getFloat(p)
  }

  def float_=(x: Float): Unit = {
    _unsafe.putFloat(p, x)
  }

  def double: Double = {
    _unsafe.getDouble(p)
  }

  def double_=(x: Double): Unit = {
    _unsafe.putDouble(p, x)
  }

  def shortLE: Short = {
    short.fromLE
  }

  def shortLE_=(x: Short) = {
    short = x.toLE
  }

  def intLE: Int = {
    int.fromLE
  }

  def intLE_=(x: Int) = {
    int = x.toLE
  }

  def longLE: Long = {
    long.fromLE
  }

  def longLE_=(x: Long) = {
    long = x.toLE
  }

  def shortBE: Short = {
    short.fromBE
  }

  def shortBE_=(x: Short) = {
    short = x.toBE
  }

  def intBE: Int = {
    int.fromBE
  }

  def intBE_=(x: Int) = {
    int = x.toBE
  }

  def longBE: Long = {
    long.fromBE
  }

  def longBE_=(x: Long) = {
    long = x.toBE
  }

  def memcpy(dest: Pointer, len: Long): Unit = {
    _unsafe.copyMemory(p, dest.p, len)
  }

  def memcpy(dest: Array[Byte], len: Long): Unit = {
    var i = 0
    var cp = p
    while (i < len) {
      dest(i) = _unsafe.getByte(cp)
      i += 1
      cp += 1
    }
  }

  def memcpy(dest: Array[Short], len: Long): Unit = {
    var i = 0
    var cp = p
    while (i < len) {
      dest(i) = _unsafe.getShort(cp)
      i += 1
      cp += 2
    }
  }

  def memcpy(dest: Array[Int], len: Long): Unit = {
    var i = 0
    var cp = p
    while (i < len) {
      dest(i) = _unsafe.getInt(cp)
      i += 1
      cp += 4
    }
  }

  def memcpy(dest: Array[Long], len: Long): Unit = {
    var i = 0
    var cp = p
    while (i < len) {
      dest(i) = _unsafe.getLong(cp)
      i += 1
      cp += 8
    }
  }

  def pointer: Pointer = {
    new Pointer(_unsafe.getAddress(p))
  }

  def pointer_=(x: Long): Unit = {
    _unsafe.putAddress(p, x)
  }

  def byteBuffer(l: Long): ByteBuffer = {
    new JPointer(p).getByteBuffer(0, l)
  }

  def jna: JPointer = {
    new JPointer(p)
  }
}