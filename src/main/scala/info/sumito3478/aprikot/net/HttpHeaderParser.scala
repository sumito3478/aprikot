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
package info.sumito3478.aprikot.net

import info.sumito3478.aprikot.text.ByteString

trait HttpHeaderParser {
  val extracter = """(?:\r?\n)*(.*)\r?\n\r?\n([\S\s]*)""".r

  def extract(buffer: Vector[Byte]): Vector[Byte] = {
    val start = buffer.iterator.takeWhile(b => b == '\r' || b == '\n').length
    buffer.indexOfSlice("\r\n\r\n", start)
    var end = 0
    buffer.slice(start, end)
  }
}