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
package info.sumito3478.aprikot.http

import scala.util.parsing.combinator.PackratParsers
import java.net.URI
import scala.collection.mutable.ArrayBuilder
import info.sumito3478.aprikot.parsing.IndexedSeqReader

trait HttpHeaderParser extends PackratParsers {
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

  val LWS: Parser[Option[Array[Byte]]] = CRLF.? ~ (SP | HT).+ ^^ (_ => None)

  val FIELD_NAME_CHAR = elem("FIELD_NAME_CHAR", b => !isCTL(b) && b != ':')

  val fieldName: Parser[String] =
    FIELD_NAME_CHAR.+ ^^ (xs => new String(xs.toArray, "UTF-8"))

  val fieldContent: Parser[Option[Array[Byte]]] = NON_WS.+ ^^ {
    xs => Some(xs.toArray)
  }

  val fieldValue: Parser[Array[Byte]] =
    (fieldContent | LWS).* ^^ {
      case xs =>
        val builder = new ArrayBuilder.ofByte
        var first = true
        xs foreach {
          x =>
            if (x.isDefined) {
              val a = x.get
              if (first) {
                builder ++= a
                first = false
              } else {
                builder += ' '
                builder ++= a
              }
            }
        }
        builder.result
    }

  val messageHeader: Parser[MessageHeader] =
    fieldName ~ elem(':') ~ fieldValue.? ~ CRLF ^^ {
      case n ~ _ ~ v ~ _ => new MessageHeader(n, v.getOrElse(new Array[Byte](0)))
    }

  val HttpSlash = elem('H') ~ elem('T') ~ elem('T') ~ elem('P') ~ elem('/')

  val HttpVersion = HttpSlash ~
    DIGIT.+ ~ elem('.') ~ DIGIT.+ ^^ {
      case _ ~ major ~ _ ~ minor => {
        val m = new String(major.toArray, "UTF-8").toInt
        val n = new String(minor.toArray, "UTF-8").toInt
        new HttpVersion(m, n)
      }
    }

  val RequestURI = NON_WS.+ ^^ {
    xs =>
      val uri = new String(xs.toArray, "UTF-8")
      new URI(uri)
  }

  val Method = NON_WS.+

  val RequestLine = Method ~ SP ~ RequestURI ~ SP ~ HttpVersion ~ CRLF ^^ {
    case m ~ _ ~ uri ~ _ ~ v ~ _ => {
      new RequestLine(new String(m.toArray, "UTF-8"), uri, v)
    }
  }

  val StatusCode = DIGIT ~ DIGIT ~ DIGIT ^^ {
    case a ~ b ~ c => a.toChar.asDigit * 100 + b.toChar.asDigit * 10 +
      c.toChar.asDigit
  }

  val ReasonPhrase = NON_CTL.* ^^ {
    xs => new String(xs.toArray, "UTF-8")
  }

  val StatusLine = HttpVersion ~ SP ~ StatusCode ~ SP ~ ReasonPhrase ~ CRLF ^^ {
    case v ~ _ ~ c ~ _ ~ r ~ _ => {
      new StatusLine(v, c, r)
    }
  }

  //val startLine = RequestLine | StatusLine
  //
  //  val startLine: Parser[String] = NON_CTL.+ ~ CRLF ^^ {
  //    case s ~ _ => new String(s.toArray, "UTF-8")
  //  }

  val startLine = StatusLine | RequestLine

  val genericMessage = startLine ~ messageHeader.+ ~ CRLF ^^ {
    case s ~ m ~ _ => HttpHeader(s, MessageHeaderMap(m: _*))
  }

  def apply(input: Array[Byte]): HttpHeader = {
    HttpHeaderParser.genericMessage(
      new IndexedSeqReader(input)) match {
        case e: HttpHeaderParser.Failure => sys.error(f"ParseError: ${e.msg}")
        case e: HttpHeaderParser.Error => sys.error(f"ParseError: ${e.msg}")
        case r: HttpHeaderParser.Success[_] => r.result match {
          case r: HttpHeader => r
          case _ => sys.error("Unknown Parser Error")
        }
        case _ => sys.error("Unknown Parser Error")
      }
  }
}

object HttpHeaderParser extends HttpHeaderParser