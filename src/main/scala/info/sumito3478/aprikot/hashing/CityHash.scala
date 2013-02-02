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
package info.sumito3478.aprikot.hashing

import info.sumito3478.aprikot.numeric.Cent
import info.sumito3478.aprikot.unsafe.Pointer
import info.sumito3478.aprikot.math._
import info.sumito3478.aprikot.numeric.MutableCent

/**
 * CityHash v1.1 implementation.
 *
 * @note This implementation is tested only on my x86_64 machine.
 * I think it also works on big-endian system, but not is tested at all
 * and may have bugs.
 *
 * @note The original CityHash implementation doen't maintain backward
 * compatibility. This implementation is compatible to CityHash v1.1, and
 * not to CityHash v1.0.
 */
object CityHash {
  val kMul: Long = 0x9ddfea08eb382d69L

  def hash128to64(u: Long, v: Long): Long = {
    var a = (u ^ v) * kMul
    a ^= (a >>> 47)
    var b: Long = (v ^ a) * kMul
    b ^= (b >>> 47)
    b *= kMul
    b
  }

  def hash128to64(x: Cent): Long = {
    hash128to64(x.low, x.high)
  }

  /**
   * Some primes between 2^63 and 2^64 for various uses.
   */
  object k {
    val _0: Long = 0xc3a5c85c97cb3127L
    val _1: Long = 0xb492b66fbe98f273L
    val _2: Long = 0x9ae16a3b2f90404fL
  }

  /**
   * Magic numbers for 32bit hashing. Copied from Murmur3.
   */
  object c {
    val _1: Int = 0xcc9e2d51
    val _2: Int = 0x1b873593
  }

  /**
   * A 32-bit to 32-bit integer hash copied from Murmur3.
   */
  def fmix(x: Int): Int = {
    var h = x
    h ^= h >>> 16
    h *= 0x85ebca6b
    h ^= h >>> 13
    h *= 0xc2b2ae35
    h ^= h >>> 16
    h
  }

  def rotate32(x: Int, shift: Int): Int = {
    if (shift == 0) x else ((x >>> shift) | (x << (32 - shift)))
  }

  /**
   * Helper from Murmur3 for combining two 32bit values.
   */
  def mur(x: Int, y: Int): Int = {
    var a = x
    var h = y
    a *= c._1
    a = rotate32(a, 17)
    a *= c._2
    h ^= a
    h = rotate32(h, 19)
    h * 5 + 0xe6546b64
  }

  def rotate(x: Long, shift: Int): Long = {
    if (shift == 0) x else ((x >>> shift) | (x << (64 - shift)))
  }

  def shiftMix(x: Long): Long = {
    x ^ (x >>> 47)
  }

  def hashLen16(u: Long, v: Long): Long = {
    hash128to64(u, v)
  }

  def hashLen16(u: Long, v: Long, mul: Long): Long = {
    // Murmur-inspired hashing.
    var a = (u ^ v) * mul
    a ^= (a >>> 47)
    var b = (v ^ a) * mul
    b ^= (b >>> 47)
    b *= mul
    b
  }

  def hashLen0to16(s: Pointer, len: Int): Long = {
    len match {
      case len if len >= 8 => {
        val mul = k._2 + len * 2
        val a = s.longLE + k._2
        val b = (s + len - 8).longLE
        val c = rotate(b, 37) * mul + a
        val d = (rotate(a, 25) + b) * mul
        hashLen16(c, d, mul)
      }
      case len if len >= 4 => {
        val mul = k._2 + len * 2
        val a = s.intLE
        hashLen16(len + (a << 3), (s + len - 4).intLE, mul)
      }
      case len if len > 0 => {
        val a = s.byte
        val b = (s + (len >>> 1)).byte
        val c = (s + len - 1).byte
        val y = a.toInt + (b.toInt << 8)
        val z = len + (c.toInt << 2)
        shiftMix(y * k._2 ^ z * k._0) * k._2
      }
      case _ => k._2
    }
  }

  def hashLen17to32(s: Pointer, len: Int): Long = {
    val mul = k._2 + len * 2
    val a = s.longLE * k._1
    val b = (s + 8).longLE
    val c = (s + len - 8).longLE * mul
    val d = (s + len - 16).longLE * k._2
    hashLen16(rotate(a + b, 43) + rotate(c, 30) + d, a + rotate(b + k._2, 18) + c, mul)
  }

