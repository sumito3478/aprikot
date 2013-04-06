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

import org.scribe.builder._
import org.scribe.builder.api._
import org.scribe.model._
import org.scribe.oauth._

import java.io._

case class OAuthConfig(consumer: Consumer, token: Token)

object OAuthConfig {
  def defaultConsumer = {
    import System._
    val consumerKey = Option(getProperty(
      "org.sumito3478.aprikot.twitter.consumer.token.default",
      getenv("TWITTER_CONSUMER_KEY_DEFAULT")))
    val consumerSecret = Option(getProperty(
      "org.sumito3478.aprikot.twitter.consumer.secret.default",
      getenv("TWITTER_CONSUMER_SECRET_DEFAULT")))
    consumerKey match {
      case Some(key) =>
        consumerSecret match {
          case Some(secret) => Some(Consumer(key, secret))
          case _ => None
        }
      case _ => None
    }
  }

  def defaultToken = {
    import System._
    val token = Option(getProperty(
      "org.sumito3478.aprikot.twitter.access.token.default",
      getenv("TWITTER_ACCESS_TOKEN_DEFAULT")))
    val tokenSecret = Option(getProperty(
      "org.sumito3478.aprikot.twitter.access.token.secret.default",
      getenv("TWITTER_ACCESS_TOKEN_SECRET_DEFAULT")))
    token match {
      case Some(token) =>
        tokenSecret match {
          case Some(secret) => Some(Token(token, secret))
          case _ => None
        }
      case _ => None
    }
  }

  def default = {
    println(s"defaultComsumer: ${defaultConsumer}; defaultToken: ${defaultToken}")
    defaultConsumer match {
      case Some(consumer) =>
        defaultToken match {
          case Some(token) => Some(OAuthConfig(consumer, token))
          case _ => None
        }
      case _ => None
    }
  }
}