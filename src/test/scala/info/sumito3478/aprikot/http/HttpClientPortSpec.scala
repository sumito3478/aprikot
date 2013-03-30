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

class HttpClientPortSpec extends FunSpec {
  describe("") {
    it("") {
      val client = HttpClientPort("maven.sumito3478.info", 80)
      client.writeHeader(new HttpRequestHeader(new RequestLine("GET", new java.net.URI("/"), new HttpVersion(1, 1)),
        MessageHeaderMap(new MessageHeader("Content-Length", "0".getBytes("UTF-8")), new MessageHeader("Host", "mave.sumito3478.info".getBytes("UTF-8")))))
      println(client.read(10240).map(_.utf8String))
    }
  }
}