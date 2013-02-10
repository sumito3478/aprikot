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

package info.sumito3478.aprikot.classic.perseus

import scala.xml._
import scala.slick.driver.BasicDriver.simple._
import Database.threadLocalSession
import info.sumito3478.aprikot.classic.perseus.sql.LewisShortDictionaryDatum

object PerseusImporter {
  def importLewisShort(dataPath: String, db: Database) = {
    val xml = XML.load(Source.fromFile(dataPath))
    val teis = xml \\ "entryFree"
    val datum = teis map {
      tei =>
        val key = Lemma.normalize((tei \ "@key").toString)
        new LewisShortDictionaryData(key, tei.toString)
    }
    db withSession {
      datum foreach {
        data =>
          println(s"inserting ${data.key}")
          LewisShortDictionaryDatum.insert(data.key, data.tei, data.html)
      }
    }
  }
}