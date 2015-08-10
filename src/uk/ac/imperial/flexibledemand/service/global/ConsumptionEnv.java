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
package uk.ac.imperial.flexibledemand.service.global;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import uk.ac.imperial.flexibledemand.consumption.allocation.ConsumptionManager;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.AllocationList;
import uk.ac.imperial.presage2.alloc.allocation.AllocationListInterface;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;
import uk.ac.imperial.presage2.alloc.manager.AllocationManager;
import uk.ac.imperial.presage2.alloc.service.global.AllocEnvSrv;
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;

/**
 * Implementation of the AllocEnvSrv
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ConsumptionEnv extends AllocEnvSrv {

    private HashMap<UUID, Boolean> allocationsOK;

    /**
     * <p>Constructor for ConsumptionEnv.</p>
     *
     * @param sharedState a
     * {@link uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess} object.
     */
    @Inject
    public ConsumptionEnv(EnvironmentSharedStateAccess sharedState) {
        super(sharedState);
        allocationsOK = new HashMap<UUID, Boolean>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AllocationManager getAllocationManager() {
        return new ConsumptionManager();
    }

    public synchronized void addAllocationsStatus(Map<UUID, Boolean> map) {
        allocationsOK.putAll(map);
    }

    public synchronized Map<UUID, Boolean> getAllocationsStatus() {
        return allocationsOK;
    }

    public synchronized void emptyAllocationStatus() {
        allocationsOK = new HashMap<UUID, Boolean>();
    }

    public synchronized List<Allocation> getAllocations() {
        AllocationList al = ((AllocationList) getCurrentAllocation()).clone();
        ArrayList<Allocation> allocs = new ArrayList<Allocation>();
        ArrayList<ProvisionTime> times = new ArrayList<ProvisionTime>();

        times.addAll(al.getAllAllocationTimes());
        for (ProvisionTime pt : times) {
            allocs.addAll(al.getAllocationsAtTime(pt));
        }
        return allocs;
    }
}
