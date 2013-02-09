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

import org.scalatest.FunSpec
import scala.util.parsing.input.CharSequenceReader

class AnalysesDataParserSpec extends FunSpec {
  describe("AnalysesDataParserSpec#Line") {
    it("should returns AnalysesData") {
      val l = "saluto\t{67838076 9 salu_to_,saluto\t \tpres ind act 1st sg}"
      val r = AnalysesDataParser.Line(new CharSequenceReader(l))
      assert(r.isInstanceOf[AnalysesDataParser.Success[_]] === true)
      r match {
        case s: AnalysesDataParser.Success[_] =>
          assert(s.result.head === new AnalysesData(
            new InflectedWord("saluto"),
            new LemmaDescription("67838076 9 salu_to_,saluto"),
            new ShortVocabDescription(" "),
            new InflectionDescription("pres ind act 1st sg")))
        case f: AnalysesDataParser.Failure => {
          println(f)
        }
        case e: AnalysesDataParser.Error => {
          println(e.msg)
        }
      }
    }
  }
}