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
package info.sumito3478.aprikot.text

import java.lang.Character.{ UnicodeBlock => JUnicodeBlock }
import scala.collection.immutable.HashMap

object UnicodeBlock {
  /**
   * the List of names of Unicode Blocks in upper snake case.
   *
   * @note the order of this list is exactly the same as
   * {UnicodeBlock.camelNames}
   */
  val snakeNames = List(
    "BASIC_LATIN",
    "LATIN_1_SUPPLEMENT",
    "LATIN_EXTENDED_A",
    "LATIN_EXTENDED_B",
    "IPA_EXTENSIONS",
    "SPACING_MODIFIER_LETTERS",
    "COMBINING_DIACRITICAL_MARKS",
    "GREEK",
    "CYRILLIC",
    "ARMENIAN",
    "HEBREW",
    "ARABIC",
    "DEVANAGARI",
    "BENGALI",
    "GURMUKHI",
    "GUJARATI",
    "ORIYA",
    "TAMIL",
    "TELUGU",
    "KANNADA",
    "MALAYALAM",
    "THAI",
    "LAO",
    "TIBETAN",
    "GEORGIAN",
    "HANGUL_JAMO",
    "LATIN_EXTENDED_ADDITIONAL",
    "GREEK_EXTENDED",
    "GENERAL_PUNCTUATION",
    "SUPERSCRIPTS_AND_SUBSCRIPTS",
    "CURRENCY_SYMBOLS",
    "COMBINING_MARKS_FOR_SYMBOLS",
    "LETTERLIKE_SYMBOLS",
    "NUMBER_FORMS",
    "ARROWS",
    "MATHEMATICAL_OPERATORS",
    "MISCELLANEOUS_TECHNICAL",
    "CONTROL_PICTURES",
    "OPTICAL_CHARACTER_RECOGNITION",
    "ENCLOSED_ALPHANUMERICS",
    "BOX_DRAWING",
    "BLOCK_ELEMENTS",
    "GEOMETRIC_SHAPES",
    "MISCELLANEOUS_SYMBOLS",
    "DINGBATS",
    "CJK_SYMBOLS_AND_PUNCTUATION",
    "HIRAGANA",
    "KATAKANA",
    "BOPOMOFO",
    "HANGUL_COMPATIBILITY_JAMO",
    "KANBUN",
    "ENCLOSED_CJK_LETTERS_AND_MONTHS",
    "CJK_COMPATIBILITY",
    "CJK_UNIFIED_IDEOGRAPHS",
    "HANGUL_SYLLABLES",
    "PRIVATE_USE_AREA",
    "CJK_COMPATIBILITY_IDEOGRAPHS",
    "ALPHABETIC_PRESENTATION_FORMS",
    "ARABIC_PRESENTATION_FORMS_A",
    "COMBINING_HALF_MARKS",
    "CJK_COMPATIBILITY_FORMS",
    "SMALL_FORM_VARIANTS",
    "ARABIC_PRESENTATION_FORMS_B",
    "HALFWIDTH_AND_FULLWIDTH_FORMS",
    "SPECIALS",
    //    java.lang.Character.UnicodeBlock.SURROGATES_AREA is deprecated. 
    //    "SURROGATES_AREA",
    "SYRIAC",
    "THAANA",
    "SINHALA",
    "MYANMAR",
    "ETHIOPIC",
    "CHEROKEE",
    "UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS",
    "OGHAM",
    "RUNIC",
    "KHMER",
    "MONGOLIAN",
    "BRAILLE_PATTERNS",
    "CJK_RADICALS_SUPPLEMENT",
    "KANGXI_RADICALS",
    "IDEOGRAPHIC_DESCRIPTION_CHARACTERS",
    "BOPOMOFO_EXTENDED",
    "CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A",
    "YI_SYLLABLES",
    "YI_RADICALS",
    "CYRILLIC_SUPPLEMENTARY",
    "TAGALOG",
    "HANUNOO",
    "BUHID",
    "TAGBANWA",
    "LIMBU",
    "TAI_LE",
    "KHMER_SYMBOLS",
    "PHONETIC_EXTENSIONS",
    "MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A",
    "SUPPLEMENTAL_ARROWS_A",
    "SUPPLEMENTAL_ARROWS_B",
    "MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B",
    "SUPPLEMENTAL_MATHEMATICAL_OPERATORS",
    "MISCELLANEOUS_SYMBOLS_AND_ARROWS",
    "KATAKANA_PHONETIC_EXTENSIONS",
    "YIJING_HEXAGRAM_SYMBOLS",
    "VARIATION_SELECTORS",
    "LINEAR_B_SYLLABARY",
    "LINEAR_B_IDEOGRAMS",
    "AEGEAN_NUMBERS",
    "OLD_ITALIC",
    "GOTHIC",
    "UGARITIC",
    "DESERET",
    "SHAVIAN",
    "OSMANYA",
    "CYPRIOT_SYLLABARY",
    "BYZANTINE_MUSICAL_SYMBOLS",
    "MUSICAL_SYMBOLS",
    "TAI_XUAN_JING_SYMBOLS",
    "MATHEMATICAL_ALPHANUMERIC_SYMBOLS",
    "CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B",
    "CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT",
    "TAGS",
    "VARIATION_SELECTORS_SUPPLEMENT",
    "SUPPLEMENTARY_PRIVATE_USE_AREA_A",
    "SUPPLEMENTARY_PRIVATE_USE_AREA_B",
    "HIGH_SURROGATES",
    "HIGH_PRIVATE_USE_SURROGATES",
    "LOW_SURROGATES",
    "ARABIC_SUPPLEMENT",
    "NKO",
    "SAMARITAN",
    "MANDAIC",
    "ETHIOPIC_SUPPLEMENT",
    "UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED",
    "NEW_TAI_LUE",
    "BUGINESE",
    "TAI_THAM",
    "BALINESE",
    "SUNDANESE",
    "BATAK",
    "LEPCHA",
    "OL_CHIKI",
    "VEDIC_EXTENSIONS",
    "PHONETIC_EXTENSIONS_SUPPLEMENT",
    "COMBINING_DIACRITICAL_MARKS_SUPPLEMENT",
    "GLAGOLITIC",
    "LATIN_EXTENDED_C",
    "COPTIC",
    "GEORGIAN_SUPPLEMENT",
    "TIFINAGH",
    "ETHIOPIC_EXTENDED",
    "CYRILLIC_EXTENDED_A",
    "SUPPLEMENTAL_PUNCTUATION",
    "CJK_STROKES",
    "LISU",
    "VAI",
    "CYRILLIC_EXTENDED_B",
    "BAMUM",
    "MODIFIER_TONE_LETTERS",
    "LATIN_EXTENDED_D",
    "SYLOTI_NAGRI",
    "COMMON_INDIC_NUMBER_FORMS",
    "PHAGS_PA",
    "SAURASHTRA",
    "DEVANAGARI_EXTENDED",
    "KAYAH_LI",
    "REJANG",
    "HANGUL_JAMO_EXTENDED_A",
    "JAVANESE",
    "CHAM",
    "MYANMAR_EXTENDED_A",
    "TAI_VIET",
    "ETHIOPIC_EXTENDED_A",
    "MEETEI_MAYEK",
    "HANGUL_JAMO_EXTENDED_B",
    "VERTICAL_FORMS",
    "ANCIENT_GREEK_NUMBERS",
    "ANCIENT_SYMBOLS",
    "PHAISTOS_DISC",
    "LYCIAN",
    "CARIAN",
    "OLD_PERSIAN",
    "IMPERIAL_ARAMAIC",
    "PHOENICIAN",
    "LYDIAN",
    "KHAROSHTHI",
    "OLD_SOUTH_ARABIAN",
    "AVESTAN",
    "INSCRIPTIONAL_PARTHIAN",
    "INSCRIPTIONAL_PAHLAVI",
    "OLD_TURKIC",
    "RUMI_NUMERAL_SYMBOLS",
    "BRAHMI",
    "KAITHI",
    "CUNEIFORM",
    "CUNEIFORM_NUMBERS_AND_PUNCTUATION",
    "EGYPTIAN_HIEROGLYPHS",
    "BAMUM_SUPPLEMENT",
    "KANA_SUPPLEMENT",
    "ANCIENT_GREEK_MUSICAL_NOTATION",
    "COUNTING_ROD_NUMERALS",
    "MAHJONG_TILES",
    "DOMINO_TILES",
    "PLAYING_CARDS",
    "ENCLOSED_ALPHANUMERIC_SUPPLEMENT",
    "ENCLOSED_IDEOGRAPHIC_SUPPLEMENT",
    "MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS",
    "EMOTICONS",
    "TRANSPORT_AND_MAP_SYMBOLS",
    "ALCHEMICAL_SYMBOLS",
    "CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C",
    "CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D")

