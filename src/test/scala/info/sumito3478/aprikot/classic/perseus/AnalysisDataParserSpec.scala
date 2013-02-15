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

import scala.util.parsing.input._

import org.scalatest._

class AnalysisDataParserSpec extends FunSpec {
  describe("AnalysesDataParserSpec#Line") {
    it("should return list of AnalysisData") {
      val l = "saluto\t{67838076 9 salu_to_,saluto\t \tpres ind act 1st sg}"
      val r = AnalysisDataParser.Line(new CharSequenceReader(l))
      assert(r.isInstanceOf[AnalysisDataParser.Success[_]] === true)
      r match {
        case s: AnalysisDataParser.Success[_] =>
          assert(s.result.head === new AnalysisData(
            new InflectedWord("saluto"),
            new LemmaDescription("salu_to_,saluto"),
            new ShortVocabDescription(" "),
            new InflectionDescription("pres ind act 1st sg")))
        case f: AnalysisDataParser.Failure => {
          println(f)
        }
        case e: AnalysisDataParser.Error => {
          println(e.msg)
        }
      }
    }
    it("""should return List of AnalysisData for the line that contains
        |  multiple analysis datum.""".stripMargin) {
      val l = "cerebrum\t{12844671 9 cere_bru_m,cerebrum\tthe brain\tneut gen pl}{12844671 9 cere_brum,cerebrum\tthe brain\tneut nom/voc/acc sg}"
      val r = AnalysisDataParser.Line(new CharSequenceReader(l))
      assert(r.isInstanceOf[AnalysisDataParser.Success[_]] === true)
      r match {
        case s: AnalysisDataParser.Success[_] => {
          val xs = s.result
          assert(xs.length === 2)
          assert(xs(0) === new AnalysisData(
            new InflectedWord("cerebrum"),
            new LemmaDescription("cere_bru_m,cerebrum"),
            new ShortVocabDescription("the brain"),
            new InflectionDescription("neut gen pl")))
          assert(xs(1) === new AnalysisData(
            new InflectedWord("cerebrum"),
            new LemmaDescription("cere_brum,cerebrum"),
            new ShortVocabDescription("the brain"),
            new InflectionDescription("neut nom/voc/acc sg")))
        }
        case f: AnalysisDataParser.Failure => {
          println(f)
        }
        case e: AnalysisDataParser.Error => {
          println(e.msg)
        }
      }
    }
  }
}