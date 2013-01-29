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

import scala.util.parsing.combinator.PackratParsers
import java.net.URI

object HttpHeaderParser extends PackratParsers {
  type Elem = Byte

  def isCTL(b: Byte) = b <= 31 || b == 127

  val NON_CTL = elem("NON_CTL", !isCTL(_))

  val NON_WS = elem("NON_WS", b => !isCTL(b) && b != ' ' && b != '\t')

  def isDIGIT(b: Byte) = '0' <= b && b <= '9'

  val DIGIT = elem("DIGIT", isDIGIT(_))

  val NON_DIGIT = elem("NON_DIGIT", b => !isCTL(b) || !isDIGIT(b))

  val CRLF = elem('\r') ~ elem('\n')

  val SP = elem(' ')

  val HT = elem('\t')

  val LWS = CRLF.? ~ (SP | HT).+ ^^ (_ => " ")

  val FIELD_NAME_CHAR = elem("FIELD_NAME_CHAR", b => !isCTL(b) && b != ':')

  val fieldName: Parser[String] =
    FIELD_NAME_CHAR.+ ^^ (xs => new String(xs.toArray, "UTF-8"))

  val fieldContent: Parser[String] = NON_WS.+ ^^ {
    xs => new String(xs.toArray, "UTF-8")
  }

  val fieldValue: Parser[String] =
    (fieldContent | LWS).* ^^ {
      case xs =>
        xs.filter(_ != " ").mkString(" ")
    }

  val messageHeader: Parser[(String, String)] =
    fieldName ~ elem(':') ~ fieldValue.? ~ CRLF ^^ {
      case n ~ _ ~ v ~ _ => (n, v.getOrElse(""))
    }

  val HttpSlash = elem('H') ~ elem('T') ~ elem('T') ~ elem('P') ~ elem('/')

  val HttpVersion = HttpSlash ~
    DIGIT.+ ~ elem('.') ~ DIGIT.+ ^^ {
      case _ ~ major ~ _ ~ minor =>
        s"HTTP/${major.map(_.toChar.asDigit).mkString}.${minor.map(_.toChar.asDigit).mkString}"
    }

  val RequestURI = NON_WS.+ ^^ {
    xs =>
      val uri = new String(xs.toArray, "UTF-8")
      new URI(uri)
  }

  val Method = NON_WS.+

  val RequestLine = Method ~ SP ~ RequestURI ~ SP ~ HttpVersion ~ CRLF ^^ {
    case m ~ _ ~ uri ~ _ ~ v ~ _ => s"$m ${uri} $v"
  }

  val StatusCode = DIGIT ~ DIGIT ~ DIGIT ^^ {
    case a ~ b ~ c => a.toChar.asDigit * 100 + b.toChar.asDigit * 10 +
      c.toChar.asDigit
  }

  val ReasonPhrase = NON_WS.* ^^ {
    xs => new String(xs.toArray, "UTF-8")
  }

  val StatusLine = HttpVersion ~ SP ~ StatusCode ~ SP ~ ReasonPhrase ~ CRLF ^^ {
    case v ~ _ ~ c ~ _ ~ r ~ _ => s"$v $c $r"
  }

  //val startLine = RequestLine | StatusLine
  //
  //  val startLine: Parser[String] = NON_CTL.+ ~ CRLF ^^ {
  //    case s ~ _ => new String(s.toArray, "UTF-8")
  //  }

  val startLine = StatusLine | RequestLine

  val genericMessage = startLine ~ messageHeader.+ ~ CRLF ^^ {
    _.toString
  }
}