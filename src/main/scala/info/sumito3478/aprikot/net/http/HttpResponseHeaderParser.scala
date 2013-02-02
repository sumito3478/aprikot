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

import java.nio.ByteBuffer
import info.sumito3478.aprikot.parsing.ByteBufferReader
import java.nio.charset.Charset

object HttpResponseHeaderParser extends HttpHeaderParser {
  val requestMessage = StatusLine ~ messageHeader.+ ~ CRLF ^^ {
    case s ~ m ~ _ => new HttpResponseHeader(s, MessageHeaderMap(m:_*))
  }

  def apply(input: ByteBuffer): (HttpResponseHeader, ByteBuffer) = {
    //println(Charset.forName("UTF-8").decode(input))
    responseMessage(new ByteBufferReader(input, 0)) match {
        case e: Failure => sys.error(f"ParseError at ${e.next.pos.column}: ${e.msg}")
        case e: Error => sys.error(f"ParseError: ${e.msg}")
        case r: Success[_] => {
          input.position(r.next.offset)
          val slice = input.slice
          slice.position(slice.limit)
          println(f"response slice: ${slice.position}, ${slice.limit}")
          (r.result, slice)
        }
        case _ => sys.error("Unknown Parser Error")
      }
  }
}