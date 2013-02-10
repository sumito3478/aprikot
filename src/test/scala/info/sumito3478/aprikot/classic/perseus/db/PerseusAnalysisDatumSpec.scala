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

package info.sumito3478.aprikot.classic.perseus.db

import org.scalatest.FunSpec

import scala.slick.driver.BasicDriver.simple._
import Database.threadLocalSession

class PerseusAnalysisDatumSpec extends FunSpec {
  describe("PerseusAnalysisDatum") {
    it("should create new table in database, insert and query") {
      val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
      db withSession {
        val datum = PerseusAnalysisDatum
        datum.ddl.create
        datum.insert("saluto", "salu_to_,saluto", " ", "pres ind act 1st sg")
        val r = Query(datum)
        val data = r.first
        println(data)
        val (inflected, lemma, vocab, inflection) = data
        assert(inflected === "saluto")
        assert(lemma === "salu_to_,saluto")
        assert(vocab === " ")
        assert(inflection === "pres ind act 1st sg")
      }
    }

    it("should query with inflected string") {
      val db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
      db withSession {
        val datum = PerseusAnalysisDatum
        datum.ddl.create
        datum.insert("saluto", "salu_to_,saluto", " ", "pres ind act 1st sg")
        datum.insert("cerebrum", "cere_bru_m,cerebrum",
          "the brain", "neut gen pl")
        datum.insert("cerebrum", "cere_bru_m,cerebrum",
          "the brain", "neut nom/voc/acc sg")
        val r = for (d <- datum if d.inflected === "cerebrum") yield d.inflected
        r.foreach {
          inflected =>
            assert(inflected === "cerebrum")
        }
      }
    }
  }
}