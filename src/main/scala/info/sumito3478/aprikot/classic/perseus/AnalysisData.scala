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

class AnalysisData(
  val inflected: InflectedWord,
  val lemma: LemmaDescription,
  val vocab: ShortVocabDescription,
  val inflection: InflectionDescription) extends Equals {
  def canEqual(other: Any) = {
    other.isInstanceOf[AnalysisData]
  }

  override def equals(other: Any) = {
    other match {
      case that: AnalysisData => {
        that.canEqual(AnalysisData.this) &&
          inflected == that.inflected &&
          lemma == that.lemma &&
          vocab == that.vocab &&
          inflection == that.inflection
      }
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime * (
      prime * (
        prime * (prime + inflected.hashCode) + lemma.hashCode) +
        vocab.hashCode) + inflection.hashCode
  }
}