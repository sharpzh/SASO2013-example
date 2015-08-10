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
package uk.ac.imperial.flexibledemand.agents;

import java.util.List;
import java.util.UUID;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;

/**
 * Agent of the system which consumes electricity
 * Only exchanges an allocation when it best for him
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public final class SelfishConsumer extends AbstractConsumer {

    /**
     * Constructor
     *
     * @param id a {@link java.util.UUID} object.
     * @param name a {@link java.lang.String} object.
     */
    public SelfishConsumer(UUID id, String name) {
        super(id, name);
    }

    

    /** {@inheritDoc} */
    @Override
    public synchronized boolean acceptOffer(UUID exchangeAdId, UUID initiator,
            List<Allocation> offer) {
        
        boolean interested, accept;
        
        // I can accept this offer
        accept = super.acceptOffer(exchangeAdId, initiator, offer);
        
        // This offer is in my preference
        interested = ad.getEmptyPreference(myAllocations).contains(offer.get(0).getAllocationTime());
        
        
        return accept && interested;
    }

    @Override
    public String toString(){
        return "Selfish "+this.getName();
    }
            
}
