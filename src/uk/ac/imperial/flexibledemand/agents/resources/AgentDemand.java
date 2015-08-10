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
package uk.ac.imperial.flexibledemand.agents.resources;

import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionDemand;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.Demand;
import uk.ac.imperial.presage2.alloc.allocation.time.IntegerProvisionTime;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;

/**
 * Represents the demand of electricity of an agent
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class AgentDemand {

    private static final int MAX_SLOTS = 24;
    private static final int SLOTS = 4;
    private static final int SLOTS_PREFERENCE = 4;
    private static final double CONSUMPTION = 1;
    private UUID agent;
    private List<Demand> demands;
    private ArrayList<ProvisionTime> preference;

    /**
     * Constructor of a random selection
     *
     * @param agent a {@link java.util.UUID} object.
     */
    public AgentDemand(UUID agent) {
        this.agent = agent;
        this.preference = createPreference(SLOTS_PREFERENCE, MAX_SLOTS);
        this.demands = createDemands(preference, SLOTS, CONSUMPTION);
    }

    private ArrayList<ProvisionTime> createPreference(int slots, int max) {
        ArrayList<ProvisionTime> list = new ArrayList<ProvisionTime>();
        IntegerProvisionTime t;
        int i;

        do {
            i = (int) (Math.random() * max);
            t = new IntegerProvisionTime(i);
            if (!list.contains(t)) {
                list.add(t);
            }
        } while (list.size() < slots);
        return list;
    }

    private List<Demand> createDemands(ArrayList<ProvisionTime> l,
            int slots, double consumption) {
        List<Demand> dem = new ArrayList<Demand>();
        ArrayList<ProvisionTime> list = new ArrayList<ProvisionTime>(l.size());
        ProvisionTime t;
        int position;

        for (ProvisionTime item : l) {
            list.add(item);
        }


        do {
            position = (int) (Math.random() * list.size());
            t = list.get(position);
            list.remove(t);
            dem.add(new ConsumptionDemand(agent, consumption, t));
        } while (dem.size() < slots);

        return dem;
    }

    /**
     * Gets the number slots
     *
     * @return a int.
     */
    public int getNumberOfDemandedSlots() {
        return demands.size();
    }

    /**
     * Gets the agent slot preference
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<ProvisionTime> getPreference() {
        return preference;
    }

    /**
     * Gets the agent preference in the specified slot
     *
     * @param t a {@link uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime} object.
     * @return a boolean.
     */
    public boolean isPrefered(ProvisionTime t) {
        return preference.contains(t);
    }

    /**
     * <p>Getter for the field
     * <code>demands</code>.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public List<Demand> getDemands() {
        return demands;
    }

    /**
     * <p>getSatisfaction.</p>
     *
     * @param myAllocations a {@link java.util.ArrayList} object.
     * @return a double.
     */
    public double getSatisfaction(List<Allocation> myAllocations) {

        if (myAllocations.isEmpty()) {
            return demands.size() > 0 ? 0 : 1;
        }

        double notPrefered = getNotPrefered(myAllocations).size();

        return 1 - (notPrefered / myAllocations.size());
    }

    /**
     * <p>getNotPrefered.</p>
     *
     * @param myAllocations a {@link java.util.ArrayList} object.
     * @return a {@link java.util.List} object.
     */
    public List<Allocation> getNotPrefered(List<Allocation> myAllocations) {

        ArrayList<ProvisionTime> list = new ArrayList<ProvisionTime>(preference.size());
        ArrayList<Allocation> notPrefered = new ArrayList<Allocation>();

        for (ProvisionTime item : preference) {
            list.add(item);
        }

        for (Allocation a : myAllocations) {
            if (list.contains(a.getAllocationTime())) {
                list.remove(a.getAllocationTime());
            } else {
                notPrefered.add(a);
            }
        }
        return notPrefered;
    }

    /**
     * <p>getEmptyPreference.</p>
     *
     * @param myAllocations a {@link java.util.ArrayList} object.
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<ProvisionTime> getEmptyPreference(List<Allocation> myAllocations) {
        ArrayList<ProvisionTime> list = new ArrayList<ProvisionTime>(preference.size());

        for (ProvisionTime item : preference) {
            list.add(item);
        }

        for (Allocation a : myAllocations) {
            if (list.contains(a.getAllocationTime())) {
                list.remove(a.getAllocationTime());
            }
        }

        return list;
    }
}
