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
package aprikot.text.json4s

import scala.reflect.runtime.universe._

import org.scalatest._

import org.json4s._
import org.json4s.jackson._
import JsonDSL._
import JsonMethods._
import Serialization.{ read, write }

object ADTSerializerSpec {
  case class SomeCaseClass(x: String)
}

class ADTSerializerSpec extends FunSpec {
  self =>

  import ADTSerializerSpec._

  describe("ADTSerializerSpec#serialize") {
    it("""should serialize case class that has only one val to the JSON text
        |  formatted as "{"<class name>":"<value>"}"""".stripMargin) {
      implicit val formats = Serialization.formats(NoTypeHints) +
        new ADTSerializer[SomeCaseClass]
      val ser = parse(write(SomeCaseClass("some string")))
      assert(ser === JObject(JField("SomeCaseClass", "some string") :: Nil))
    }
  }

  describe("ADTSerializer#deserialize") {
    it("""should deserialize the JSON text formatted as
        |  "{"<class name>":"<value>"} to the case class that has only
        | one val"""".stripMargin) {
      implicit val formats = Serialization.formats(NoTypeHints) +
        new ADTSerializer[SomeCaseClass]
      val ret = read[SomeCaseClass]("""{"SomeCaseClass":"some text"}""")
      assert(ret === SomeCaseClass("some text"))
    }
  }
}