package uk.ac.imperial.flexibledemand.charts.saso;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.ac.imperial.presage2.alloc.allocation.Allocation;
import uk.ac.imperial.presage2.alloc.allocation.time.IntegerProvisionTime;

/**
 * {Insert class description here}
 *
 * @author Patricio E. Petruzzi
 * @version
 * @since
 *
 */
public class SasoInterface extends JFrame {

    //JPanel dynset;
    int bar[] = new int[24];
    double averageSatisfaction = 0;
    int round = 0;

    public SasoInterface() {
        //create an instance of your processing applet
        final DrawArea applet = new DrawArea(this);

        final JFrame frame = new JFrame("PApplet in Java Application");

        frame.setSize(1440,700);

        //make sure to shut down the application, when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create a panel for the applet and the button panel
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1440,700);
            }
        };
        //start the applet
        applet.init();

        //store the applet in panel
        panel.add(applet);
        
        //store the panel in the frame
        frame.add(panel);
        //assign a size for the frame
        //reading the size from the applet


        //display the frame
        frame.setVisible(true);
        frame.pack();
    }

    public synchronized int[] getBars() {
        return bar;
    }

    public synchronized double getSatisfaction() {
        return averageSatisfaction;
    }
    
    public synchronized int getRound(){
        return round;
    }

    public synchronized void refresh(List<Allocation> allocs, Map<UUID, Boolean> status, int round) {
        int barLocal[] = new int[24];
        int total = 0;
        double averageSatisfactionLocal = 0;
        this.round = round;

        for (Allocation a : allocs) {
            if (!status.isEmpty() &&status.get(a.getId()) != null && status.get(a.getId())) {
                IntegerProvisionTime ipt = (IntegerProvisionTime) a.getAllocationTime();
                barLocal[ipt.getSlot()] += 1;
                averageSatisfactionLocal++;
            }
            total++;
        }
        averageSatisfaction = averageSatisfactionLocal / total;
        bar = barLocal;
        try {
            Thread.sleep(350);
        } catch (InterruptedException ex) {
            Logger.getLogger(SasoInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
