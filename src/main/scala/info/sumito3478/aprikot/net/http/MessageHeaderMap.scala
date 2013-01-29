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

import scala.collection.immutable.TreeMap

import info.sumito3478.aprikot.text.NeutralIgnoreCaseOrder

class MessageHeaderMap private (
  map: TreeMap[String, MessageHeader]) extends Map[String, MessageHeader] {
  def get(key: String): Option[MessageHeader] = map get key

  def iterator: Iterator[(String, MessageHeader)] = map.iterator

  def +[B1 >: MessageHeader](kv: (String, B1)): Map[String, B1] = {
    map + kv
  }

  def +(kv: (String, MessageHeader)): MessageHeaderMap = {
    new MessageHeaderMap(map + kv)
  }

  def -(key: String): MessageHeaderMap = {
    new MessageHeaderMap(map - key)
  }

}

object MessageHeaderMap {
  def apply(elems: MessageHeader*): MessageHeaderMap = {
    new MessageHeaderMap(
      TreeMap[String, MessageHeader](
        elems.map(e => (e.fieldName, e)): _*)(NeutralIgnoreCaseOrder))
  }
}