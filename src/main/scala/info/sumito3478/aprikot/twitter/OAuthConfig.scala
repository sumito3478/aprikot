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
    val consumerToken = Option(getProperty(
      "org.sumito3478.aprikot.twitter.consumer.token.default",
      getenv("TWITTER_CONSUMER_TOKEN_DEFAULT")))
    val consumerSecret = Option(getProperty(
      "org.sumito3478.aprikot.twitter.consumer.secret.default",
      getenv("TWITTER_CONSUMER_SECRET_DEFAULT")))
    consumerToken match {
      case Some(token) =>
        consumerSecret match {
          case Some(secret) => Some(Consumer(token, secret))
          case _ => None
        }
      case _ => None
    }
  }

  def defaultToken = {
    import System._
    val token = Option(getProperty(
      "org.sumito3478.aprikot.twitter.token.default",
      getenv("TWITTER_TOKEN_DEFAULT")))
    val tokenSecret = Option(getProperty(
      "org.sumito3478.aprikot.twitter.token.secret.default",
      getenv("TWITTER_TOKEN_SECRET_DEFAULT")))
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
    defaultConsumer match {
      case Some(consumer) =>
        defaultToken match {
          case Some(token) => OAuthConfig(consumer, token)
          case _ => None
        }
      case _ => None
    }
  }
}