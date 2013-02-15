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
package aprikot.unmanaged

import java.lang.{ Double => JDouble }

/**
 * The base trait for endian tag objects.
 */
sealed trait ByteOrder

/**
 * The tag object for little endian.
 */
case object LittleEndian extends ByteOrder

/**
 * The tag object for big endian.
 */
case object BigEndian extends ByteOrder

/**
 * The tag object for little endian of ARM.
 *
 * In the little endian in ARM, the byte order of
 * double precision floating pooint value is different to
 * other hosts.
 *
 * @note Handling ARM-little-endian is not implemented well yet.
 */
case object ArmLittleEndian extends ByteOrder

object ByteOrder {
  /**
   * Detects if the host is a little-endian system, including ARM.
   */
  val isLE: Boolean = {
    (0xcafebabe >>> 16) == 0xcafe
  }

  /**
   * Detects if the host is a big-endian system.
   */
  val isBE: Boolean = {
    !isLE
  }

  /**
   * Detects if the host is a little-enmdian ARM system.
   * Not implemented well yet.
   */
  val isArmLE: Boolean = {
    // TODO: I'm not sure this is really correct...
    JDouble.doubleToLongBits(1.0) == 0x3ff00000L
  }

  /**
   * Returns an instance of ByteOrder that represents endianness of
   * current host.
   *
   * @note ARM is not supported well yet.
   */
  val native: ByteOrder = {
    if (isArmLE) {
      ArmLittleEndian
    } else if (isLE) {
      LittleEndian
    } else {
      BigEndian
    }
  }
}