  //  /**
  //   * the List of names of Unicode Blocks in camel case.
  //   *
  //   * @note the order of this list is exactly the same as
  //   * {UnicodeBlock.camelNames}
  //   */
  //  val camelNames = snakeNames map (xs =>
  //    WordUtils.capitalizeFully(xs, '_').replaceAll("_", ""))

  //  def generateSource: String = {
  //    val builder = new StringBuilder
  //    val names = snakeNames zip camelNames
  //    names foreach {
  //      name =>
  //        val (snake, camel) = name
  //        builder ++= f"""object ${camel} extends UnicodeBlock {
  //          |val intern = JUnicodeBlock.${snake}
  //          |}
  //          |
  //          |""".stripMargin
  //    }
  //    builder ++= """val blocks = List(
  //      |""".stripMargin
  //    builder ++= (camelNames.foldRight[List[String]](List.empty[String]) {
  //      (camel, acc) =>
  //        f"${camel}" :: acc
  //    }.mkString(""",
  //        |""".stripMargin))
  //    builder ++= """)
  //      |
  //      |""".stripMargin
  //    builder ++= """val jblocks = {
  //      |import JUnicodeBlock._
  //      |List(
  //      |""".stripMargin
  //    builder ++= (snakeNames.foldRight[List[String]](List.empty[String]) {
  //      (snake, acc) =>
  //        f"${snake}" :: acc
  //    }.mkString(""",
  //        |""".stripMargin))
  //    builder ++= """)
  //      |}
  //      |
  //      |""".stripMargin
  //    builder.result
  //  }

  object BasicLatin extends UnicodeBlock {
    val intern = JUnicodeBlock.BASIC_LATIN
  }

