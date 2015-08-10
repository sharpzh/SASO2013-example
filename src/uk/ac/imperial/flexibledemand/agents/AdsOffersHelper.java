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
import java.util.Map;
import java.util.UUID;
import uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.exchange.ads.ExchangeAd;

/**
 * Class to organize which offers were made and which allocations were advertise
 *
 * @author Patricio E. Petruzzi
 * @version 0.0.2
 * @since 0.0.2
 */
public class AdsOffersHelper {

    // To control the ads and offer
    private UUID me;
    private List<ConsumptionExchangeAd> adsAlreadyExchanged;
    private List<Allocation> allocsOffered;
    private List<Allocation> myAllocations;
    private Map<ConsumptionExchangeAd, List<Allocation>> adsOfferedTo;
    private List<ConsumptionExchangeAd> myAds;
    private List<ConsumptionExchangeAd> allAds;

    /**
     * <p>Constructor for AdsOffersHelper.</p>
     *
     * @param me a {@link java.util.UUID} object.
     */
    public AdsOffersHelper(UUID me) {
        this.me = me;
        this.adsAlreadyExchanged = new ArrayList<ConsumptionExchangeAd>();
        this.allocsOffered = new ArrayList<Allocation>();
        this.myAllocations = new ArrayList<Allocation>();
        this.adsOfferedTo = new HashMap<ConsumptionExchangeAd, List<Allocation>>();
        this.myAds = new ArrayList<ConsumptionExchangeAd>();
        this.allAds = new ArrayList<ConsumptionExchangeAd>();
    }

    /**
     * <p>Notify that the ad has been exchanged.</p>
     *
     * @param ad a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     */
    public void myAdExchanged(ConsumptionExchangeAd ad) {
        adsAlreadyExchanged.add(ad);
        for (Allocation alloc : ad.getOffer()) {
            myAllocations.remove(alloc);
        }
    }

    /**
     * <p>Updates the ads lists</p>
     *
     * @param ads a {@link java.util.List} object.
     */
    public void updateAds(List<? extends ExchangeAd> ads) {
        myAds.clear();
        allAds.clear();
        
        for (ExchangeAd ea : ads) {
            ConsumptionExchangeAd cea = (ConsumptionExchangeAd) ea;

            if (cea.getOwner().equals(me)) {
                myAds.add(cea);
            } else {
                allAds.add(cea);
            }
        }
    }

    /**
     * <p>Gets the current ads for the agent</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends ExchangeAd> getMyAds() {
        return myAds;
    }

    /**
     * <p>Notifies that an offer has been sent.</p>
     *
     * @param ad a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     * @param offer a {@link java.util.List} object.
     */
    public void offerSent(ConsumptionExchangeAd ad, List<Allocation> offer) {
        allocsOffered.addAll(offer);

        if (!adsOfferedTo.containsKey(ad)) {
            adsOfferedTo.put(ad, new ArrayList<Allocation>());
        }
        adsOfferedTo.get(ad).addAll(offer);

    }

    /**
     * <p>Notifies that an offer has been accepted</p>
     *
     * @param ad a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     * @param offer a {@link java.util.List} object.
     */
    public void offerAccepted(ConsumptionExchangeAd ad, List<Allocation> offer) {

        // nothing to do

    }
    
    /**
     * <p>Returns the ads where the allocation is advertised.</p>
     *
     * @param alloc a {@link uk.ac.imperial.presage2.alloc.allocation.Allocation} object.
     * @return a {@link java.util.List} object.
     */
    public List<? extends ExchangeAd> getAdsWith(Allocation alloc){
        List<ConsumptionExchangeAd> ads = new ArrayList<ConsumptionExchangeAd>();
        
        for(ConsumptionExchangeAd cea:myAds){
            if(cea.getOffer().contains(alloc)){
                ads.add(cea);
            }
        }
        return ads;
    } 

    /**
     * <p>Notifies that an offer has been declined</p>
     *
     * @param ad a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     * @param offer a {@link java.util.List} object.
     */
    public void offerDeclined(ConsumptionExchangeAd ad, List<Allocation> offer) {
        for (Allocation al : offer) {
            allocsOffered.remove(al);
        }
    }

