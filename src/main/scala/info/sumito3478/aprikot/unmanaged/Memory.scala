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
package aprikot.unmanaged

import scala.concurrent.util.Unsafe.{ instance => _unsafe }

import java.nio._

import org.bridj.{ Pointer => BPointer }

/**
 * A trait represents a memory block.
 *
 * Use Memory(Long) to allocate the memory block with the default
 * implementation.
 *
 * Example Usage:
 * {{{
 * import aprikot.io.{Memory, using, le}
 *
 * using(Memory(4)) {
 *   block =>
 *     var p = block.pointer
 *     p.int(le(0xcafebabe))
 *     println(f"0x${p.byte}") // => 0xbe
 *     p += 1
 *     println(f"0x${p.byte}") // => 0xba
 *     p += 1
 *     println(f"0x${p.byte}") // => 0xfe
 *     p += 1
 *     println(f"0x${p.byte}") // => 0xca
 * }
 * }}}
 */
trait Memory extends Disposable {
  /**
   * Get the pointer that points to the beginning of the memory block.
   */
  def pointer: Pointer

  /**
   * Returns the length of memory in bytes.
   */
  def length: Long

  /**
   * Returns the ByteBuffer view of this memory.
   */
  lazy val byteBuffer: ByteBuffer = {
    pointer.byteBuffer(length)
  }
}

object Memory {
  /**
   * Allocates a memory block with the given size and return the instance of
   * [[Memory]] that references to that block.
   *
   * @note Current implementation allocates the memory with
   *   [[com.sun.jna.Native.malloc]] and deallocates it with
   *   [[com.sun.jna.Native.free]].
   *
   *   The instance that this method returns has a finalizer that deallocate
   *   the unmanaged memory. This is necessary for the thread local unmanaged
   *   memory.
   */
  def apply(size: Long): Memory = {
    val bpointer = BPointer.allocateBytes(size)
    val ptr = bpointer.getPeer
    new Memory {
      def pointer: Pointer = {
        //Pointer(ptr)
        new Pointer(ptr)
      }

      def disposeInternal: Unit = {
        bpointer.release
      }

      override def finalize: Unit = {
        dispose
      }

      def length = size
    }
  }
}