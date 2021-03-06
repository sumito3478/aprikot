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
import scala.util.parsing.input.CharSequenceReader

import scala.slick.driver.BasicDriver.simple._
import Database.threadLocalSession

import java.io._

import org.apache.commons.io._

import aprikot.classic.perseus.db._

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
      val insertInvoker = LewisShortDictionaryDatum.insertInvoker
      datum foreach {
        data =>
          println(s"inserting ${data.key}...")
          insertInvoker.insert(data.key.toLowerCase, data.tei, data.html)
      }
    }
  }

  def importLatinAnalyses(db: Database, dataPath: String) = {
    val it = FileUtils.lineIterator(new File(dataPath), "UTF-8")
    try {
      val lines = Iterator.continually(it.next).takeWhile(_ => it.hasNext)
      db withSession {
        val insertInvoker = PerseusAnalysisDatum.insertInvoker
        for (line <- lines) {
          val r = AnalysisDataParser.Line(new CharSequenceReader(line))
          r match {
            case s: AnalysisDataParser.Success[_] => {
              for (data <- s.result) {
                println(s"inserting ${data.inflected.underlined}")
                insertInvoker.insert(
                  data.inflected.underlined.toLowerCase,
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

  def openDefaultDatabase = Database.forURL("jdbc:h2:~/.aprikot/perseus_data/perseus_data")

  val defaultLewisShortDataPath =
    "/usr/share/diogenes/perl/Perseus_Data/1999.04.0059.xml"

  val defaultLatinAnalysesDataPath =
    "/usr/share/diogenes/perl/Perseus_Data/latin-analyses.txt"

  def importAll(
    db: => Database = openDefaultDatabase,
    lewisShortDataPath: String = defaultLewisShortDataPath,
    latinAnalysesDataPath: String = defaultLatinAnalysesDataPath) = {
    db withTransaction {
      LewisShortDictionaryDatum.ddl.create
      importLewisShort(db, lewisShortDataPath)
      PerseusAnalysisDatum.ddl.create
      importLatinAnalyses(db, defaultLatinAnalysesDataPath)
    }
  }
}