    /**
     * <p>Returns true if the agent can offer these allocations to that ad</p>
     *
     * @param ad a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     * @param offer a {@link java.util.List} object.
     * @return a boolean.
     */
    public boolean canIOffer(ConsumptionExchangeAd ad, List<Allocation> offer) {

        if (adsOfferedTo.containsKey(ad)) {
            List<Allocation> list = adsOfferedTo.get(ad);
            for (Allocation al : offer) {
                if (list.contains(al)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * <p>Returns true if the agent can offer these allocations. </p>
     *
     * @param offer a {@link java.util.List} object.
     * @return a boolean.
     */
    public boolean canIOffer(List<Allocation> offer) {

        for (Allocation alloc : offer) {
            if (!canIOffer(alloc)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>Returns true if the agent can offer that allocation. </p>
     *
     * @param alloc a {@link uk.ac.imperial.presage2.alloc.allocation.Allocation} object.
     * @return a boolean.
     */
    public boolean canIOffer(Allocation alloc) {

        // it is mine
        if (!myAllocations.contains(alloc)) {
            return false;
        }

        // Already offered that allocation to other ad
        if (allocsOffered.contains(alloc)) {
            return false;
        }
        
        /*if(getAdsWith(alloc).size()>0){
            return false;
        }*/
        
        return true;
    }

    /**
     * <p>Returns true if the agent can accept an offer to this ad.</p>
     *
     * @param adId a {@link java.util.UUID} object.
     * @return a boolean.
     */
    public boolean canIAccept(UUID adId) {

        ConsumptionExchangeAd ad = getMyAd(adId);

        // is not my ad
        if (ad == null) {
            return false;
        }

        // Already accepted an offer for this ad
        if (adsAlreadyExchanged.contains(ad)) {
            return false;
        }

        // Already offered that allocation to other ad
        for (Allocation al : ad.getOffer()) {
            if (allocsOffered.contains(al)) {
                return false;
            }
        }

        // I don't have that allocation anymore
        for (Allocation al : ad.getOffer()) {
            if (!myAllocations.contains(al)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>Gets the ad from my list</p>
     *
     * @param id a {@link java.util.UUID} object.
     * @return a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     */
    public ConsumptionExchangeAd getMyAd(UUID id) {

        for (ConsumptionExchangeAd cea : myAds) {
            if (cea.getId().equals(id)) {
                return cea;
            }
        }

        return null;
    }

    /**
     * <p>Gets the ad</p>
     *
     * @param id a {@link java.util.UUID} object.
     * @return a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     */
    public ConsumptionExchangeAd getAd(UUID id) {

        for (ConsumptionExchangeAd cea : allAds) {
            if (cea.getId().equals(id)) {
                return cea;
            }
        }

        return null;
    }
    
    /**
     * <p>Gets the ad from the offered list</p>
     *
     * @param id a {@link java.util.UUID} object.
     * @return a {@link uk.ac.imperial.flexibledemand.consumption.exchange.ads.ConsumptionExchangeAd} object.
     */
    public ConsumptionExchangeAd getOfferedAd(UUID id) {

        for (ConsumptionExchangeAd cea : adsOfferedTo.keySet()) {
            if (cea.getId().equals(id)) {
                return cea;
            }
        }

        return null;
    }

    /**
     * <p>Returns true if the allocation can be offered in an ad</p>
     *
     * @param alloc a {@link uk.ac.imperial.presage2.alloc.allocation.Allocation} object.
     * @return a boolean.
     */
    public boolean canIAdvertise(Allocation alloc) {

        // it is mine
        if (!myAllocations.contains(alloc)) {
            return false;
        }

        // not offered to other agent
        //if (allocsOffered.contains(alloc)) {
        //    return false;
        //}

        // not already in an ad
        if (myAdsContain(alloc)) {
            return false;
        }


        return true;
    }

    private boolean myAdsContain(Allocation alloc) {

        for (ConsumptionExchangeAd cea : myAds) {
            for (Allocation a : cea.getOffer()) {
                if (a.equals(alloc)) {
                    return true;
                }
            }
        }
        return false;
    }

    void updateMyAllocations(List<Allocation> list) {
        myAllocations = list;
    }
}
