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
import info.sumito3478.aprikot.classic.perseus.db.LewisShortDictionaryDatum
import org.apache.commons.io.FileUtils
import java.io.File
import scala.util.parsing.input.CharSequenceReader
import info.sumito3478.aprikot.classic.perseus.db.PerseusAnalysisDatum

object PerseusImporter {
  def importLewisShort(db: Database, dataPath: String) = {
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

  def importLatinAnalyses(db: Database, dataPath: String) = {
    val it = FileUtils.lineIterator(new File(dataPath), "UTF-8")
    try {
      val lines = Iterator.continually(it.next).takeWhile(_ => it.hasNext)
      db withSession {
        for (line <- lines) {
          val r = AnalysisDataParser.Line(new CharSequenceReader(line))
          r match {
            case s: AnalysisDataParser.Success[_] => {
              for (data <- s.result) {
                PerseusAnalysisDatum.insert(
                  data.inflected.underlined,
                  data.lemma.underlined,
                  data.vocab.underlined,
                  data.inflection.underlined)
              }
            }
            case _ => {
              // TODO: Log non successful parsing result
            }
          }
        }
      }
    } finally {
      it.close
    }
  }
}