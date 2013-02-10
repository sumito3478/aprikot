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

package info.sumito3478.aprikot.classic.perseus.sql

import scala.slick.driver.BasicDriver.simple._
import Database.threadLocalSession

object LewisShortDictionaryDatum extends Table[(String, String, String)]("LEWIS_SHORT_DICTIONARY_DATUM") {
  def key = column[String]("KEY")

  def tei = column[String]("TEI", O.DBType("TEXT"))

  def html = column[String]("HTML", O.DBType("TEXT"))

  def * = key ~ tei ~ html

  def idx = index("IDX", key)
}