package uk.ac.imperial.flexibledemand.agents.action;

import java.util.Map;
import java.util.UUID;
import uk.ac.imperial.presage2.alloc.actions.AgentAction;
import uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;

/**
 * {Insert class description here}
 *
 * @author Patricio E. Petruzzi
 * @version 
 * @since 
 * 
 */
public class NotifyAllocationStatus extends AgentAction {

    Map<UUID,Boolean> map;

    /**
     * Constructor
     *
     * @param agent a {@link uk.ac.imperial.presage2.util.participant.AbstractParticipant} object.
     * @param ad a {@link uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd} object.
     */
    public NotifyAllocationStatus(AbstractParticipant agent, Map<UUID,Boolean> map) {
        super(agent);
        this.map = map;
    }

    /**
     * {@inheritDoc}
     *
     * toString
     */
    @Override
    public String toString() {
        return "NotifyAllocationStatus ";
    }

    /**
     * Gets the Ad
     *
     * @return a {@link uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd} object.
     */
    public Map<UUID,Boolean> getMap() {
        return map;
    }

}
