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
package aprikot.text

import scala.collection.immutable
import scala.collection.mutable

import java.text._

import org.scalatest._

import aprikot.NeutralJLocale

class BreakIteratorWSpec extends FunSpec {
  describe("BreakIteratorWSpec#mapIterator") {
    val text = "Mis, Mis, Mister, Drill, Driller, I'll do  my best, I cant lose!"
    val ret = List("Mis", ",", " ", "Mis", ",", " ", "Mister", ",", " ", "Drill", ",", " ", "Driller", ",", " ", "I'll", " ", "do", "  ", "my", " ", "best", ",", " ", "I", " ", "cant", " ", "lose", "!")
    it("should map from text of which length is less than `aheadSize`") {
      val en = NeutralJLocale
      val it = BreakIterator.getWordInstance
      val words = it.mapIterator(new immutable.WrappedString(text).iterator)
      assert(words.toList === ret)
    }

    it("should map from text of which length is greater than `aheadSize`") {
      val en = NeutralJLocale
      val it = BreakIterator.getWordInstance
      val mul = (it.aheadSize / text.length + 1)
      val longText = text * mul
      val longRet = {
        val buffer = new mutable.ListBuffer[String]
        for (_ <- 0 until mul) {
          buffer ++= ret
        }
        buffer.toList
      }
      assert(longText.length > it.aheadSize)
      val words = it.mapIterator(new immutable.WrappedString(longText).iterator)
      assert(words.toList === longRet)
    }

    it("should be able to split a latin text to words with Locale.ENGLISH") {
      val civ5Texts = List(
        ("Te saluto. Augustus sum, imperator et pontifex maximus romae. Si tu es Romae amicus, es gratus.",
          List("Te", " ", "saluto", ".", " ", "Augustus", " ", "sum", ",", " ", "imperator", " ", "et", " ", "pontifex", " ", "maximus", " ", "romae", ".", " ", "Si", " ", "tu", " ", "es", " ", "Romae", " ", "amicus", ",", " ", "es", " ", "gratus", ".")),
        ("Tam fortis, tamen tam stupidus! Utinam habeas cerebrum simile tuae fortitudini.",
          List("Tam", " ", "fortis", ",", " ", "tamen", " ", "tam", " ", "stupidus", "!", " ", "Utinam", " ", "habeas", " ", "cerebrum", " ", "simile", " ", "tuae", " ", "fortitudini", ".")),
        ("Dei fauorem a Roma reuocauerunt. Superati sumus.",
          List("Dei", " ", "fauorem", " ", "a", " ", "Roma", " ", "reuocauerunt", ".", " ", "Superati", " ", "sumus", ".")),
        ("Aerarium meum paucum continet et milites turbidi fiunt...*sigh*...iguitur debes mori.",
          List("Aerarium", " ", "meum", " ", "paucum", " ", "continet", " ", "et", " ", "milites", " ", "turbidi", " ", "fiunt", ".", ".", ".", "*", "sigh", "*", ".", ".", ".", "iguitur", " ", "debes", " ", "mori", ".")),
        ("Da nobis quod uolumus aut consecutiones patere.",
          List("Da", " ", "nobis", " ", "quod", " ", "uolumus", " ", "aut", " ", "consecutiones", " ", "patere", ".")),
        ("Deus Mars nobis iterum subrisit. Ita omnes hostes Romae comprimentur.",
          List("Deus", " ", "Mars", " ", "nobis", " ", "iterum", " ", "subrisit", ".", " ", "Ita", " ", "omnes", " ", "hostes", " ", "Romae", " ", "comprimentur", ".")))
      for (civ5 <- civ5Texts) {
        val en = NeutralJLocale
        val it = BreakIterator.getWordInstance
        val words = it.mapIterator(new immutable.WrappedString(civ5._1).iterator)
        assert(words.toList === civ5._2)
      }
    }
  }
}