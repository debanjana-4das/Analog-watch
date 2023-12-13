import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AnalogWatch implements Runnable {
    JFrame frame;
    JPanel watchPanel;
    Thread thread = null;
    int currentHour = 0, currentMinute = 0, currentSecond = 0;
    String currentTimeString = "";

    AnalogWatch() {
        frame = new JFrame("Analog Watch");
        watchPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWatch(g);
            }
        };

        frame.add(watchPanel);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            while (true) {
                Calendar cal = Calendar.getInstance();
                currentHour = cal.get(Calendar.HOUR_OF_DAY);
                currentMinute = cal.get(Calendar.MINUTE);
                currentSecond = cal.get(Calendar.SECOND);

                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                Date date = cal.getTime();
                currentTimeString = formatter.format(date);

                watchPanel.repaint();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawWatch(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int centerX = watchPanel.getWidth() / 2;
        int centerY = watchPanel.getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 10;

        // Draw decorative border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Draw clock face
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Draw clock numbers
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(font);
        drawNumbers(g2d, centerX, centerY, radius);

        // Draw clock hands
        drawClockHand(g2d, centerX, centerY, currentHour % 12 * 30 + currentMinute / 2, radius * 0.5, 8, Color.BLACK);
        drawClockHand(g2d, centerX, centerY, currentMinute * 6, radius * 0.7, 4, Color.BLACK);
        drawClockHand(g2d, centerX, centerY, currentSecond * 6, radius * 0.9, 2, Color.RED);

        // Draw center point
        g2d.setColor(Color.BLACK);
        g2d.fillOval(centerX - 5, centerY - 5, 10, 10);
    }

    public void drawNumbers(Graphics2D g2d, int centerX, int centerY, int radius) {
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(i * 30 - 60);
            int numberX = (int) (centerX + radius * 0.8 * Math.cos(angle));
            int numberY = (int) (centerY + radius * 0.8 * Math.sin(angle));

            g2d.drawString(Integer.toString(i), numberX - 4, numberY + 5);
        }
    }

    public void drawClockHand(Graphics2D g2d, int centerX, int centerY, double angle, double length, int width,
            Color color) {
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY);
        transform.rotate(Math.toRadians(angle));

        Shape hand = new Rectangle2D.Double(-width / 2, -length, width, length);
        g2d.setColor(color);
        g2d.fill(transform.createTransformedShape(hand));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnalogWatch());
    }
}
