/**
 * Copyright (C) 2013 Patricio Petruzzi <p.petruzzi12 [at] imperial [dot] ac [dot] uk>
 *
 * FlexibleDemand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FlexibleDemand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with FlexibleDemand. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.imperial.flexibledemand.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.sc.knowledge.Module;
import uk.ac.imperial.presage2.sc.knowledge.SocialKnowledge;
import uk.ac.imperial.presage2.sc.modules.favor.FavorModuleInterface;
import uk.ac.imperial.presage2.sc.modules.favor.interaction.FavorDone;
import uk.ac.imperial.presage2.sc.modules.favor.interaction.FavorRequested;
import uk.ac.imperial.presage2.util.fsm.FSMException;

/**
 * Agent of the system which consumes electricity
 * Exchanges allocations when is best for him or when helps a "friend"
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public final class SocialConsumer extends AbstractConsumer {

    SocialKnowledge scKnow;
    FavorModuleInterface favMod;

    /**
     * Constructor
     *
     * @param id a {@link java.util.UUID} object.
     * @param name a {@link java.lang.String} object.
     */
    public SocialConsumer(UUID id, String name) {
        super(id, name);

        List<Module> modules = new ArrayList<Module>();
        modules.add(Module.FAVOR);
        scKnow = new SocialKnowledge(modules);
        favMod = (FavorModuleInterface) scKnow.getModule(Module.FAVOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean acceptOffer(UUID exchangeAdId, UUID initiator,
            List<Allocation> offer) {
        boolean interested, accept;
        long favorsDone, favorsReceived;

        // I can accept this offer
        if (!super.acceptOffer(exchangeAdId, initiator, offer)) {
            return false;
        }

        // The offer's allocation time is not in our preference
        interested = ad.getEmptyPreference(myAllocations).contains(offer.get(0).getAllocationTime());

        favorsDone = favMod.getFavors(getID(), initiator);
        favorsReceived = favMod.getFavors(initiator, getID());

        if (interested) {
            scKnow.addSocialInteraction(new FavorDone(initiator, getID()));
            return true;
        }

        if (favorsDone - favorsReceived - Math.sqrt(favorsDone) + 1 < 0) {
            scKnow.addSocialInteraction(new FavorDone(getID(), initiator));
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void offerAccepted(UUID exchangeAdId, UUID initiator, List<Allocation> offer) {
        super.offerAccepted(exchangeAdId, initiator, offer);

        // get the ad
        ConsumptionExchangeAd cea = adsHelper.getOfferedAd(exchangeAdId);

        // Add the favor
        scKnow.addSocialInteraction(new FavorDone(cea.getOwner(), getID()));

    }

    @Override
    public String toString() {
        return "Social " + this.getName();
    }
}
