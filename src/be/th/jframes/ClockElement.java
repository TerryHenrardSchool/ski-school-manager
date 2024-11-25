package be.th.jframes;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class ClockElement {
	public static JLabel create(JFrame frame) {
        JLabel lblClock = new JLabel();
        lblClock.setHorizontalAlignment(SwingConstants.LEFT);
        lblClock.setBounds(10, 0, 150, 27);
        frame.getContentPane().add(lblClock);

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblClock.setText(timeFormat.format(new Date())); 

        Timer timer = new Timer(1000, e -> lblClock.setText(timeFormat.format(new Date())));
        timer.start();

        return lblClock;
    }
}
