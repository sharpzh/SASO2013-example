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
package uk.ac.imperial.flexibledemand.service.participant;

import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import com.google.inject.Inject;
import java.util.UUID;
import uk.ac.imperial.flexibledemand.simulation.RoundType;
import uk.ac.imperial.flexibledemand.service.global.RoundEnvSrv;
import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.participant.Participant;

/**
 * View of the Round Environment for Consumers
 *
 * @author Patricio Petruzzi
 * @version 0.1.0
 * @since 0.1.0
 */
public class ParticipantRoundEnvSrv extends EnvironmentService {

    private RoundEnvSrv globalEnvironment;
    private UUID agentId;

    /**
     * Constructor
     *
     * @param sharedState a {@link uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess} object.
     * @param provider a {@link uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider} object.
     * @param participant a {@link uk.ac.imperial.presage2.core.participant.Participant} object.
     * @throws uk.ac.imperial.presage2.core.environment.UnavailableServiceException if any.
     */
    @Inject
    public ParticipantRoundEnvSrv(EnvironmentSharedStateAccess sharedState, EnvironmentServiceProvider provider, Participant participant) throws UnavailableServiceException {
        super(sharedState);
        this.globalEnvironment = provider.getEnvironmentService(RoundEnvSrv.class);
        this.agentId = participant.getID();
    }

    
    /**
     * Gets the round number
     *
     * @return round number
     */
    public int getRoundNumber() {
        return globalEnvironment.getRoundNumber();
    }

    /**
     * Gets the round type
     *
     * @return roundType
     */
    public RoundType getRoundType() {
        return globalEnvironment.getRoundType();
    }

}
