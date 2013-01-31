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

import java.net.URI
import scala.collection.mutable.WrappedArray

/**
 * A class that represents the start line of HTTP request message.
 */
class RequestLine(
  val method: String,
  val uri: URI,
  val version: HttpVersion) extends StartLine {
  override def toString: String = {
    s"${method} ${uri} ${version}"
  }
}