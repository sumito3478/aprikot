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

import scala.util.parsing.combinator.PackratParsers

object AnalysesDataLineParser extends PackratParsers {
  type Elem = Char

  val TAB = elem("TAB", _ == '\t')

  val SP = elem("SP", _ == ' ')

  val LEFT_CURLY_BRACKET = elem("LEFT_CURLY_BRACKET", _ == '{')

  val RIGHT_CURLY_BRACKET = elem("RIGHT_CURLY_BRACKET", _ == '}')

  val NON_CTL = elem("NON_CTL", !"\t {}".contains(_))

  val InflectedWord = NON_CTL.+ ^^ {
    xs =>
      new InflectedWord(xs.mkString)
  }

  val InflectionDescription = NON_CTL.+ ^^ {
    xs =>
      new InflectionDescription(xs.mkString)
  }

  val LemmaDescription = NON_CTL.+ ^^ {
    xs =>
      new LemmaDescription(xs.mkString)
  }

  val ShortVocabDescription = NON_CTL.+ ^^ {
    xs =>
      new ShortVocabDescription(xs.mkString)
  }

  val Line = InflectedWord ~ TAB ~ LEFT_CURLY_BRACKET ~
    NON_CTL.+ ~ NON_CTL.+ ~ LemmaDescription ~ TAB ~ ShortVocabDescription ~
    TAB ~ InflectionDescription ~ RIGHT_CURLY_BRACKET ^^ {
    case inflected ~ _ ~ _ ~ _ ~ _ ~ lemma ~ _ ~ vocab ~ _ ~ inflection ~ _ =>
      new AnalysesData(inflected, lemma, vocab, inflection)
  }
}