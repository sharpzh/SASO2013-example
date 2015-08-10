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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.imperial.flexibledemand.agents.action.NotifyAllocationStatus;
import uk.ac.imperial.flexibledemand.agents.resources.AgentDemand;
import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionAllocation;
import uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd;
import uk.ac.imperial.flexibledemand.service.participant.ParticipantRoundEnvSrv;
import uk.ac.imperial.flexibledemand.simulation.RoundType;
import uk.ac.imperial.presage2.alloc.actions.AddExchangeAdAction;
import uk.ac.imperial.presage2.alloc.actions.DemandAction;
import uk.ac.imperial.presage2.alloc.actions.DoNothingAction;
import uk.ac.imperial.presage2.alloc.actions.ExchangeContractAction;
import uk.ac.imperial.presage2.alloc.actions.RemoveExchangeAdAction;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;
import uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd;
import uk.ac.imperial.presage2.alloc.protocol.exchange.ExchangeProtocol;
import uk.ac.imperial.presage2.alloc.service.participant.ParticAllocEnvSrv;
import uk.ac.imperial.presage2.alloc.service.participant.ParticExAdEnvSrv;
import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.network.NetworkAdaptor;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.util.fsm.FSM;
import uk.ac.imperial.presage2.util.fsm.FSMException;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;

