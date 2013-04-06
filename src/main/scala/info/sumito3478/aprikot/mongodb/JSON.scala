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
package aprikot.mongodb

import com.mongodb.casbah.commons._
import com.mongodb.casbah.commons.Implicits._

import com.mongodb._
import com.mongodb.util.{ JSON => underlined }

object JSON {
  def parse(json: String): MongoDBObject = {
    if (json.length == 0) {
      MongoDBObject.empty
    } else {
      underlined.parse(json) match {
        case obj: DBObject => new MongoDBObject(obj)
        case obj =>
          sys.error(
            s"unknown return value from com.mongodb.util.JSON: ${obj}: ${obj.getClass}")
      }
    }
  }
}