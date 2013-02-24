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

import scala.reflect._
import scala.reflect.api._
import scala.reflect.runtime.universe._

import org.json4s._
import JsonDSL._

import aprikot._

class ADTSerializer[A <: { def x: Any }: ClassTag](
  outerClass: Option[Any] = None)(
    implicit ttag: TypeTag[A]) extends Serializer[A] {

  private[this] val c = implicitly[ClassTag[A]].runtimeClass

  private[this] val rm = runtimeMirror(c.getClassLoader)

  private[this] val s = typeOf[A].typeSymbol.asClass

  private[this] val n = s.name.decoded

  private[this] val reflectClass: ClassSymbol => ClassMirror = {
    if (outerClass.isDefined) {
      val o = rm.reflect(outerClass.get)
      s => o.reflectClass(s)
    } else {
      s => rm.reflectClass(s)
    }
  }

  private[this] val ctor = {
    val m = reflectClass(s)
    val ctor = typeOf[A].declaration(nme.CONSTRUCTOR).asMethod
    val method = m.reflectConstructor(ctor)
    arg: Any => method(arg).asInstanceOf[A]
  }

  private[this] val value = {
    val m = reflectClass(s)
    val values = typeOf[A].declarations.flatMap {
      case s: TermSymbol => List(s)
      case _ => Nil
    }.filter(_.isVal)
    val v = values.head
    arg: A => {
      val am = rm.reflect(arg)
      val vm = am.reflectField(v)
      vm.get
    }
  }

  def deserialize(
    implicit format: Formats): PartialFunction[(TypeInfo, JValue), A] = {
    case (TypeInfo(c, _), json) => json match {
      case JObject(JField(name, JString(s)) :: Nil) if (name == n) =>
        ctor(s)
      case _ => throw new MappingException(s"Can't convert ${json} to $c")
    }
  }

  def serialize(implicit format: Formats) = {
    case x: A =>
      JObject(JField(n, value(x).toString))
  }
}