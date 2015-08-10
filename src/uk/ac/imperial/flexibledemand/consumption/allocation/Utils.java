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
package uk.ac.imperial.flexibledemand.consumption.allocation;

import java.util.ArrayList;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.AllocationListInterface;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;

/**
 * Utilities to perform with allocations
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class Utils {
   
    /**
     * <p>Return the amount over a list of ConsumptionAllocations</p>
     *
     * @param list a {@link java.util.ArrayList} object.
     * @return a double.
     */
    public static double getAllocationsAmount(ArrayList<Allocation> list) {
        double total = 0;
        
        for(Allocation a:list){
            if(a instanceof ConsumptionAllocation){
                ConsumptionAllocation cs = (ConsumptionAllocation)a;
                total += cs.getAllocated();
            }
        }
        
        return total;
    }

    /**
     * <p>Return the average of all the allocations</p>
     *
     * @param demand a {@link uk.ac.imperial.presage2.alloc.allocation.AllocationListInterface} object.
     * @return a double.
     */
    public static double getAverage(AllocationListInterface demand) {
         ArrayList<ProvisionTime> list = demand.getAllAllocationTimes();
         double total=0;
         
         for(ProvisionTime at:list){
             total += getAllocationsAmount(demand.getAllocationsAtTime(at));
         }
         return total/list.size();
    }

}
