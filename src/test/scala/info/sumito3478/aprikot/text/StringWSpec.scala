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

import org.scalatest._

class StringWWSpec extends FunSpec {
  describe("StringW#neutralWordIterator") {
    it("should be able to split a latin text") {
      val civ5Texts = List(
        ("Te saluto. Augustus sum, imperator et pontifex maximus romae. Si tu es Romae amicus, es gratus.",
          List("Te", "saluto", "Augustus", "sum", "imperator", "et", "pontifex", "maximus", "romae", "Si", "tu", "es", "Romae", "amicus", "es", "gratus")),
        ("Tam fortis, tamen tam stupidus! Utinam habeas cerebrum simile tuae fortitudini.",
          List("Tam", "fortis", "tamen", "tam", "stupidus", "Utinam", "habeas", "cerebrum", "simile", "tuae", "fortitudini")),
        ("Dei fauorem a Roma reuocauerunt. Superati sumus.",
          List("Dei", "fauorem", "a", "Roma", "reuocauerunt", "Superati", "sumus")),
        ("Aerarium meum paucum continet et milites turbidi fiunt...*sigh*...iguitur debes mori.",
          List("Aerarium", "meum", "paucum", "continet", "et", "milites", "turbidi", "fiunt", "sigh", "iguitur", "debes", "mori")),
        ("Da nobis quod uolumus aut consecutiones patere.",
          List("Da", "nobis", "quod", "uolumus", "aut", "consecutiones", "patere")),
        ("Deus Mars nobis iterum subrisit. Ita omnes hostes Romae comprimentur.",
          List("Deus", "Mars", "nobis", "iterum", "subrisit", "Ita", "omnes", "hostes", "Romae", "comprimentur")))
      for (civ5 <- civ5Texts) {
        assert(civ5._1.neutralWordIterator.toList === civ5._2)
      }
    }
  }
}