  object Latin1Supplement extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_1_SUPPLEMENT
  }

  object LatinExtendedA extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_EXTENDED_A
  }

  object LatinExtendedB extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_EXTENDED_B
  }

  object IpaExtensions extends UnicodeBlock {
    val intern = JUnicodeBlock.IPA_EXTENSIONS
  }

  object SpacingModifierLetters extends UnicodeBlock {
    val intern = JUnicodeBlock.SPACING_MODIFIER_LETTERS
  }

  object CombiningDiacriticalMarks extends UnicodeBlock {
    val intern = JUnicodeBlock.COMBINING_DIACRITICAL_MARKS
  }

  object Greek extends UnicodeBlock {
    val intern = JUnicodeBlock.GREEK
  }

  object Cyrillic extends UnicodeBlock {
    val intern = JUnicodeBlock.CYRILLIC
  }

  object Armenian extends UnicodeBlock {
    val intern = JUnicodeBlock.ARMENIAN
  }

  object Hebrew extends UnicodeBlock {
    val intern = JUnicodeBlock.HEBREW
  }

  object Arabic extends UnicodeBlock {
    val intern = JUnicodeBlock.ARABIC
  }

  object Devanagari extends UnicodeBlock {
    val intern = JUnicodeBlock.DEVANAGARI
  }

  object Bengali extends UnicodeBlock {
    val intern = JUnicodeBlock.BENGALI
  }

  object Gurmukhi extends UnicodeBlock {
    val intern = JUnicodeBlock.GURMUKHI
  }

  object Gujarati extends UnicodeBlock {
    val intern = JUnicodeBlock.GUJARATI
  }

  object Oriya extends UnicodeBlock {
    val intern = JUnicodeBlock.ORIYA
  }

  object Tamil extends UnicodeBlock {
    val intern = JUnicodeBlock.TAMIL
  }

  object Telugu extends UnicodeBlock {
    val intern = JUnicodeBlock.TELUGU
  }

  object Kannada extends UnicodeBlock {
    val intern = JUnicodeBlock.KANNADA
  }

  object Malayalam extends UnicodeBlock {
    val intern = JUnicodeBlock.MALAYALAM
  }

  object Thai extends UnicodeBlock {
    val intern = JUnicodeBlock.THAI
  }

  object Lao extends UnicodeBlock {
    val intern = JUnicodeBlock.LAO
  }

  object Tibetan extends UnicodeBlock {
    val intern = JUnicodeBlock.TIBETAN
  }

  object Georgian extends UnicodeBlock {
    val intern = JUnicodeBlock.GEORGIAN
  }

  object HangulJamo extends UnicodeBlock {
    val intern = JUnicodeBlock.HANGUL_JAMO
  }

  object LatinExtendedAdditional extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_EXTENDED_ADDITIONAL
  }

  object GreekExtended extends UnicodeBlock {
    val intern = JUnicodeBlock.GREEK_EXTENDED
  }

  object GeneralPunctuation extends UnicodeBlock {
    val intern = JUnicodeBlock.GENERAL_PUNCTUATION
  }

  object SuperscriptsAndSubscripts extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPERSCRIPTS_AND_SUBSCRIPTS
  }

  object CurrencySymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.CURRENCY_SYMBOLS
  }

  object CombiningMarksForSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.COMBINING_MARKS_FOR_SYMBOLS
  }

  object LetterlikeSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.LETTERLIKE_SYMBOLS
  }

  object NumberForms extends UnicodeBlock {
    val intern = JUnicodeBlock.NUMBER_FORMS
  }

  object Arrows extends UnicodeBlock {
    val intern = JUnicodeBlock.ARROWS
  }

  object MathematicalOperators extends UnicodeBlock {
    val intern = JUnicodeBlock.MATHEMATICAL_OPERATORS
  }

  object MiscellaneousTechnical extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_TECHNICAL
  }

  object ControlPictures extends UnicodeBlock {
    val intern = JUnicodeBlock.CONTROL_PICTURES
  }

  object OpticalCharacterRecognition extends UnicodeBlock {
    val intern = JUnicodeBlock.OPTICAL_CHARACTER_RECOGNITION
  }

  object EnclosedAlphanumerics extends UnicodeBlock {
    val intern = JUnicodeBlock.ENCLOSED_ALPHANUMERICS
  }

  object BoxDrawing extends UnicodeBlock {
    val intern = JUnicodeBlock.BOX_DRAWING
  }

  object BlockElements extends UnicodeBlock {
    val intern = JUnicodeBlock.BLOCK_ELEMENTS
  }

  object GeometricShapes extends UnicodeBlock {
    val intern = JUnicodeBlock.GEOMETRIC_SHAPES
  }

  object MiscellaneousSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_SYMBOLS
  }

  object Dingbats extends UnicodeBlock {
    val intern = JUnicodeBlock.DINGBATS
  }

  object CjkSymbolsAndPunctuation extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
  }

  object Hiragana extends UnicodeBlock {
    val intern = JUnicodeBlock.HIRAGANA
  }

  object Katakana extends UnicodeBlock {
    val intern = JUnicodeBlock.KATAKANA
  }

  object Bopomofo extends UnicodeBlock {
    val intern = JUnicodeBlock.BOPOMOFO
  }

  object HangulCompatibilityJamo extends UnicodeBlock {
    val intern = JUnicodeBlock.HANGUL_COMPATIBILITY_JAMO
  }

  object Kanbun extends UnicodeBlock {
    val intern = JUnicodeBlock.KANBUN
  }

  object EnclosedCjkLettersAndMonths extends UnicodeBlock {
    val intern = JUnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS
  }

  object CjkCompatibility extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_COMPATIBILITY
  }

  object CjkUnifiedIdeographs extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
  }

  object HangulSyllables extends UnicodeBlock {
    val intern = JUnicodeBlock.HANGUL_SYLLABLES
  }

  object PrivateUseArea extends UnicodeBlock {
    val intern = JUnicodeBlock.PRIVATE_USE_AREA
  }

  object CjkCompatibilityIdeographs extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
  }

  object AlphabeticPresentationForms extends UnicodeBlock {
    val intern = JUnicodeBlock.ALPHABETIC_PRESENTATION_FORMS
  }

  object ArabicPresentationFormsA extends UnicodeBlock {
    val intern = JUnicodeBlock.ARABIC_PRESENTATION_FORMS_A
  }

  object CombiningHalfMarks extends UnicodeBlock {
    val intern = JUnicodeBlock.COMBINING_HALF_MARKS
  }

  object CjkCompatibilityForms extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_COMPATIBILITY_FORMS
  }

  object SmallFormVariants extends UnicodeBlock {
    val intern = JUnicodeBlock.SMALL_FORM_VARIANTS
  }

  object ArabicPresentationFormsB extends UnicodeBlock {
    val intern = JUnicodeBlock.ARABIC_PRESENTATION_FORMS_B
  }

  object HalfwidthAndFullwidthForms extends UnicodeBlock {
    val intern = JUnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
  }

  object Specials extends UnicodeBlock {
    val intern = JUnicodeBlock.SPECIALS
  }

  object Syriac extends UnicodeBlock {
    val intern = JUnicodeBlock.SYRIAC
  }

  object Thaana extends UnicodeBlock {
    val intern = JUnicodeBlock.THAANA
  }

  object Sinhala extends UnicodeBlock {
    val intern = JUnicodeBlock.SINHALA
  }

  object Myanmar extends UnicodeBlock {
    val intern = JUnicodeBlock.MYANMAR
  }

  object Ethiopic extends UnicodeBlock {
    val intern = JUnicodeBlock.ETHIOPIC
  }

  object Cherokee extends UnicodeBlock {
    val intern = JUnicodeBlock.CHEROKEE
  }

  object UnifiedCanadianAboriginalSyllabics extends UnicodeBlock {
    val intern = JUnicodeBlock.UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS
  }

  object Ogham extends UnicodeBlock {
    val intern = JUnicodeBlock.OGHAM
  }

  object Runic extends UnicodeBlock {
    val intern = JUnicodeBlock.RUNIC
  }

  object Khmer extends UnicodeBlock {
    val intern = JUnicodeBlock.KHMER
  }

  object Mongolian extends UnicodeBlock {
    val intern = JUnicodeBlock.MONGOLIAN
  }

  object BraillePatterns extends UnicodeBlock {
    val intern = JUnicodeBlock.BRAILLE_PATTERNS
  }

  object CjkRadicalsSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_RADICALS_SUPPLEMENT
  }

  object KangxiRadicals extends UnicodeBlock {
    val intern = JUnicodeBlock.KANGXI_RADICALS
  }

  object IdeographicDescriptionCharacters extends UnicodeBlock {
    val intern = JUnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS
  }

  object BopomofoExtended extends UnicodeBlock {
    val intern = JUnicodeBlock.BOPOMOFO_EXTENDED
  }

  object CjkUnifiedIdeographsExtensionA extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
  }

  object YiSyllables extends UnicodeBlock {
    val intern = JUnicodeBlock.YI_SYLLABLES
  }

  object YiRadicals extends UnicodeBlock {
    val intern = JUnicodeBlock.YI_RADICALS
  }

  object CyrillicSupplementary extends UnicodeBlock {
    val intern = JUnicodeBlock.CYRILLIC_SUPPLEMENTARY
  }

  object Tagalog extends UnicodeBlock {
    val intern = JUnicodeBlock.TAGALOG
  }

  object Hanunoo extends UnicodeBlock {
    val intern = JUnicodeBlock.HANUNOO
  }

  object Buhid extends UnicodeBlock {
    val intern = JUnicodeBlock.BUHID
  }

  object Tagbanwa extends UnicodeBlock {
    val intern = JUnicodeBlock.TAGBANWA
  }

  object Limbu extends UnicodeBlock {
    val intern = JUnicodeBlock.LIMBU
  }

  object TaiLe extends UnicodeBlock {
    val intern = JUnicodeBlock.TAI_LE
  }

  object KhmerSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.KHMER_SYMBOLS
  }

  object PhoneticExtensions extends UnicodeBlock {
    val intern = JUnicodeBlock.PHONETIC_EXTENSIONS
  }

  object MiscellaneousMathematicalSymbolsA extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A
  }

  object SupplementalArrowsA extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTAL_ARROWS_A
  }

  object SupplementalArrowsB extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTAL_ARROWS_B
  }

  object MiscellaneousMathematicalSymbolsB extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B
  }

  object SupplementalMathematicalOperators extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTAL_MATHEMATICAL_OPERATORS
  }

  object MiscellaneousSymbolsAndArrows extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_ARROWS
  }

  object KatakanaPhoneticExtensions extends UnicodeBlock {
    val intern = JUnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS
  }

  object YijingHexagramSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.YIJING_HEXAGRAM_SYMBOLS
  }

  object VariationSelectors extends UnicodeBlock {
    val intern = JUnicodeBlock.VARIATION_SELECTORS
  }

  object LinearBSyllabary extends UnicodeBlock {
    val intern = JUnicodeBlock.LINEAR_B_SYLLABARY
  }

  object LinearBIdeograms extends UnicodeBlock {
    val intern = JUnicodeBlock.LINEAR_B_IDEOGRAMS
  }

  object AegeanNumbers extends UnicodeBlock {
    val intern = JUnicodeBlock.AEGEAN_NUMBERS
  }

  object OldItalic extends UnicodeBlock {
    val intern = JUnicodeBlock.OLD_ITALIC
  }

  object Gothic extends UnicodeBlock {
    val intern = JUnicodeBlock.GOTHIC
  }

  object Ugaritic extends UnicodeBlock {
    val intern = JUnicodeBlock.UGARITIC
  }

  object Deseret extends UnicodeBlock {
    val intern = JUnicodeBlock.DESERET
  }

  object Shavian extends UnicodeBlock {
    val intern = JUnicodeBlock.SHAVIAN
  }

  object Osmanya extends UnicodeBlock {
    val intern = JUnicodeBlock.OSMANYA
  }

  object CypriotSyllabary extends UnicodeBlock {
    val intern = JUnicodeBlock.CYPRIOT_SYLLABARY
  }

  object ByzantineMusicalSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.BYZANTINE_MUSICAL_SYMBOLS
  }

  object MusicalSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.MUSICAL_SYMBOLS
  }

  object TaiXuanJingSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.TAI_XUAN_JING_SYMBOLS
  }

  object MathematicalAlphanumericSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.MATHEMATICAL_ALPHANUMERIC_SYMBOLS
  }

  object CjkUnifiedIdeographsExtensionB extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
  }

  object CjkCompatibilityIdeographsSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
  }

  object Tags extends UnicodeBlock {
    val intern = JUnicodeBlock.TAGS
  }

  object VariationSelectorsSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.VARIATION_SELECTORS_SUPPLEMENT
  }

  object SupplementaryPrivateUseAreaA extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTARY_PRIVATE_USE_AREA_A
  }

  object SupplementaryPrivateUseAreaB extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTARY_PRIVATE_USE_AREA_B
  }

  object HighSurrogates extends UnicodeBlock {
    val intern = JUnicodeBlock.HIGH_SURROGATES
  }

  object HighPrivateUseSurrogates extends UnicodeBlock {
    val intern = JUnicodeBlock.HIGH_PRIVATE_USE_SURROGATES
  }

  object LowSurrogates extends UnicodeBlock {
    val intern = JUnicodeBlock.LOW_SURROGATES
  }

  object ArabicSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.ARABIC_SUPPLEMENT
  }

  object Nko extends UnicodeBlock {
    val intern = JUnicodeBlock.NKO
  }

  object Samaritan extends UnicodeBlock {
    val intern = JUnicodeBlock.SAMARITAN
  }

  object Mandaic extends UnicodeBlock {
    val intern = JUnicodeBlock.MANDAIC
  }

  object EthiopicSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.ETHIOPIC_SUPPLEMENT
  }

  object UnifiedCanadianAboriginalSyllabicsExtended extends UnicodeBlock {
    val intern = JUnicodeBlock.UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED
  }

  object NewTaiLue extends UnicodeBlock {
    val intern = JUnicodeBlock.NEW_TAI_LUE
  }

  object Buginese extends UnicodeBlock {
    val intern = JUnicodeBlock.BUGINESE
  }

  object TaiTham extends UnicodeBlock {
    val intern = JUnicodeBlock.TAI_THAM
  }

  object Balinese extends UnicodeBlock {
    val intern = JUnicodeBlock.BALINESE
  }

  object Sundanese extends UnicodeBlock {
    val intern = JUnicodeBlock.SUNDANESE
  }

  object Batak extends UnicodeBlock {
    val intern = JUnicodeBlock.BATAK
  }

  object Lepcha extends UnicodeBlock {
    val intern = JUnicodeBlock.LEPCHA
  }

  object OlChiki extends UnicodeBlock {
    val intern = JUnicodeBlock.OL_CHIKI
  }

  object VedicExtensions extends UnicodeBlock {
    val intern = JUnicodeBlock.VEDIC_EXTENSIONS
  }

  object PhoneticExtensionsSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.PHONETIC_EXTENSIONS_SUPPLEMENT
  }

  object CombiningDiacriticalMarksSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.COMBINING_DIACRITICAL_MARKS_SUPPLEMENT
  }

  object Glagolitic extends UnicodeBlock {
    val intern = JUnicodeBlock.GLAGOLITIC
  }

  object LatinExtendedC extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_EXTENDED_C
  }

  object Coptic extends UnicodeBlock {
    val intern = JUnicodeBlock.COPTIC
  }

  object GeorgianSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.GEORGIAN_SUPPLEMENT
  }

  object Tifinagh extends UnicodeBlock {
    val intern = JUnicodeBlock.TIFINAGH
  }

  object EthiopicExtended extends UnicodeBlock {
    val intern = JUnicodeBlock.ETHIOPIC_EXTENDED
  }

  object CyrillicExtendedA extends UnicodeBlock {
    val intern = JUnicodeBlock.CYRILLIC_EXTENDED_A
  }

  object SupplementalPunctuation extends UnicodeBlock {
    val intern = JUnicodeBlock.SUPPLEMENTAL_PUNCTUATION
  }

  object CjkStrokes extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_STROKES
  }

  object Lisu extends UnicodeBlock {
    val intern = JUnicodeBlock.LISU
  }

  object Vai extends UnicodeBlock {
    val intern = JUnicodeBlock.VAI
  }

  object CyrillicExtendedB extends UnicodeBlock {
    val intern = JUnicodeBlock.CYRILLIC_EXTENDED_B
  }

  object Bamum extends UnicodeBlock {
    val intern = JUnicodeBlock.BAMUM
  }

  object ModifierToneLetters extends UnicodeBlock {
    val intern = JUnicodeBlock.MODIFIER_TONE_LETTERS
  }

  object LatinExtendedD extends UnicodeBlock {
    val intern = JUnicodeBlock.LATIN_EXTENDED_D
  }

  object SylotiNagri extends UnicodeBlock {
    val intern = JUnicodeBlock.SYLOTI_NAGRI
  }

  object CommonIndicNumberForms extends UnicodeBlock {
    val intern = JUnicodeBlock.COMMON_INDIC_NUMBER_FORMS
  }

  object PhagsPa extends UnicodeBlock {
    val intern = JUnicodeBlock.PHAGS_PA
  }

  object Saurashtra extends UnicodeBlock {
    val intern = JUnicodeBlock.SAURASHTRA
  }

  object DevanagariExtended extends UnicodeBlock {
    val intern = JUnicodeBlock.DEVANAGARI_EXTENDED
  }

  object KayahLi extends UnicodeBlock {
    val intern = JUnicodeBlock.KAYAH_LI
  }

  object Rejang extends UnicodeBlock {
    val intern = JUnicodeBlock.REJANG
  }

  object HangulJamoExtendedA extends UnicodeBlock {
    val intern = JUnicodeBlock.HANGUL_JAMO_EXTENDED_A
  }

  object Javanese extends UnicodeBlock {
    val intern = JUnicodeBlock.JAVANESE
  }

  object Cham extends UnicodeBlock {
    val intern = JUnicodeBlock.CHAM
  }

  object MyanmarExtendedA extends UnicodeBlock {
    val intern = JUnicodeBlock.MYANMAR_EXTENDED_A
  }

  object TaiViet extends UnicodeBlock {
    val intern = JUnicodeBlock.TAI_VIET
  }

  object EthiopicExtendedA extends UnicodeBlock {
    val intern = JUnicodeBlock.ETHIOPIC_EXTENDED_A
  }

  object MeeteiMayek extends UnicodeBlock {
    val intern = JUnicodeBlock.MEETEI_MAYEK
  }

  object HangulJamoExtendedB extends UnicodeBlock {
    val intern = JUnicodeBlock.HANGUL_JAMO_EXTENDED_B
  }

  object VerticalForms extends UnicodeBlock {
    val intern = JUnicodeBlock.VERTICAL_FORMS
  }

  object AncientGreekNumbers extends UnicodeBlock {
    val intern = JUnicodeBlock.ANCIENT_GREEK_NUMBERS
  }

  object AncientSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.ANCIENT_SYMBOLS
  }

  object PhaistosDisc extends UnicodeBlock {
    val intern = JUnicodeBlock.PHAISTOS_DISC
  }

  object Lycian extends UnicodeBlock {
    val intern = JUnicodeBlock.LYCIAN
  }

  object Carian extends UnicodeBlock {
    val intern = JUnicodeBlock.CARIAN
  }

  object OldPersian extends UnicodeBlock {
    val intern = JUnicodeBlock.OLD_PERSIAN
  }

  object ImperialAramaic extends UnicodeBlock {
    val intern = JUnicodeBlock.IMPERIAL_ARAMAIC
  }

  object Phoenician extends UnicodeBlock {
    val intern = JUnicodeBlock.PHOENICIAN
  }

  object Lydian extends UnicodeBlock {
    val intern = JUnicodeBlock.LYDIAN
  }

  object Kharoshthi extends UnicodeBlock {
    val intern = JUnicodeBlock.KHAROSHTHI
  }

  object OldSouthArabian extends UnicodeBlock {
    val intern = JUnicodeBlock.OLD_SOUTH_ARABIAN
  }

  object Avestan extends UnicodeBlock {
    val intern = JUnicodeBlock.AVESTAN
  }

  object InscriptionalParthian extends UnicodeBlock {
    val intern = JUnicodeBlock.INSCRIPTIONAL_PARTHIAN
  }

  object InscriptionalPahlavi extends UnicodeBlock {
    val intern = JUnicodeBlock.INSCRIPTIONAL_PAHLAVI
  }

  object OldTurkic extends UnicodeBlock {
    val intern = JUnicodeBlock.OLD_TURKIC
  }

  object RumiNumeralSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.RUMI_NUMERAL_SYMBOLS
  }

  object Brahmi extends UnicodeBlock {
    val intern = JUnicodeBlock.BRAHMI
  }

  object Kaithi extends UnicodeBlock {
    val intern = JUnicodeBlock.KAITHI
  }

  object Cuneiform extends UnicodeBlock {
    val intern = JUnicodeBlock.CUNEIFORM
  }

  object CuneiformNumbersAndPunctuation extends UnicodeBlock {
    val intern = JUnicodeBlock.CUNEIFORM_NUMBERS_AND_PUNCTUATION
  }

  object EgyptianHieroglyphs extends UnicodeBlock {
    val intern = JUnicodeBlock.EGYPTIAN_HIEROGLYPHS
  }

  object BamumSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.BAMUM_SUPPLEMENT
  }

  object KanaSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.KANA_SUPPLEMENT
  }

  object AncientGreekMusicalNotation extends UnicodeBlock {
    val intern = JUnicodeBlock.ANCIENT_GREEK_MUSICAL_NOTATION
  }

  object CountingRodNumerals extends UnicodeBlock {
    val intern = JUnicodeBlock.COUNTING_ROD_NUMERALS
  }

  object MahjongTiles extends UnicodeBlock {
    val intern = JUnicodeBlock.MAHJONG_TILES
  }

  object DominoTiles extends UnicodeBlock {
    val intern = JUnicodeBlock.DOMINO_TILES
  }

  object PlayingCards extends UnicodeBlock {
    val intern = JUnicodeBlock.PLAYING_CARDS
  }

  object EnclosedAlphanumericSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.ENCLOSED_ALPHANUMERIC_SUPPLEMENT
  }

  object EnclosedIdeographicSupplement extends UnicodeBlock {
    val intern = JUnicodeBlock.ENCLOSED_IDEOGRAPHIC_SUPPLEMENT
  }

  object MiscellaneousSymbolsAndPictographs extends UnicodeBlock {
    val intern = JUnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS
  }

  object Emoticons extends UnicodeBlock {
    val intern = JUnicodeBlock.EMOTICONS
  }

  object TransportAndMapSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS
  }

  object AlchemicalSymbols extends UnicodeBlock {
    val intern = JUnicodeBlock.ALCHEMICAL_SYMBOLS
  }

  object CjkUnifiedIdeographsExtensionC extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
  }

  object CjkUnifiedIdeographsExtensionD extends UnicodeBlock {
    val intern = JUnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
  }

  /**
   * the List of UnicodeBlock objects.
   *
   * @note the order of this list is exactly the same as
   * {UnicodeBlock.jblocks}
   */
  val blocks = List(
    BasicLatin,
    Latin1Supplement,
    LatinExtendedA,
    LatinExtendedB,
    IpaExtensions,
    SpacingModifierLetters,
    CombiningDiacriticalMarks,
    Greek,
    Cyrillic,
    Armenian,
    Hebrew,
    Arabic,
    Devanagari,
    Bengali,
    Gurmukhi,
    Gujarati,
    Oriya,
    Tamil,
    Telugu,
    Kannada,
    Malayalam,
    Thai,
    Lao,
    Tibetan,
    Georgian,
    HangulJamo,
    LatinExtendedAdditional,
    GreekExtended,
    GeneralPunctuation,
    SuperscriptsAndSubscripts,
    CurrencySymbols,
    CombiningMarksForSymbols,
    LetterlikeSymbols,
    NumberForms,
    Arrows,
    MathematicalOperators,
    MiscellaneousTechnical,
    ControlPictures,
    OpticalCharacterRecognition,
    EnclosedAlphanumerics,
    BoxDrawing,
    BlockElements,
    GeometricShapes,
    MiscellaneousSymbols,
    Dingbats,
    CjkSymbolsAndPunctuation,
    Hiragana,
    Katakana,
    Bopomofo,
    HangulCompatibilityJamo,
    Kanbun,
    EnclosedCjkLettersAndMonths,
    CjkCompatibility,
    CjkUnifiedIdeographs,
    HangulSyllables,
    PrivateUseArea,
    CjkCompatibilityIdeographs,
    AlphabeticPresentationForms,
    ArabicPresentationFormsA,
    CombiningHalfMarks,
    CjkCompatibilityForms,
    SmallFormVariants,
    ArabicPresentationFormsB,
    HalfwidthAndFullwidthForms,
    Specials,
    Syriac,
    Thaana,
    Sinhala,
    Myanmar,
    Ethiopic,
    Cherokee,
    UnifiedCanadianAboriginalSyllabics,
    Ogham,
    Runic,
    Khmer,
    Mongolian,
    BraillePatterns,
    CjkRadicalsSupplement,
    KangxiRadicals,
    IdeographicDescriptionCharacters,
    BopomofoExtended,
    CjkUnifiedIdeographsExtensionA,
    YiSyllables,
    YiRadicals,
    CyrillicSupplementary,
    Tagalog,
    Hanunoo,
    Buhid,
    Tagbanwa,
    Limbu,
    TaiLe,
    KhmerSymbols,
    PhoneticExtensions,
    MiscellaneousMathematicalSymbolsA,
    SupplementalArrowsA,
    SupplementalArrowsB,
    MiscellaneousMathematicalSymbolsB,
    SupplementalMathematicalOperators,
    MiscellaneousSymbolsAndArrows,
    KatakanaPhoneticExtensions,
    YijingHexagramSymbols,
    VariationSelectors,
    LinearBSyllabary,
    LinearBIdeograms,
    AegeanNumbers,
    OldItalic,
    Gothic,
    Ugaritic,
    Deseret,
    Shavian,
    Osmanya,
    CypriotSyllabary,
    ByzantineMusicalSymbols,
    MusicalSymbols,
    TaiXuanJingSymbols,
    MathematicalAlphanumericSymbols,
    CjkUnifiedIdeographsExtensionB,
    CjkCompatibilityIdeographsSupplement,
    Tags,
    VariationSelectorsSupplement,
    SupplementaryPrivateUseAreaA,
    SupplementaryPrivateUseAreaB,
    HighSurrogates,
    HighPrivateUseSurrogates,
    LowSurrogates,
    ArabicSupplement,
    Nko,
    Samaritan,
    Mandaic,
    EthiopicSupplement,
    UnifiedCanadianAboriginalSyllabicsExtended,
    NewTaiLue,
    Buginese,
    TaiTham,
    Balinese,
    Sundanese,
    Batak,
    Lepcha,
    OlChiki,
    VedicExtensions,
    PhoneticExtensionsSupplement,
    CombiningDiacriticalMarksSupplement,
    Glagolitic,
    LatinExtendedC,
    Coptic,
    GeorgianSupplement,
    Tifinagh,
    EthiopicExtended,
    CyrillicExtendedA,
    SupplementalPunctuation,
    CjkStrokes,
    Lisu,
    Vai,
    CyrillicExtendedB,
    Bamum,
    ModifierToneLetters,
    LatinExtendedD,
    SylotiNagri,
    CommonIndicNumberForms,
    PhagsPa,
    Saurashtra,
    DevanagariExtended,
    KayahLi,
    Rejang,
    HangulJamoExtendedA,
    Javanese,
    Cham,
    MyanmarExtendedA,
    TaiViet,
    EthiopicExtendedA,
    MeeteiMayek,
    HangulJamoExtendedB,
    VerticalForms,
    AncientGreekNumbers,
    AncientSymbols,
    PhaistosDisc,
    Lycian,
    Carian,
    OldPersian,
    ImperialAramaic,
    Phoenician,
    Lydian,
    Kharoshthi,
    OldSouthArabian,
    Avestan,
    InscriptionalParthian,
    InscriptionalPahlavi,
    OldTurkic,
    RumiNumeralSymbols,
    Brahmi,
    Kaithi,
    Cuneiform,
    CuneiformNumbersAndPunctuation,
    EgyptianHieroglyphs,
    BamumSupplement,
    KanaSupplement,
    AncientGreekMusicalNotation,
    CountingRodNumerals,
    MahjongTiles,
    DominoTiles,
    PlayingCards,
    EnclosedAlphanumericSupplement,
    EnclosedIdeographicSupplement,
    MiscellaneousSymbolsAndPictographs,
    Emoticons,
    TransportAndMapSymbols,
    AlchemicalSymbols,
    CjkUnifiedIdeographsExtensionC,
    CjkUnifiedIdeographsExtensionD)

  /**
   * the List of the java.lang.Character.UnicodeBlock objects.
   *
   * @note the order of this list is exactly the same as
   * {UnicodeBlock.blocks}
   */
  val jblocks = {
    import JUnicodeBlock._
    List(
      BASIC_LATIN,
      LATIN_1_SUPPLEMENT,
      LATIN_EXTENDED_A,
      LATIN_EXTENDED_B,
      IPA_EXTENSIONS,
      SPACING_MODIFIER_LETTERS,
      COMBINING_DIACRITICAL_MARKS,
      GREEK,
      CYRILLIC,
      ARMENIAN,
      HEBREW,
      ARABIC,
      DEVANAGARI,
      BENGALI,
      GURMUKHI,
      GUJARATI,
      ORIYA,
      TAMIL,
      TELUGU,
      KANNADA,
      MALAYALAM,
      THAI,
      LAO,
      TIBETAN,
      GEORGIAN,
      HANGUL_JAMO,
      LATIN_EXTENDED_ADDITIONAL,
      GREEK_EXTENDED,
      GENERAL_PUNCTUATION,
      SUPERSCRIPTS_AND_SUBSCRIPTS,
      CURRENCY_SYMBOLS,
      COMBINING_MARKS_FOR_SYMBOLS,
      LETTERLIKE_SYMBOLS,
      NUMBER_FORMS,
      ARROWS,
      MATHEMATICAL_OPERATORS,
      MISCELLANEOUS_TECHNICAL,
      CONTROL_PICTURES,
      OPTICAL_CHARACTER_RECOGNITION,
      ENCLOSED_ALPHANUMERICS,
      BOX_DRAWING,
      BLOCK_ELEMENTS,
      GEOMETRIC_SHAPES,
      MISCELLANEOUS_SYMBOLS,
      DINGBATS,
      CJK_SYMBOLS_AND_PUNCTUATION,
      HIRAGANA,
      KATAKANA,
      BOPOMOFO,
      HANGUL_COMPATIBILITY_JAMO,
      KANBUN,
      ENCLOSED_CJK_LETTERS_AND_MONTHS,
      CJK_COMPATIBILITY,
      CJK_UNIFIED_IDEOGRAPHS,
      HANGUL_SYLLABLES,
      PRIVATE_USE_AREA,
      CJK_COMPATIBILITY_IDEOGRAPHS,
      ALPHABETIC_PRESENTATION_FORMS,
      ARABIC_PRESENTATION_FORMS_A,
      COMBINING_HALF_MARKS,
      CJK_COMPATIBILITY_FORMS,
      SMALL_FORM_VARIANTS,
      ARABIC_PRESENTATION_FORMS_B,
      HALFWIDTH_AND_FULLWIDTH_FORMS,
      SPECIALS,
      SYRIAC,
      THAANA,
      SINHALA,
      MYANMAR,
      ETHIOPIC,
      CHEROKEE,
      UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS,
      OGHAM,
      RUNIC,
      KHMER,
      MONGOLIAN,
      BRAILLE_PATTERNS,
      CJK_RADICALS_SUPPLEMENT,
      KANGXI_RADICALS,
      IDEOGRAPHIC_DESCRIPTION_CHARACTERS,
      BOPOMOFO_EXTENDED,
      CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,
      YI_SYLLABLES,
      YI_RADICALS,
      CYRILLIC_SUPPLEMENTARY,
      TAGALOG,
      HANUNOO,
      BUHID,
      TAGBANWA,
      LIMBU,
      TAI_LE,
      KHMER_SYMBOLS,
      PHONETIC_EXTENSIONS,
      MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A,
      SUPPLEMENTAL_ARROWS_A,
      SUPPLEMENTAL_ARROWS_B,
      MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B,
      SUPPLEMENTAL_MATHEMATICAL_OPERATORS,
      MISCELLANEOUS_SYMBOLS_AND_ARROWS,
      KATAKANA_PHONETIC_EXTENSIONS,
      YIJING_HEXAGRAM_SYMBOLS,
      VARIATION_SELECTORS,
      LINEAR_B_SYLLABARY,
      LINEAR_B_IDEOGRAMS,
      AEGEAN_NUMBERS,
      OLD_ITALIC,
      GOTHIC,
      UGARITIC,
      DESERET,
      SHAVIAN,
      OSMANYA,
      CYPRIOT_SYLLABARY,
      BYZANTINE_MUSICAL_SYMBOLS,
      MUSICAL_SYMBOLS,
      TAI_XUAN_JING_SYMBOLS,
      MATHEMATICAL_ALPHANUMERIC_SYMBOLS,
      CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,
      CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT,
      TAGS,
      VARIATION_SELECTORS_SUPPLEMENT,
      SUPPLEMENTARY_PRIVATE_USE_AREA_A,
      SUPPLEMENTARY_PRIVATE_USE_AREA_B,
      HIGH_SURROGATES,
      HIGH_PRIVATE_USE_SURROGATES,
      LOW_SURROGATES,
      ARABIC_SUPPLEMENT,
      NKO,
      SAMARITAN,
      MANDAIC,
      ETHIOPIC_SUPPLEMENT,
      UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED,
      NEW_TAI_LUE,
      BUGINESE,
      TAI_THAM,
      BALINESE,
      SUNDANESE,
      BATAK,
      LEPCHA,
      OL_CHIKI,
      VEDIC_EXTENSIONS,
      PHONETIC_EXTENSIONS_SUPPLEMENT,
      COMBINING_DIACRITICAL_MARKS_SUPPLEMENT,
      GLAGOLITIC,
      LATIN_EXTENDED_C,
      COPTIC,
      GEORGIAN_SUPPLEMENT,
      TIFINAGH,
      ETHIOPIC_EXTENDED,
      CYRILLIC_EXTENDED_A,
      SUPPLEMENTAL_PUNCTUATION,
      CJK_STROKES,
      LISU,
      VAI,
      CYRILLIC_EXTENDED_B,
      BAMUM,
      MODIFIER_TONE_LETTERS,
      LATIN_EXTENDED_D,
      SYLOTI_NAGRI,
      COMMON_INDIC_NUMBER_FORMS,
      PHAGS_PA,
      SAURASHTRA,
      DEVANAGARI_EXTENDED,
      KAYAH_LI,
      REJANG,
      HANGUL_JAMO_EXTENDED_A,
      JAVANESE,
      CHAM,
      MYANMAR_EXTENDED_A,
      TAI_VIET,
      ETHIOPIC_EXTENDED_A,
      MEETEI_MAYEK,
      HANGUL_JAMO_EXTENDED_B,
      VERTICAL_FORMS,
      ANCIENT_GREEK_NUMBERS,
      ANCIENT_SYMBOLS,
      PHAISTOS_DISC,
      LYCIAN,
      CARIAN,
      OLD_PERSIAN,
      IMPERIAL_ARAMAIC,
      PHOENICIAN,
      LYDIAN,
      KHAROSHTHI,
      OLD_SOUTH_ARABIAN,
      AVESTAN,
      INSCRIPTIONAL_PARTHIAN,
      INSCRIPTIONAL_PAHLAVI,
      OLD_TURKIC,
      RUMI_NUMERAL_SYMBOLS,
      BRAHMI,
      KAITHI,
      CUNEIFORM,
      CUNEIFORM_NUMBERS_AND_PUNCTUATION,
      EGYPTIAN_HIEROGLYPHS,
      BAMUM_SUPPLEMENT,
      KANA_SUPPLEMENT,
      ANCIENT_GREEK_MUSICAL_NOTATION,
      COUNTING_ROD_NUMERALS,
      MAHJONG_TILES,
      DOMINO_TILES,
      PLAYING_CARDS,
      ENCLOSED_ALPHANUMERIC_SUPPLEMENT,
      ENCLOSED_IDEOGRAPHIC_SUPPLEMENT,
      MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS,
      EMOTICONS,
      TRANSPORT_AND_MAP_SYMBOLS,
      ALCHEMICAL_SYMBOLS,
      CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C,
      CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D)
  }

  private[this] val blocksMap = {
    HashMap[JUnicodeBlock, UnicodeBlock]((jblocks zip blocks): _*)
  }

  /**
   * Return the UnicodeBlock instance representing the Unicode block that
   * contains the specified CodePoint.
   */
  def apply(codePoint: CodePoint): UnicodeBlock = {
    blocksMap(JUnicodeBlock.of(codePoint.intern))
  }
}

abstract class UnicodeBlock {
  protected val intern: JUnicodeBlock

  override def equals(that: Any): Boolean = that match {
    case that: UnicodeBlock => this.intern == that.intern
    case _ => false
  }
}

