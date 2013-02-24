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
package aprikot

package object pandoc {
  sealed trait Node

  sealed trait ContentElem extends Node

  type Content[T <: ContentElem] = List[T]

  case class PanDoc(val meta: Meta, val content: Content[Block]) extends Node

  case class Meta(
    val title: Content[Inline],
    val authors: List[Content[Inline]],
    val date: Content[Inline]) extends Node

  object Alignment extends Enumeration {
    val AlignLeft, AlignRight, AlignCenter, AlignDefault = Value
  }

  type Alignment = Alignment.Value

  case class ListAttributes(
    val n: Int,
    val style: ListNumberStyle,
    val delim: ListNumberDelim) extends Node

  object ListNumberStyle extends Enumeration {
    val Default = Value
    val Example = Value
    val Decimal = Value
    val LowerRoman = Value
    val UpperRoman = Value
    val LowerAlpha = Value
    val UpperAlpha = Value
  }

  type ListNumberStyle = ListNumberStyle.Value

  object ListNumberDelim extends Enumeration {
    val Default, Period, OneParen, TwoParens = Value
  }

  type ListNumberDelim = ListNumberDelim.Value

  case class Attr(
    val identifier: String,
    val classes: List[String],
    val kvs: List[(String, String)]) extends Node

  object Attr {
    object Empty extends Attr("", List(), List())
  }

  case class TableCell(val content: Content[Block]) extends Node

  case class Format(val name: String) extends Node

  sealed trait Block extends ContentElem

  object Block {
    case class Plain(val content: Content[Inline]) extends Block

    case class Para(val content: Content[Inline]) extends Block

    case class CodeBlock(val attr: Attr, val content: String) extends Block

    case class RawQuote(val format: Format, val content: String) extends Block

    case class BlockQuote(val content: Content[Block]) extends Block

    case class OrderedList(
      val attr: ListAttributes,
      val contents: List[Content[Block]])

    case class BulletList(val contents: List[Content[Block]])

    case class DefinitinoList(
      val contents: List[(Content[Inline], List[Content[Block]])])

    case class Header(
      val level: Int, val attr: Attr, val content: Content[Inline])

    case object HorizontalRule

    case class Table(
      val caption: Content[Inline],
      val alignments: List[Alignment],
      val widths: List[Double],
      val headers: List[TableCell],
      val rows: List[List[TableCell]])
  }

  object QuoteType extends Enumeration {
    val SingleQuote, DoubleQuote = Value
  }

  type QuoteType = QuoteType.Value

  case class Target(val url: String, val title: String) extends Node

  object MathType extends Enumeration {
    val DisplayMath, InlineMath = Value
  }

  type MathType = MathType.Value

  sealed trait Inline extends ContentElem

  object Inline {
    case class Str(val x: String) extends Inline

    case class Emph(val content: Content[Inline]) extends Inline

    case class Strong(val content: Content[Inline]) extends Inline

    case class Strikeout(val content: Content[Inline]) extends Inline

    case class Superscript(val content: Content[Inline]) extends Inline

    case class Subscript(val content: Content[Inline]) extends Inline

    case class SmallCaps(val content: Content[Inline]) extends Inline

    case class Quoted(
      val quoteType: QuoteType,
      val content: Content[Inline]) extends Inline

    case class Cite(
      val citations: List[Citation],
      val content: Content[Inline]) extends Inline

    case class Code(val attr: Attr, val content: String) extends Inline

    case object Space extends Inline

    case object LineBreak extends Inline

    case class Math(val mathType: MathType, val content: String)

    case class RawInline(val format: Format, val content: String)

    case class Link(val content: Content[Inline], val target: Target)

    case class Image(val content: Content[Inline], val target: Target)

    case class Note(val blocks: Content[Block])
  }

  case class Citation(
    val citationId: String,
    val citationPrefix: Content[Inline],
    val citationSuffix: Content[Inline],
    val citationMode: CitationMode,
    val citationNoteNum: Int,
    val citationHash: Int) extends Node

  object CitationMode extends Enumeration {
    val AuthorInText, SuppressAuthor, NormalCitation = Value
  }

  type CitationMode = CitationMode.Value
}