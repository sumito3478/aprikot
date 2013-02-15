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
package aprikot.classic.perseus

import scala.xml._

case class LewisShortDictionaryData(val key: String, val tei: String) {
  import LewisShortDictionaryData._

  def teiElem = XML.load(Source.fromString(tei))

  def htmlElem = mapTei2Html(teiElem)

  def html = htmlElem.toString
}

object LewisShortDictionaryData {
  private def mapTei2Html(e: Elem): Elem = {
    //var Elem(prefix, label, attributes, scope, child) = e
    var label = e.label
    var attributes = e.attributes
    val child = e.child
    if (label == "sense") {
      label = "div"
      val level = Option(attributes("level")).getOrElse("0").toString.toInt
      val padding = (level * 2).toInt
      attributes = attributes append new UnprefixedAttribute(
        "style",
        s"padding-left: ${padding}em; padding-bottom: 0.5em;",
        attributes)
    } else if (Option(attributes("rend")).getOrElse("").toString == "ital") {
      label = "i"
    }
    val c = child map {
      c =>
        c match {
          case e: Elem => mapTei2Html(e)
          case n => n
        }
    }
    new Elem(null, label, attributes, e.scope, true, c: _*)
  }
}