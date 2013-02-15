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

import java.lang.{ Character => JChar }

/**
 * Represents Unicode Code Point.
 *
 * @param intern 32-bit integer that represents Unicode Code Point
 */
class CodePoint(val intern: Int) extends AnyVal {
  /**
   * Converts the Code Point to its UTF-16 representation stored in a tuple of
   * Char.
   * If the specified code point is a BMP value, the second element of the tuple
   * is None. If supplementary code point, Some[Char].
   */
  def toChars: (Char, Option[Char]) = {
    val chars = JChar.toChars(intern)
    val len = chars.length
    len match {
      case 1 => (chars(0), None)
      case 2 => (chars(0), Some(chars(1)))
      case _ => sys.error(
        f"java.lang.Character.toChars returned array of which length is $len")
    }
  }

  /**
   * Return the numeric value of the code point in the specified radix.
   */
  def digit(radix: Int): Int = {
    JChar.digit(intern, radix)
  }

  /**
   * Return the Some(Unicode name) of the code point or None if the code point
   * is unassigned.
   */
  def name: Option[String] = {
    Option(JChar.getName(intern))
  }

  /**
   * Return the numeric value that the code point represents.
   *
   * @note For example, Uniocde Character 'ROMAN NUMERAL FIFTY' (U+216C) returns
   *   50.
   */
  def toNumeric(): Int = {
    JChar.getNumericValue(intern)
  }

  // def getType(): Int = ...

  /**
   * Determine if the code point is an alphabet.
   */
//  def isAlphabetic(): Boolean = {
//    JChar.isAlphabetic(intern)
//  }

  /**
   * Determine if the code point is the BMP.
   */
//  def isBmpCodePoint(): Boolean = {
//    JChar.isBmpCodePoint(intern)
//  }

  /**
   * Determine if the code point is defined in Unicode.
   */
  def isDefined(): Boolean = {
    JChar.isDefined(intern)
  }

  /**
   * Determine if the code point is a digit.
   */
  def isDigit(): Boolean = {
    JChar.isDigit(intern)
  }

  /**
   * Determine if the code point should be regarded as an ignorable character
   * in a Uniocde identifier.
   */
  def isIdentifierIgnorable(): Boolean = {
    JChar.isIdentifierIgnorable(intern)
  }

  /**
   * Determine if the code point is a CJKV ideograph.
   */
//  def isIdeographic(): Boolean = {
//    JChar.isIdeographic(intern)
//  }

  /**
   * Determine if the code point is an ISO control character.
   */
  def isISOControl(): Boolean = {
    JChar.isISOControl(intern)
  }

  /**
   * Determine if the code point is a letter.
   */
  def isLetter(): Boolean = {
    JChar.isLetter(intern)
  }

  /**
   * Determine if the code point is a letter or digit.
   */
  def isLetterOrDigit(): Boolean = {
    JChar.isLetterOrDigit(intern)
  }

  /**
   * Determine if the code point is a lowercase character.
   */
  def isLowerCase(): Boolean = {
    JChar.isLowerCase(intern)
  }

  /**
   * Determine whether the code point is mirrored according to the Unicode
   * specification.
   *
   * @note Mirrored characters should have their glyphs horizontally mirroed
   *   when displayed in the text that is right-to-left.
   *
   * @note For example, Unicode Character'LEFT PARENTHESIS' (U+0028) is
   *   semantically defined to be an opening parenthesis. This will appear as
   *   a "(" in text that is left-to-right but as a ")" in text that is
   *   right-to-left.
   */
  def isMirrored(): Boolean = {
    JChar.isMirrored(intern)
  }

  /**
   * Determines if the code point is a Unicode space character.
   */
  def isSpaceChar(): Boolean = {
    JChar.isSpaceChar(intern)
  }

  /**
   * Determinne whether the code point is in the supplementary character range.
   */
  def isSupplementaryCodePoint(): Boolean = {
    JChar.isSupplementaryCodePoint(intern)
  }

  /**
   * Determine if the code point is a titlecase character.
   *
   * @note For example, Uniocde Character 'GREEK CAPITAL LETTER ALPHA WITH
   *   PSILI AND PROSGEGRAMMENI' (U+1F88) is a title case letter.
   */
  def isTitleCase(): Boolean = {
    JChar.isTitleCase(intern)
  }

  /**
   * Determine if the code point may be part of a Unicode identifier as other
   * than the first character.
   */
  def isUnicodeIdentifierPart(): Boolean = {
    JChar.isUnicodeIdentifierPart(intern)
  }

  /**
   * Determine if the code point is an uppercase character.
   */
  def isUpperCase(): Boolean = {
    JChar.isUpperCase(intern)
  }

  /**
   * Determine whether the code point is a valid Unicode code point value.
   */
  def isValidCodePoint(): Boolean = {
    JChar.isValidCodePoint(intern)
  }

  /**
   * Determine if the code point is white space according to Java.
   */
  def isWhiteSpace(): Boolean = {
    JChar.isWhitespace(intern)
  }

  /**
   * Return the titlecase equivalent of the character represented by the code
   * point, if any, otherwise the character itself.
   */
  def toTitleCase(): CodePoint = {
    new CodePoint(JChar.toTitleCase(intern))
  }

  // def toUpperCase ...
  // def toLowerCase ...
}

object CodePoint {
  /**
   * Create CodePoint instance from a code point value.
   */
  def apply(value: Int): CodePoint = {
    new CodePoint(value)
  }

  /**
   * Determine if the pair of Chars is a valid Unicode surrogate pair.
   */
  def isSurrogatePair(high: Char, low: Char): Boolean = {
    JChar.isSurrogatePair(high, low)
  }

  /**
   * Create CodePoint from the Unicode surrogate pair.
   */
  def apply(high: Char, low: Char): CodePoint = {
    apply(JChar.toCodePoint(high, low))
  }

  /**
   * Create CodePoint from the specified digit with the specified radix.
   */
  def apply(digit: Int, radix: Int): CodePoint = {
    apply(JChar.forDigit(digit, radix))
  }
}
