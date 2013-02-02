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

import info.sumito3478.aprikot.collection.ToBytesable
import scala.collection.immutable.VectorBuilder

class MessageHeader(
  val fieldName: String, val fieldValue: Array[Byte]) extends ToBytesable {
  override def toString: String = {
    s"""${fieldName}: ${new String(fieldValue, "UTF-8")}"""
  }

  override def toBytes: Vector[Byte] = {
    val builder = new VectorBuilder[Byte]
    builder ++= s"${fieldName}: ".getBytes("UTF-8")
    builder ++= fieldValue
    builder.result
  }
}