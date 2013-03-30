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
package aprikot.twitter.crawler.mongodb

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util._

import akka.actor._

import com.mongodb.casbah._
import com.mongodb.casbah.commons._
import com.mongodb.casbah.commons.Implicits._

import aprikot.mongodb._
import aprikot.twitter._

class UserStreamCrawler extends Actor {
  import UserStreamCrawler._

  def receive = {
    case Start => {
      future {
        val stream = UserStreams(OAuthConfig.default.get)
        val it = stream.iterator
        val mongo = MongoConnection()("aprikot_twitter")("userstreams")
        it foreach {
          line =>
            val obj = JSON.parse(line)
            obj += "aprikot_twitter" -> (MongoDBObject("captured_at" -> new java.util.Date()))
            mongo += obj
        }
      } onComplete {
        case Success(v) => self ! Complete
        case Failure(e) => self ! Error(e)
      }
    }
    case Complete => {
      println("complete!")
    }
    case Error(e) => {
      self ! Kill
      throw new RuntimeException(e)
    }
  }
}

object UserStreamCrawler {
  sealed trait Command

  case object Start

  case object Complete

  case class Error(e: Throwable)
}