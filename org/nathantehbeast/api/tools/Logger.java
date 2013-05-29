package org.nathantehbeast.api.tools;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Nathan
 * Date: 5/28/13
 * Time: 3:04 AM
 * To change this template use File | Settings | File Templates.
 */

public class Logger {

    private static JTextArea textArea;
    private static JFrame frame;
    private static final Font defaultFont = new Font("Calibri", Font.PLAIN, 12);
    private static final Color defaultBackground = new Color(43, 43, 43);
    private static final Color defaultForeground = new Color(168, 182, 197);

    /**
     * Creates a new Logger with the default font, background, and foreground. This should be called only once per script, in the onStart method.
     * For example: new Logger();
     */
    public Logger() {
        new Logger(defaultFont, defaultBackground, defaultForeground);
    }

    /**
     * Creates a new Logger with your specified font, default background color, and foreground color.
     * @param font The font to use within the log pane.
     */
    public Logger(final Font font) {
        new Logger(font, defaultBackground, defaultForeground);
    }

    /**
     * Creates a new Logger with the default font, and your specified color on either the background or the foreground.
     * @param color The color of either the foreground or background.
     * @param background Whether to set the color as the background or foreground.
     */
    public Logger(final Color color, final boolean background) {
        if (background) {
            new Logger(defaultFont, color, defaultForeground);
        } else {
            new Logger(defaultFont, defaultBackground, color);
        }
    }

    /**
     * Creates a new Logger with your specified font, color on either the background or the foreground, and the default color for the other.
     * @param font The font to use within the log pane.
     * @param color The color for either the background or foreground
     * @param background Whether to set the color as the background or foreground
     */
    public Logger(final Font font, final Color color, final boolean background) {
        if (background) {
            new Logger(font, color, defaultForeground);
        } else {
            new Logger(font, defaultBackground, color);
        }
    }

    /**
     * Creates a new Logger with the default font, your specified background color, and your specified foreground color.
     * @param background The color to use for the background.
     * @param foreground The color to use for the foreground.
     */
    public Logger(final Color background, final Color foreground) {
        new Logger(defaultFont, background, foreground);
    }

    /**
     * Creates a new logger with your specified font, background color, and foreground color.
     * @param font The font to use within the log pane.
     * @param background The color to use for the background.
     * @param foreground The color to use for the foreground.
     */
    public Logger(final Font font, final Color background, final Color foreground) {
        JScrollPane scrollPane = new JScrollPane();
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        final Window parent = JFrame.getWindows()[0];

        textArea = new JTextArea();

        textArea.setFont(font);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setEditable(false);

        int width = parent.getWidth();
        int height = parent.getHeight();

        scrollPane.setViewportView(textArea);
        scrollPane.setPreferredSize(new Dimension(width, 150));

        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        parent.add(frame.getContentPane(), BorderLayout.SOUTH);
        parent.setSize(width, height + 150);
        parent.setMinimumSize(new Dimension(width, height+150));
        frame.pack();
        log("Successfully attached console to "+parent.getName());
    }

    /**
     * Logs a string to the pane.
     * @param s The string to log. It will be displayed as [hh:mm:ss z] Message
     */
    public static void log(String s) {
        textArea.append("[" + new SimpleDateFormat("hh:mm:ss z").format(Calendar.getInstance().getTime()) + "] " + s + System.getProperty("line.separator"));
        textArea.scrollRectToVisible(new Rectangle(0, textArea.getHeight() - 2, 1, 1));
        System.out.println(s);
    }

    /**
     * Removes the log pane from RSBot. Call this method on your onStop()
     * Ex: Logger.remove();
     */
    public static void remove() {
        final Window parent = JFrame.getWindows()[0];
        int width = parent.getWidth();
        int height = parent.getHeight();
        parent.remove(frame.getContentPane());
        parent.setSize(width, height - 150);
        parent.setMinimumSize(new Dimension(width, height-150));
        parent.pack();
        Logger.log("Removing log pane");
    }
}
