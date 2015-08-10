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
package uk.ac.imperial.flexibledemand.consumption.exchange.ads;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionAllocation;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd;

/**
 * Implementation of the ExchangeAd for Consumption Allocation
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ConsumptionExchangeAd extends ExchangeAd {

    private List<Allocation> offer;

    /**
     * <p>Constructor for ConsumptionExchangeAd.</p>
     *
     * @param owner a {@link java.util.UUID} object.
     * @param offer a {@link java.util.List} object.
     */
    public ConsumptionExchangeAd(UUID owner, List<Allocation> offer) {
        super(owner);
        this.offer = offer;
    }

    /**
     * <p>Constructor for ConsumptionExchangeAd.</p>
     *
     * @param owner a {@link java.util.UUID} object.
     * @param offer a {@link uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionAllocation} object.
     */
    public ConsumptionExchangeAd(UUID owner, ConsumptionAllocation offer) {
        super(owner);
        this.offer = new ArrayList<Allocation>();
        this.offer.add(offer);
    }

    private ConsumptionExchangeAd(UUID id, UUID owner, List<Allocation> offer) {
        super(id,owner);
        this.offer = offer;
    }

    /**
     * <p>Getter for the <code>offer</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Allocation> getOffer() {
        return offer;
    }

    /** {@inheritDoc} */
    @Override
    public ExchangeAd clone() {
        return new ConsumptionExchangeAd(id,owner,offer);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ConsumptionExchangeAd){
            ConsumptionExchangeAd cea = (ConsumptionExchangeAd)o;
            return id.equals(cea.id);
        }
        
        return false;
    }
}
