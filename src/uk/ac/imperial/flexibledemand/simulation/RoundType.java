/**
 *  Copyright (C) 2013 Patricio Petruzzi <p.petruzzi12 [at] imperial [dot] ac [dot] uk>
 *
 *  FlexibleDemand is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  FlexibleDemand is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with FlexibleDemand.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.imperial.flexibledemand.simulation;

/**
 * Enumeration with all the possible rounds
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public enum RoundType {

    /**
     * Initial
     */
    INIT,
    /**
     * Agents demand electricity
     */
    DEMAND,
    /**
     * Agents self adapt
     */
    ADAPT
}