  /**
   * Return a 16-byte hash for 48 bytes. Quick and dirty.
   *
   * @note Callers do best to use "random-looking" values for a and b.
   */
  def weakHashLen32WithSeeds(w: Long, x: Long, y: Long, z: Long, a0: Long, b0: Long): Cent = {
    var a = a0
    var b = b0
    a += w
    b = rotate(b + a + z, 21)
    val c = a
    a += x
    a += y
    b += rotate(a, 44)
    new Cent(a + z, b + c)
  }

  /**
   * Return a 16-byte hash for xs.get(0) ... xs.get(31), a, and b. Quick and dirty.
   */
  def weakHashLen32WithSeeds(s: Pointer, a: Long, b: Long): Cent = {
    weakHashLen32WithSeeds(s.longLE,
      (s + 8).longLE,
      (s + 16).longLE,
      (s + 24).longLE,
      a,
      b)
  }

  /**
   * Return an 8-byte hash for 33 to 64 bytes.
   */
  def hashLen33to64(s: Pointer, len: Int): Long = {
    val mul = k._2 + len * 2
    var a = s.longLE * k._2
    var b = (s + 8).longLE
    val c = (s + len - 24).longLE
    val d = (s + len - 32).longLE
    val e = (s + 16).longLE * k._2
    val f = (s + 24).longLE * 9
    val g = (s + len - 8).longLE
    val h = (s + len - 16).longLE * mul
    val u = rotate(a + g, 43) + (rotate(b, 30) + c) * 9
    val v = ((a + g) ^ d) + f + 1
    val w = ((u + v) * mul).bswap + h
    val x = rotate(e + f, 42) + c
    val y = (((v + w) * mul).bswap + g) * mul
    val z = e + f + c
    a = ((x + z) * mul + y).bswap + b
    b = shiftMix((z + a) * mul + d + h) * mul
    b + x
  }

  def cityHash64(_s: Pointer, _len: Int): Long = {
    var s = _s
    var len = _len
    len match {
      case _ if len <= 16 => hashLen0to16(s, len)
      case _ if len <= 32 => hashLen17to32(s, len)
      case _ if len <= 64 => hashLen33to64(s, len)
      case _ => {
        var x = (s + len - 40).longLE
        var y = (s + len - 16).longLE + (s + len - 56).longLE
        var z = hashLen16((s + len - 48).longLE + len, (s + len - 24).longLE)
        var v = weakHashLen32WithSeeds(s + len - 64, len, z)
        var w = weakHashLen32WithSeeds(s + len - 32, y + k._1, x)
        x = x * k._1 + s.longLE
        len = (len - 1) & (~63)
        do {
          x = rotate(x + y + v.low + (s + 8).longLE, 37) * k._1
          y = rotate(y + v.high + (s + 48).longLE, 42) * k._1
          x ^= w.high
          y += v.low + (s + 40).longLE
          z = rotate(z + w.low, 33) * k._1
          v = weakHashLen32WithSeeds(s, v.high * k._1, x + w.low)
          w = weakHashLen32WithSeeds(s + 32, z + w.high, y + (s + 16).longLE)
          val tmp = z
          z = x
          x = tmp
          s += 64
          len -= 64
        } while (len != 0)
        hashLen16(hashLen16(v.low, w.low) + shiftMix(y) * k._1 + z, hashLen16(v.high, w.high) + x)
      }
    }
  }

  def cityHash64(x: Byte): Long = {
    // copied from hashLen0to16
    val a, b, c = x
    val y = a.toInt + (b.toInt << 8)
    val z = 1 + (c.toInt << 2)
    shiftMix(y * k._2 ^ z * k._0) * k._2
  }

  def cityHash64(x: Short): Long = {
    // copied from hashLen0to16
    val _x = x.toLE
    val a = _x >>> 8
    val b, c = _x & 0xff
    val y = a.toInt + (b.toInt << 8)
    val z = 2 + (c.toInt << 2)
    shiftMix(y * k._2 ^ z * k._0) * k._2
  }

  def cityHash64(x: Int): Long = {
    // copied from hashLen0to16
    val _x = x.toLE
    val mul = k._2 + 4 * 2
    val a = _x
    hashLen16(4 + (a << 3), _x, mul)
  }

  def cityHash64(x: Long): Long = {
    // copied from hashLen0to16
    val _x = x.toLE
    val mul = k._2 + 8 * 2
    val a = _x + k._2
    val b = _x
    val c = rotate(b, 37) * mul + a
    val d = (rotate(a, 25) + b) * mul
    hashLen16(c, d, mul)
  }

  def cityHash64(x: Float): Long = {
    cityHash64(x.toBits)
  }

  def cityHash64(x: Double): Long = {
    cityHash64(x.toBits)
  }

