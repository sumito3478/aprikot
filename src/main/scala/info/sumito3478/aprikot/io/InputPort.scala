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
package aprikot.io

import java.nio.charset._

import akka.util._

import aprikot._
import aprikot.control._

trait InputPort {
  def readByte: Option[Byte]

  def read(n: Int): Option[ByteString] = {
    val builder = new ByteStringBuilder
    val b = readByte
    breakable[Option[ByteString]] {
      break =>
        for (_ <- 0 until n) {
          val b = readByte
          if (b.isDefined) {
            val byte = b.get
            builder += byte
          } else {
            val ret = builder.result
            if (ret.size == 0) {
              break(None)
            } else {
              break(Some(ret))
            }
          }
        }
        val ret = builder.result
        if (ret.size == 0) {
          None
        } else {
          Some(ret)
        }
    }
  }

  def readUntil(until: Byte): Option[ByteString] = {
    val builder = new ByteStringBuilder
    val b = readByte
    breakable[Option[ByteString]] {
      break =>
        while (true) {
          val b = readByte
          if (b.isDefined) {
            val byte = b.get
            builder += byte
            if (byte == until) {
              break(Some(builder.result))
            }
          } else {
            val ret = builder.result
            if (ret.size == 0) {
              break(None)
            } else {
              break(Some(ret))
            }
          }
        }
        sys.error("Executed unreachable code.")
    }
  }

  def read: Option[ByteString] = {
    val results = Iterator.continually {
      read(1024)
    }.takeWhile(_.isDefined).map(_.get).toSeq
    if (results.isEmpty) {
      None
    } else {
      Some(results.foldLeft(ByteString.empty)((acc, elem) => acc ++ elem))
    }
  }

  def read(n: Int, charset: Charset): Option[String] = {
    read(n) map {
      bytes =>
        val buffer = bytes.asByteBuffer
        charset.decode(buffer).toString
    }
  }

  def read(charset: Charset): Option[String] = {
    read map {
      bytes =>
        val buffer = bytes.asByteBuffer
        charset.decode(buffer).toString
    }
  }
}