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
package aprikot.twitter

import scala.util.continuations._
import org.scribe.builder._
import org.scribe.builder.api._
import org.scribe.model._
import org.scribe.oauth._
import java.io._
import aprikot.control._
import scala.annotation.tailrec

trait Streaming extends Endpoint {
  private[this] def connect: BufferedReader = {
    val token = new org.scribe.model.Token(oauthConfig.token.key, oauthConfig.token.secret)
    val verb = method match {
      case GET => Verb.GET
      case POST => Verb.POST
    }
    val req = new OAuthRequest(verb, url)
    underlined.signRequest(token, req)
    val res = req.send
    val stream = res.getStream
    new BufferedReader(new InputStreamReader(stream, "UTF-8"))
  }

  def line(continuation: String => Unit): Unit = {
    val reader = connect
    @tailrec
    def loop: Unit @generator.susp[String] = {
      val l = reader.readLine
      if (l != null) {
        continuation(l)
        loop
      }
    }
  }

  def line: String @suspendable = callCC(line(_))

  def iterator: Iterator[String] = {
    generator[String] {
      yields =>
        val reader = connect
        def loop: Unit @generator.susp[String] = {
          val l = reader.readLine
          if (l != null) {
            yields(l)
            loop
          }
        }
    }
  }
}