/**
 * Agent of the system which consumes electricity
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class AbstractConsumer extends AbstractParticipant {

    protected ParticipantRoundEnvSrv roundEnv;
    protected ParticAllocEnvSrv allocEnv;
    protected ParticExAdEnvSrv adEnv;
    protected AgentDemand ad;
    protected List<Action> actionsToPerform;
    protected ExchangeProtocol exchangeProtocol;
    protected AdsOffersHelper adsHelper;
    protected ArrayList<Allocation> myAllocations;

    /**
     * Constructor
     *
     * @param id a {@link java.util.UUID} object.
     * @param name a {@link java.lang.String} object.
     */
    public AbstractConsumer(UUID id, String name) {
        super(id, name);
    }

    /** {@inheritDoc} */
    @Override
    public void initialise() {
        super.initialise();

        actionsToPerform = new ArrayList<Action>();
        adsHelper = new AdsOffersHelper(getID());

        // Gets the participant environment
        try {
            this.roundEnv = this.getEnvironmentService(ParticipantRoundEnvSrv.class);
            this.allocEnv = this.getEnvironmentService(ParticAllocEnvSrv.class);
            this.adEnv = this.getEnvironmentService(ParticExAdEnvSrv.class);
        } catch (UnavailableServiceException e) {
            logger.error("Unable to reach ParticipantEnvironmentService service", e);
        }


    }

    /** {@inheritDoc} */
    @Override
    protected Set<ParticipantSharedState> getSharedState() {
        Set<ParticipantSharedState> ss = super.getSharedState();
        return ss;
    }

    /** {@inheritDoc} */
    @Override
    public void incrementTime() {
        super.incrementTime();
        myAllocations = allocEnv.getMyAllocations();
        adsHelper.updateAds(adEnv.getList());
        adsHelper.updateMyAllocations(myAllocations);


        // Set Up
        if (roundEnv.getRoundType() == RoundType.INIT) {

            // close old ads
            closeAllAds();

            // Create the demand
            ad = new AgentDemand(getID());

            // restart
            adsHelper = new AdsOffersHelper(getID());

            try {
                exchangeProtocol = new ExchangeProtocolImpl(this, network);
            } catch (FSMException ex) {
                logger.error("Unable to load the ExchangeProtocol", ex);
            }


            // Time to demand electricity
        } else if (roundEnv.getRoundType() == RoundType.DEMAND) {


            addAction(new DemandAction(this, ad.getDemands()));


            // Time to echange slots I am not interested
        } else if (roundEnv.getRoundType() == RoundType.ADAPT) {


            // Print the agent satisfaction
            System.out.println(this + " Satisfaction: " + ad.getSatisfaction(
                    myAllocations));

            // Lets advertise the slots we dont want
            publishAnAd();

            // Send an offer to an ad from the list
            sendAnOffer(adEnv.getList(),
                    ad.getNotPrefered(myAllocations),
                    ad.getEmptyPreference(myAllocations));


        }
        updateAllocationStatus();
        act();
    }

    /** {@inheritDoc} */
    @Override
    protected void processInput(Input in) {
        if (exchangeProtocol.canHandle(in)) {
            exchangeProtocol.handle(in);
        }
    }

    private void publishAnAd() {
        
        // only one ad simultaneusly
        //if(adsHelper.getMyAds().size()> 0){
        //    return;
        //}
        
        for (Allocation alloc : ad.getNotPrefered(myAllocations)) {
            if (adsHelper.canIAdvertise(alloc)) {
                ExchangeAd ea = new ConsumptionExchangeAd(this.getID(),
                        (ConsumptionAllocation) alloc);

                addAction(new AddExchangeAdAction(this, ea));
                //return;
            }
        }
    }

    private void closeAllAds() {
        for (ExchangeAd exAd : adsHelper.getMyAds()) {
            addAction(new RemoveExchangeAdAction(this, exAd));
        }
    }

    /**
     * <p>addAction.</p>
     *
     * @param a a {@link uk.ac.imperial.presage2.core.Action} object.
     */
    protected void addAction(Action a) {
        actionsToPerform.add(a);
    }

    private void act() {

        // If it is empty, add a do nothing action
        if (actionsToPerform.isEmpty()) {
            actionsToPerform.add(new DoNothingAction(this));
        }


        // Perform all the actions in the list
        for (Action a : actionsToPerform) {
            try {
                environment.act(a, getID(), authkey);
            } catch (ActionHandlingException ex) {
                Logger.getLogger(AbstractConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        actionsToPerform.clear();
    }

    private void sendAnOffer(
            List<? extends ExchangeAd> list,
            List<Allocation> notPrefered,
            ArrayList<ProvisionTime> emptyPreference) {

        if (notPrefered.isEmpty() || emptyPreference.isEmpty()) {
            return;
        }

        ConsumptionExchangeAd consAd;
        Allocation allocOffered;
        List<Allocation> offer;

        for (Allocation myAlloc : notPrefered) {

            if (!adsHelper.canIOffer(myAlloc)) {
                continue;
            }
            offer = new ArrayList<Allocation>();
            offer.add(myAlloc);

            try {
                //Select a random init point to offer at the ads
                int i=0, j = (int)(Math.random()*list.size());
                while(i < list.size()){
                    consAd = (ConsumptionExchangeAd) list.get(j);
                    j++;
                    j%= list.size();
                    
                    if (!adsHelper.canIOffer(consAd,offer)) {
                        continue;
                    }
                    allocOffered = consAd.getOffer().get(0);

                    // If the ad is in our preference
                    if (emptyPreference.contains(allocOffered.
                            getAllocationTime())) {

                        NetworkAddress to = getAddress(allocOffered.getOwner());
                        if (to != null) {
                            //exchangeProtocol.
                                    sendOffer(to, this.getID(),
                                    allocOffered.getOwner(), consAd.getId(), offer);

                            adsHelper.offerSent(consAd, offer);
                            return;
                        }
                    }
                    i++;
                }
            } catch (FSMException ex) {
                logger.error("Unable to send an offer", ex);
            }
        }
    }

    private NetworkAddress getAddress(UUID id) {
        for (NetworkAddress na : network.getConnectedNodes()) {
            if (na.getId().equals(id)) {
                return na;
            }
        }
        return null;
    }

    /**
     * <p>acceptOffer.</p>
     *
     * @param exchangeAdId a {@link java.util.UUID} object.
     * @param initiator a {@link java.util.UUID} object.
     * @param offer a {@link java.util.List} object.
     * @return a boolean.
     */
    public synchronized boolean acceptOffer(UUID exchangeAdId, UUID initiator,
            List<Allocation> offer) {
        List<Allocation> of = new ArrayList<Allocation>();
        of.add(offer.get(0));


        ConsumptionExchangeAd cea = adsHelper.getMyAd(exchangeAdId);

        // Is not my ad
        if (cea == null) {
            return false;
        }

        // can not exchange this allocation
        if (!adsHelper.canIAccept(exchangeAdId)) {            
            return false;
        }

        
        
        // record that this add has been exchanged
        adsHelper.myAdExchanged(cea);

        // create the exchange contract action
        addAction(new ExchangeContractAction(this, initiator, cea.getOffer(), of));

        // create the remove exchange ad action
        addAction(new RemoveExchangeAdAction(this, getExchangeAd(exchangeAdId, adEnv.getList())));


        /*System.out.println(
                "Accepting AdID: " + cea.getId() + " FROM: " + this.getID() + " TO: " + initiator + " OFFER: " + cea.
                getOffer().get(0).getId() + " REQUEST: " + of.get(0).getId());*/

        return true;



    }

    /**
     * <p>Gets the ExchangeAd.</p>
     *
     * @param id a {@link java.util.UUID} object.
     * @param list a {@link java.util.List} object.
     * @return a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     */
    protected ConsumptionExchangeAd getExchangeAd(UUID id, List<? extends ExchangeAd> list) {
        for (ExchangeAd ea : list) {
            if (ea.getId().equals(id)) {
                return (ConsumptionExchangeAd) ea;
            }
        }
        return null;
    }

    /**
     * <p>Excecuted when an offer is Accepted.</p>
     *
     * @param exchangeAdId a {@link java.util.UUID} object.
     * @param initiator a {@link java.util.UUID} object.
     * @param offer a {@link java.util.List} object.
     */
    public void offerAccepted(UUID exchangeAdId, UUID initiator, List<Allocation> offer) {

        // get the ad
        ConsumptionExchangeAd cea = adsHelper.getOfferedAd(exchangeAdId);

        // create the exchange contract
        addAction(new ExchangeContractAction(this, cea.getOwner(), offer, cea.getOffer()));

        // record that this offer was accepted
        adsHelper.offerAccepted(cea, offer);

        // remove ads with this allocation
        for(ExchangeAd ea:adsHelper.getAdsWith(offer.get(0))){
            addAction(new RemoveExchangeAdAction(this, ea));
        }
        
        /*System.out.println(
                "Accepting AdID: " + exchangeAdId + " FROM: " + this.getID() + " TO: " + cea.
                getOwner() + " OFFER: " + offer.get(0).getId() + " REQUEST: " + cea.getOffer().
                get(0).getId());*/
    }

    /**
     * <p>Excecuted when an offer is Declined.</p>
     *
     * @param exchangeAdId a {@link java.util.UUID} object.
     * @param initiator a {@link java.util.UUID} object.
     * @param offer a {@link java.util.List} object.
     */
    public void offerDeclined(UUID exchangeAdId, UUID initiator, List<Allocation> offer) {
        // get the ad
        ConsumptionExchangeAd cea = adsHelper.getAd(exchangeAdId);

        // record that this offer was declined
        adsHelper.offerDeclined(cea, offer);

    }

    protected void sendOffer(NetworkAddress to, UUID id, UUID owner, UUID AdId,
            List<Allocation> offer) throws FSMException {
        exchangeProtocol.sendOffer(to, this.getID(), owner, AdId, offer);
    }

    private void updateAllocationStatus() {
        HashMap<UUID,Boolean> map = new HashMap<UUID,Boolean>();
        List<Allocation> notPrefered = ad.getNotPrefered(myAllocations);
        
        for(Allocation a: myAllocations){
            map.put(a.getId(),!notPrefered.contains(a));
        }
        
        addAction(new NotifyAllocationStatus(this,map));
        
    }

    private class ExchangeProtocolImpl extends ExchangeProtocol {

        AbstractConsumer agent;

        public ExchangeProtocolImpl(AbstractConsumer agent, NetworkAdaptor network) throws FSMException {
            super(FSM.description(), network);
            this.agent = agent;
        }

        @Override
        public boolean accept(UUID exchangeAdId, UUID initiator,
                List<Allocation> offer) {
            return agent.acceptOffer(exchangeAdId, initiator, offer);
        }

        @Override
        public void offerAccepted(UUID exchangeAdId, UUID initiator,
                List<Allocation> offer) {
            agent.offerAccepted(exchangeAdId, initiator, offer);
        }

        @Override
        public void offerDeclined(UUID exchangeAdId, UUID initiator,
                List<Allocation> offer) {
            agent.offerDeclined(exchangeAdId, initiator, offer);
        }
    }
}
