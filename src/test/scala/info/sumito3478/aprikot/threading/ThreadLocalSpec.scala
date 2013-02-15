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
package aprikot.threading

import org.scalatest._

class ThreadLocalSpec extends FunSpec {
  describe("ThreadLocal#apply") {
    it("returns specific value for the current thread.") {
      val count = new java.util.concurrent.atomic.AtomicInteger(0)
      val local = ThreadLocal[Int] {
        count.incrementAndGet()
      }
      val threads = 0 until 100 map {
        i =>
          new Thread {
            override def run: Unit = {
              val l = local()
            }
          }
      }
      threads foreach (_.start)
      threads foreach (_.join)
      assert(count.get() == 100)
    }
  }
}