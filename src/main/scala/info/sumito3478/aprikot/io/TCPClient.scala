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
package info.sumito3478.aprikot.io

import java.nio.channels.AsynchronousSocketChannel
import java.net.InetSocketAddress

trait TCPClient {
  def host: String

  def port: Int

  def handle(ctx: TCPContext): Unit

  def start: Unit = {
    val c = AsynchronousSocketChannel.open
    c.connect(new InetSocketAddress(host, port), {
      handle(new TCPContext {
        def channel = c
      })
    })
  }
}