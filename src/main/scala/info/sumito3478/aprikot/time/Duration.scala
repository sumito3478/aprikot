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
package info.sumito3478.aprikot.time

// TODO: Use a 128-bit integer to represent a time duration.

class Duration(nanos: Long) {
  def toNanos: Long = nanos

  def toMillis: Long = {
    toNanos / 1000
  }

  def toMicros: Long = {
    toMillis / 1000
  }

  def toSeconds: Long = {
    toSeconds / 1000
  }

  def toMinutes: Long = {
    toSeconds / 60
  }

  def toHours: Long = {
    toMinutes / 60
  }

  def toDays: Long = {
    toHours / 24
  }
}

object Duration {

  def nanos(x: Long): Duration = {
    new Duration(x)
  }

  def millis(x: Long): Duration = {
    nanos(x * 1000)
  }

  def micros(x: Long): Duration = {
    millis(x * 1000)
  }

  def seconds(x: Long): Duration = {
    micros(x * 1000)
  }

  def seconds(s: Long, n: Long): Duration = {
    nanos(seconds(s).toNanos + n)
  }

  def minutes(x: Long): Duration = {
    seconds(x * 60)
  }

  def hours(x: Long): Duration = {
    minutes(x * 60)
  }

  def days(x: Long): Duration = {
    hours(x * 24)
  }
}