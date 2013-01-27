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

import scala.util.parsing.combinator.Parsers

object HttpHeaderParser extends Parsers {
  type Elem = Byte
  
  val any = elem("any", _ => true)
  
  val OCTET = elem("OCTET", _ => true)
  
  val SP = elem("SP", _ == ' ')
    
  val HT = elem("HT", _ == '\t')
  
  val LWS = lineBreak.? ~ (SP | HT).*
  
  val FIELD_NAME_CHAR = elem("FIELD_NAME_CHAR", _ != ':')
  
  val fieldName = FIELD_NAME_CHAR.+
  
  val fieldContent = OCTET.+
  
  val fieldValue = (LWS | fieldContent).*
  
  val messageHeader = fieldName ~ elem("COLON", _ == ':') ~ fieldValue.?
  
  val CR = elem("CR", _ == '\r')
  
  val LF = elem("LF", _ == '\n')
  
  val lineBreak = CR.? ~ LF
  
  val startLine = OCTET.+
  
  val httpMessage = (lineBreak.* ~ startLine ~ lineBreak ~ lineBreak.?
      ~ (messageHeader ~ lineBreak).* ~ lineBreak ~ lineBreak)
      
}