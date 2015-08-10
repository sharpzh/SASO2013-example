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
 * Constants Definitions
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public final class SimulationConst {
    
    private static int NUMBER_OF_SLOTS = 24;

    private static final double DEVIATION_OF_DEMAND = 2;
    
    /**
     * <p>number of available slots</p>
     *
     * @return a int.
     */
    public static int getNUMBER_OF_SLOTS() {
        return NUMBER_OF_SLOTS;
    }

    /**
     * <p>Accepted deviation of demand</p>
     *
     * @return a double.
     */
    public static double getDEVIATION_OF_DEMAND() {
        return DEVIATION_OF_DEMAND;
    }
    
    
    
    

}
