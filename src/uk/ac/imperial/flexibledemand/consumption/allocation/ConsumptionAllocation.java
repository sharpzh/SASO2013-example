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

import java.util.UUID;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;

/**
 * Represents an amount of energy allocated
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ConsumptionAllocation extends Allocation {

    private double amount;

    /**
     * Constructor
     *
     * @param agent owner
     * @param amount amount
     * @param at a {@link uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime} object.
     */
    public ConsumptionAllocation(UUID agent, ProvisionTime at, double amount) {
        super(agent, at);
        this.amount = amount;
    }

    /**
     * Constructor
     *
     * @param agent owner
     * @param amount amount
     * @param id a {@link java.util.UUID} object.
     * @param at a {@link uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime} object.
     */
    protected ConsumptionAllocation(UUID id, UUID agent, ProvisionTime at, double amount) {
        super(id, agent, at);
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public Double getAllocated() {
        return amount;
    }

    /** {@inheritDoc} */
    @Override
    public ConsumptionAllocation clone() {
        return new ConsumptionAllocation(this.id, this.getOwner(), this.getAllocationTime(), amount);
    }
}
