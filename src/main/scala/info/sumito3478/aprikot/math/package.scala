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

package info.sumito3478.aprikot

import java.lang.{ Long => JLong }
import java.lang.{ Integer => JInt }
import java.lang.{ Short => JShort }
import java.lang.{ Byte => JByte }
import java.lang.{ Float => JFloat }
import java.lang.{ Double => JDouble }

package object math {

  implicit class ByteW(val underlined: Byte) extends AnyVal {
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toByte
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Byte to Double failed.")
      ret
    }
  }

  implicit class ShortW(val underlined: Short) extends AnyVal {
    def bswap: Short = JShort.reverseBytes(underlined)
    def toLE: Short = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        underlined
      } else {
        bswap
      }
    }
    def toBE: Short = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        bswap
      } else {
        underlined
      }
    }
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toShort
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Short to Double failed.")
      ret
    }
  }

  implicit class IntW(val underlined: Int) extends AnyVal {
    def bswap: Int = JInt.reverseBytes(underlined)
    def toLE: Int = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        underlined
      } else {
        bswap
      }
    }
    def toBE: Int = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        bswap
      } else {
        underlined
      }
    }
    def toBinaryString: String = JInt.toBinaryString(underlined)
    def toHexString: String = JInt.toHexString(underlined)
    def toOctalString: String = JInt.toOctalString(underlined)
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toInt
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Int to Double failed.")
      ret
    }

    /**
     * Floor-division operator.
     */
    def /->(x: Int) = {
      if (underlined >= 0) underlined / x else ((underlined + 1) / x) - 1
    }

    /**
     * Positive-modulus operator.
     */
    def %->(x: Int) = {
      (underlined % x + x) % x
    }
  }

  implicit class LongW(val underlined: Long) extends AnyVal {
    def bswap: Long = JLong.reverseBytes(underlined)
    def toLE: Long = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        underlined
      } else {
        bswap
      }
    }
    def toBE: Long = {
      if ((0xcafebabe >>> 16) == 0xcafe) {
        bswap
      } else {
        underlined
      }
    }
    def toBinaryString: String = JLong.toBinaryString(underlined)
    def toHexString: String = JLong.toHexString(underlined)
    def toOctalString: String = JLong.toOctalString(underlined)
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toLong
      require(restored == underlined, f"checked cast from 0x${underlined}%x: Long to Double failed.")
      ret
    }

    /**
     * Floor-division operator.
     */
    def /->(x: Long) = {
      if (underlined >= 0) underlined / x else ((underlined + 1) / x) - 1
    }

    /**
     * Positive-modulus operator.
     */
    def %->(x: Long) = {
      (underlined % x + x) % x
    }
  }

  implicit class FloatW(val underlined: Float) extends AnyVal {
    def toBits: Int = JFloat.floatToIntBits(underlined)
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toFloat
      require(restored == underlined, f"checked cast from ${underlined}: Float to Double failed.")
      ret
    }
  }

  implicit class DoubleW(val underlined: Double) extends AnyVal {
    def toBits: Long = JDouble.doubleToLongBits(underlined)
    def toByteChecked: Byte = {
      val ret = underlined.toByte
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Byte failed.")
      ret
    }
    def toShortChecked: Short = {
      val ret = underlined.toShort
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Short failed.")
      ret
    }
    def toIntChecked: Int = {
      val ret = underlined.toInt
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Int failed.")
      ret
    }
    def toLongChecked: Long = {
      val ret = underlined.toLong
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Long failed.")
      ret
    }
    def toFloatChecked: Float = {
      val ret = underlined.toFloat
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Float failed.")
      ret
    }
    def toDoubleChecked: Double = {
      val ret = underlined.toDouble
      val restored = ret.toDouble
      require(restored == underlined, f"checked cast from ${underlined}: Double to Double failed.")
      ret
    }
  }

}