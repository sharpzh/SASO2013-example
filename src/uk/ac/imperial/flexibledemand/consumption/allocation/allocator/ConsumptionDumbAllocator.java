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
package uk.ac.imperial.flexibledemand.consumption.allocation.allocator;

import java.util.ArrayList;
import java.util.List;
import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionDemand;
import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionAllocation;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.Demand;
import uk.ac.imperial.presage2.alloc.allocator.AllocatorInterface;

/**
 * Allocator that allocates what is demanded
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ConsumptionDumbAllocator implements AllocatorInterface {

    /** {@inheritDoc} */
    @Override
    public List<Allocation> allocate(List<Demand> demands) {
        List<Allocation> list = new ArrayList<Allocation>();
        for (Demand d : demands) {
            if (d instanceof ConsumptionDemand) {
                ConsumptionDemand cd = (ConsumptionDemand) d;
                list.add(new ConsumptionAllocation(d.getAgent(), d.getProvisionTime(), cd.getAmount()));
            }
        }
        return list;
    }
}
