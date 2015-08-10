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
package uk.ac.imperial.flexibledemand.simulation;

import java.util.HashSet;
import java.util.Set;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.network.NetworkModule;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import uk.ac.imperial.flexibledemand.agents.AbstractConsumer;
import uk.ac.imperial.flexibledemand.agents.AltruistConsumer;
import uk.ac.imperial.flexibledemand.agents.SelfishConsumer;
import uk.ac.imperial.flexibledemand.agents.SocialConsumer;
import uk.ac.imperial.flexibledemand.agents.action.handler.NotifyAllocationStatusHandler;
import uk.ac.imperial.flexibledemand.service.global.ConsumptionEnv;
import uk.ac.imperial.flexibledemand.service.global.RoundEnvSrv;
import uk.ac.imperial.flexibledemand.service.participant.ParticipantRoundEnvSrv;
import uk.ac.imperial.presage2.alloc.actions.handler.AddExchangeAdActionHandler;
import uk.ac.imperial.presage2.alloc.actions.handler.ChangeAllocationTimeActionHandler;
import uk.ac.imperial.presage2.alloc.actions.handler.DemandActionHandler;
import uk.ac.imperial.presage2.alloc.actions.handler.DoNothingActionHandler;
import uk.ac.imperial.presage2.alloc.actions.handler.ExchangeContractActionHandler;
import uk.ac.imperial.presage2.alloc.allocator.AllocatorInterface;
import uk.ac.imperial.presage2.alloc.service.global.AllocEnvSrv;
import uk.ac.imperial.presage2.alloc.service.global.ExAdEnvSrv;
import uk.ac.imperial.presage2.alloc.service.global.ExContrEnvSrv;
import uk.ac.imperial.presage2.alloc.service.participant.ParticAllocEnvSrv;
import uk.ac.imperial.presage2.alloc.service.participant.ParticExAdEnvSrv;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.simulator.Parameter;

/**
 * Creates a Simulation
 *
 * @author Patricio E. Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class FlexibleDemandSimulation extends InjectedSimulation {

    @Parameter(name = "allocator")
    public String allocator;

    @Parameter(name = "rounds")
    public int rounds;
    /**
     * Injects a service provider
     *
     * @param serviceProvider a {@link uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider} object.
     */
    @Inject
    public void setServiceProvider(EnvironmentServiceProvider serviceProvider) {
        try {
            AllocEnvSrv aes = serviceProvider.getEnvironmentService(AllocEnvSrv.class);
            aes.setAllocator(instantiate(allocator, AllocatorInterface.class));
            
            RoundEnvSrv res = serviceProvider.getEnvironmentService(RoundEnvSrv.class);
            res.setRound(rounds);
        } catch (UnavailableServiceException e) {
            logger.warn("unable to load AllocationEnvironmentService class", e);
        }
    }

    /**
     * Sets the modules
     *
     * @param modules a {@link java.util.Set} object.
     */
    public FlexibleDemandSimulation(Set<AbstractModule> modules) {
        super(modules);
    }

    /**
     * {@inheritDoc}
     *
     * Gets the modules
     */
    @Override
    protected Set<AbstractModule> getModules() {
        Set<AbstractModule> modules = new HashSet<AbstractModule>();
        modules.add(new AbstractEnvironmentModule()
                .addActionHandler(DemandActionHandler.class)
                .addActionHandler(ExchangeContractActionHandler.class)
                .addActionHandler(ChangeAllocationTimeActionHandler.class)
                .addActionHandler(DoNothingActionHandler.class)
                .addActionHandler(AddExchangeAdActionHandler.class)
                .addActionHandler(NotifyAllocationStatusHandler.class)
                .addGlobalEnvironmentService(RoundEnvSrv.class)
                .addGlobalEnvironmentService(ConsumptionEnv.class)
                .addGlobalEnvironmentService(ExContrEnvSrv.class)
                .addGlobalEnvironmentService(ExAdEnvSrv.class)
                .addParticipantEnvironmentService(ParticipantRoundEnvSrv.class)
                .addParticipantEnvironmentService(ParticAllocEnvSrv.class)
                .addParticipantEnvironmentService(ParticExAdEnvSrv.class)
                );
        modules.add(NetworkModule.fullyConnectedNetworkModule().withNodeDiscovery());
        return modules;
    }

    /**
     * {@inheritDoc}
     *
     * Adds to the Scenario
     */
    @Override
    protected void addToScenario(Scenario scenario) {
        
        for (int i = 0; i < 96; i++) {
            scenario.addParticipant(new SelfishConsumer(Random.randomUUID(), "Agent" + i));
        }
    }

    private <T> T instantiate(final String className, final Class<T> type) {
        try {
            return type.cast(Class.forName(className).newInstance());
        } catch (final InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