  def cityHash64WithSeed(s: Pointer, len: Int, seed: Long): Long = {
    cityHash64WithSeeds(s, len, k._2, seed)
  }

  def cityHash64WithSeeds(s: Pointer, len: Int, seed0: Long, seed1: Long) = {
    hashLen16(cityHash64(s, len) - seed0, seed1)
  }

  /**
   * A subroutine for cityHash128. Returns a decent 128-bit hash for strings
   * of any length representable in signed 32-bit integer. Based on City and Murmur.
   */
  def cityMurmur(_s: Pointer, len: Int, seed: Cent): Cent = {
    var s = _s
    var a = seed.low
    var b = seed.high
    var c = 0L
    var d = 0L
    var l = len - 16
    if (l <= 0) {
      a = shiftMix(a * k._1) * k._1
      c = b * k._1 + hashLen0to16(s, len)
      d = shiftMix(a + (if (len >= 8) s.longLE else c))
    } else {
      c = hashLen16((s + len - 8).longLE + k._1, a)
      d = hashLen16(b + len, c + (s + len - 16).longLE)
      a += d
      do {
        a ^= shiftMix(s.longLE * k._1) * k._1
        a *= k._1
        b ^= a
        c ^= shiftMix((s + 8).longLE * k._1) * k._1
        c *= k._1
        d ^= c
        s += 16
        l -= 16
      } while (l > 0)
    }
    a = hashLen16(a, c)
    b = hashLen16(d, b)
    new Cent(a ^ b, hashLen16(b, a))
  }

  def cityHash128WithSeed(_s: Pointer, length: Int, seed: Cent): Cent = {
    var len = length
    if (len < 128) {
      cityMurmur(_s, len, seed)
    } else {
      var s = _s
      var v = new MutableCent(0, 0)
      var w = new MutableCent(0, 0)
      var x = seed.low
      var y = seed.high
      var z = len * k._1
      v.low = rotate(y ^ k._1, 49) * k._1 + s.longLE
      v.high = rotate(v.low, 42) * k._1 + (s + 8).longLE
      w.low = rotate(y + z, 35) * k._1 + x
      w.high = rotate(x + (s + 88).longLE, 53) * k._1
      do {
        x = rotate(x + y + v.low + (s + 8).longLE, 37) * k._1
        y = rotate(y + v.high + (s + 48).longLE, 42) * k._1
        x ^= w.high
        y += v.low + (s + 40).longLE
        z = rotate(z + w.low, 33) * k._1
        v = MutableCent(weakHashLen32WithSeeds(s, v.high * k._1, x + w.low))
        w = MutableCent(weakHashLen32WithSeeds(s + 32, z + w.high, y + (s + 16).longLE))
        var tmp = z
        z = x
        x = tmp
        s += 64
        x = rotate(x + y + v.low + (s + 8).longLE, 37) * k._1
        y = rotate(y + v.high + (s + 48).longLE, 42) * k._1
        x ^= w.high
        y += v.low + (s + 40).longLE
        z = rotate(z + w.low, 33) * k._1
        v = MutableCent(weakHashLen32WithSeeds(s, v.high * k._1, x + w.low))
        w = MutableCent(weakHashLen32WithSeeds(s + 32, z + w.high, y + (s + 16).longLE))
        tmp = z
        z = x
        x = tmp
        s += 64
        len -= 128
      } while (len >= 128)
      x += rotate(v.low + z, 49) * k._0
      y = y * k._0 + rotate(w.high, 37)
      z = z * k._0 + rotate(w.low, 27)
      w.low *= 9
      v.low *= k._0
      var tail_done = 0
      while (tail_done < len) {
        tail_done += 32
        y = rotate(x + y, 42) * k._0 + v.high
        w.low += (s + len - tail_done + 16).longLE
        x = x * k._0 + w.low
        z += w.high + (s + len - tail_done).longLE
        w.high += v.low
        v = MutableCent(weakHashLen32WithSeeds(s + len - tail_done, v.low + z, v.high))
        v.low *= k._0
      }
      x = hashLen16(x, v.low)
      y = hashLen16(y + z, w.low)
      new Cent(hashLen16(x + v.high, w.high) + y, hashLen16(x + w.high, y + v.high))
    }
  }

  def cityHash128(s: Pointer, len: Int): Cent = {
    if (len >= 16) {
      cityHash128WithSeed(s + 16, len - 16, new Cent(s.longLE, (s + 8).longLE + k._0))
    } else {
      cityHash128WithSeed(s, len, new Cent(k._0, k._1))
    }
  }
}