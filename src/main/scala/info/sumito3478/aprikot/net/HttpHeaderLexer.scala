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

object HttpHeaderLexer {
  val CHAR = 0 to 127 toList
  val UPALPHA = 'A'.toByte to 'Z'.toByte toList
  val LOALPHA = 'a'.toByte to 'z'.toByte toList
  val ALPHA = UPALPHA ++ LOALPHA
  val DIGIT = '0'.toByte to '9'.toByte toList
  val CTL = (0 to 31 toList) ++ List(127)
  val CR = '\r'.toByte
  val LF = '\n'.toByte
  val SP = ' '.toByte
  val HT = '\t'.toByte
  val DOUBLE_QUOTE = '"'.toByte
  val separators = "()<>@,::\\\"/[]?={}".toList.map(_.toByte) ++ List(SP, HT)

  def isSeparator(b: Byte): Boolean = {
    b match {
      case '(' | ')' | '<' | '>' | '@'
        | ',' | ';' | ':' | '\\' | '\''
        | '/' | '[' | ']' | '?' | '='
        | '{' | '}' | SP | HT => true
      case _ => false
    }
    separators.contains(b)
  }

  def apply(src: Array[Byte]) = {

  }
}