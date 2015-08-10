package uk.ac.imperial.flexibledemand.agents.action.handler;

import com.google.inject.Inject;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import uk.ac.imperial.flexibledemand.agents.action.NotifyAllocationStatus;
import uk.ac.imperial.flexibledemand.service.global.ConsumptionEnv;
import uk.ac.imperial.presage2.alloc.actions.DemandAction;
import uk.ac.imperial.presage2.alloc.actions.handler.DemandActionHandler;
import uk.ac.imperial.presage2.alloc.service.global.AllocEnvSrv;
import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandler;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;

/**
 * {Insert class description here}
 *
 * @author Patricio E. Petruzzi
 * @version
 * @since
 *
 */
public class NotifyAllocationStatusHandler implements ActionHandler{

    final protected Logger logger = Logger.getLogger(this.getClass());
    protected ConsumptionEnv consumptionEnv = null;

    /**
     * Constructor
     *
     * @param serviceProvider Environment
     * @throws UnavailableServiceException if any.
     */
    @Inject
    public NotifyAllocationStatusHandler(
            EnvironmentServiceProvider serviceProvider) {

        try {
            this.consumptionEnv = serviceProvider.getEnvironmentService(ConsumptionEnv.class);
        } catch (UnavailableServiceException ex) {
            java.util.logging.Logger.getLogger(DemandActionHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Action action) {
        return action instanceof NotifyAllocationStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Input handle(Action action, UUID actor)
            throws ActionHandlingException {

        // check if the action is a Demand Action
        if (action instanceof NotifyAllocationStatus) {
            NotifyAllocationStatus nas = (NotifyAllocationStatus) action;
            this.consumptionEnv.addAllocationsStatus(nas.getMap());
            logger.debug("Handling: " + action);
        }
        return null;
    }
}
