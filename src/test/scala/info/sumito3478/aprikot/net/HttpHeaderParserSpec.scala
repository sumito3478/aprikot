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

import org.scalatest
import scalatest.FunSpec

import info.sumito3478.aprikot.parsing.IndexedSeqReader

class HttpHeaderParserSpec extends FunSpec {
  describe("HttpHeaderParser#genericMessage") {
    it("") {
      val message = """HTTP/1.1 200 OK
Transfer-Encoding: chunked  
Date: Sat, 28 Nov 2009 04:36:25 GMT  
Server: LiteSpeed  
Connection: close  
X-Powered-By: W3 Total Cache/0.8  
Pragma: public  
Expires: Sat, 28 Nov 2009 05:36:25 GMT  
Etag: "pub1259380237;gz"  
Cache-Control: max-age=3600, public  
Content-Type: text/html; charset=UTF-8  
Last-Modified: Sat, 28 Nov 2009 03:50:37 GMT  
X-Pingback: http://net.tutsplus.com/xmlrpc.php  
Content-Encoding: gzip  
Vary: Accept-Encoding, Cookie, User-Agent  
  

""".replace("\n", "\r\n")
      val ret = HttpHeaderParser.genericMessage(
        new IndexedSeqReader(message.getBytes("UTF-8")))
      ret match {
        case e: HttpHeaderParser.Failure => println(e.msg)
        case e: HttpHeaderParser.Error => println(e.msg)
        case r: HttpHeaderParser.Success[_] => println(r.result)
      }
      HttpHeaderParser.StatusLine(
        new IndexedSeqReader("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"))) match {
          case e: HttpHeaderParser.Failure => println(e.msg)
          case e: HttpHeaderParser.Error => println(e.msg)
          case r: HttpHeaderParser.Success[_] => println(r.result)
        }
    }
  }
}