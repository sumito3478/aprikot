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
package aprikot.http

import org.scalatest._

import aprikot.parsing._

class HttpHeaderParserSpec extends FunSpec {
  describe("HttpHeaderParser.apply") {
    it("should be able to parse HTTP ResponseMessage") {
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
      val ret = HttpHeaderParser(message.getBytes("UTF-8"))
      assert(ret.startLine.isInstanceOf[StatusLine] === true)
    }
    it("should be able to parse HTTP RequestMessage") {
      val message = """GET /tutorials/other/top-20-mysql-best-practices/ HTTP/1.1
Host: net.tutsplus.com  
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)  
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8  
Accept-Language: en-us,en;q=0.5  
Accept-Encoding: gzip,deflate  
Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7  
Keep-Alive: 300  
Connection: keep-alive  
Cookie: PHPSESSID=r2t5uvjq435r4q7ib3vtdjq120  
Pragma: no-cache  
Cache-Control: no-cache  
  

""".replace("\n", "\r\n")
      val ret = HttpHeaderParser(message.getBytes("UTF-8"))
      assert(ret.startLine.isInstanceOf[RequestLine] === true)
    }
  }
}