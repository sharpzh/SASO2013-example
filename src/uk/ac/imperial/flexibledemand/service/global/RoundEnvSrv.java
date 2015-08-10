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

import org.apache.log4j.Logger;
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import uk.ac.imperial.presage2.core.event.EventBus;
import uk.ac.imperial.presage2.core.event.EventListener;
import uk.ac.imperial.presage2.core.simulator.EndOfTimeCycle;
import com.google.inject.Inject;
import java.util.ArrayList;
import processing.core.PApplet;
import uk.ac.imperial.flexibledemand.charts.saso.SasoInterface;
import uk.ac.imperial.flexibledemand.simulation.RoundType;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.AllocationList;
import uk.ac.imperial.presage2.alloc.allocation.AllocationListInterface;
import uk.ac.imperial.presage2.alloc.allocation.time.ProvisionTime;
import uk.ac.imperial.presage2.alloc.service.global.AllocEnvSrv;
import uk.ac.imperial.presage2.alloc.service.global.ExContrEnvSrv;
import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;

/**
 * Environment of the simulation to define the Rounds
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class RoundEnvSrv extends EnvironmentService {

    private final Logger logger = Logger.getLogger(this.getClass());
    private EnvironmentServiceProvider serviceProvider;
    private AllocEnvSrv allocEnvSrv;
    private ExContrEnvSrv exchContEnvSrv;
    private SasoInterface saso;
    private int roundNumber;
    private int roundLength;
    private RoundType round;

    /**
     * Constructor
     *
     * @param sharedState a
     * {@link uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess} object.
     * @param eb a {@link uk.ac.imperial.presage2.core.event.EventBus} object.
     * @param serviceProvider a
     * {@link uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider} object.
     */
    @Inject
    public RoundEnvSrv(EnvironmentSharedStateAccess sharedState, EventBus eb,
            EnvironmentServiceProvider serviceProvider) {
        super(sharedState);
        eb.subscribe(this);
        this.round = RoundType.INIT;
        this.roundNumber = 0;
        this.serviceProvider = serviceProvider;
        this.roundLength = 100;
    }

    /**
     * Excecuted at each time step
     *
     * Change the round type when necessary, increments the round number and
     * shows the chart
     *
     * @param e EndOfTimeCycle
     */
    @EventListener
    public synchronized void onIncrementTime(EndOfTimeCycle e) {

        getEnvironments();

        if (round == RoundType.INIT) {

            round = RoundType.DEMAND;
        } else if (round == RoundType.DEMAND) {
            performAllocation();
            round = RoundType.ADAPT;
        } else if (round == RoundType.ADAPT) {
        }

        incTimeOtherEnv();
        roundNumber++;

        if (roundNumber % roundLength == 0) {
            round = RoundType.INIT;
            System.out.println("Starting Day: " + (int) (roundNumber / roundLength));
        }

        logger.info("Next round: " + round);


    }

    private void getEnvironments() {
        // Gets the ConsumptionEnvSrv if not loaded
        if (allocEnvSrv == null) {
            try {
                this.allocEnvSrv = serviceProvider.getEnvironmentService(AllocEnvSrv.class);
            } catch (UnavailableServiceException ex) {
                logger.warn("unable to load AllocationEnvironmentService class", ex);
            }
        }

        // Gets the ExchangeContractEnvSrv if not loaded
        if (exchContEnvSrv == null) {
            try {
                this.exchContEnvSrv = serviceProvider.getEnvironmentService(ExContrEnvSrv.class);
            } catch (UnavailableServiceException ex) {
                logger.warn("unable to load ExchangeContractEnvironmentService class", ex);
            }
        }
    }

    private void performAllocation() {
        if (allocEnvSrv != null) {
            allocEnvSrv.performAllocation();
        }
    }

    private void incTimeOtherEnv() {
        if (allocEnvSrv != null) {
            allocEnvSrv.updateRound();
            ConsumptionEnv cenv = (ConsumptionEnv) allocEnvSrv;
            /*if (((int) (roundNumber / roundLength)) > 46) {
                if (saso == null) {
                    saso = new SasoInterface();
                    try {
                        Thread.sleep(20 * 1000);
                    } catch (Exception e) {
                    };
                }
                saso.refresh(cenv.getAllocations(), cenv.getAllocationsStatus(),
                        roundNumber % roundLength);

            }*/
        }
        if (exchContEnvSrv != null) {
            exchContEnvSrv.executeContracts();
        }
    }

    /**
     * Gets the round number
     *
     * @return round number
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Gets the round type
     *
     * @return roundType
     */
    public RoundType getRoundType() {
        return round;
    }

    /**
     * Defines the number of steps for a round
     *
     * @param r a int.
     */
    public void setRound(int r) {
        roundLength = r;
    }
}
