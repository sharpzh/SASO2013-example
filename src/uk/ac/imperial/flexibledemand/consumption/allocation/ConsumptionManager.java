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

import uk.ac.imperial.flexibledemand.consumption.allocation.allocator.ConsumptionDumbAllocator;
import uk.ac.imperial.presage2.alloc.allocator.AllocatorInterface;
import uk.ac.imperial.presage2.alloc.manager.AllocationManager;

/**
 * Implementation of the AllocationManager
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ConsumptionManager extends AllocationManager{

    /** {@inheritDoc} */
    @Override
    protected AllocatorInterface getDefaultAllocator() {
        return new ConsumptionDumbAllocator();
    }

}
