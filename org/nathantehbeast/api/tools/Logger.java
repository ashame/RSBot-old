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
    private static Font defaultFont = new Font("Calibri", Font.PLAIN, 12);
    private static Color defaultBackground = new Color(43, 43, 43);
    private static Color defaultForeground = new Color(168, 182, 197);

    public Logger() {
        new Logger(defaultFont, defaultBackground, defaultForeground);
    }

    public Logger(final Font font) {
        new Logger(font, defaultBackground, defaultForeground);
    }

    public Logger(final Color color, final boolean background) {
        if (background) {
            new Logger(defaultFont, color, defaultForeground);
        } else {
            new Logger(defaultFont, defaultBackground, color);
        }
    }

    public Logger(final Font font, final Color color, final boolean background) {
        if (background) {
            new Logger(font, color, defaultForeground);
        } else {
            new Logger(font, defaultBackground, color);
        }
    }

    public Logger(final Color background, final Color foreground) {
        new Logger(defaultFont, background, foreground);
    }

    public Logger(final Font font, final Color background, final Color foreground) {
        JScrollPane scrollPane = new JScrollPane();
        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();

        textArea.setFont(font);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(background);
        textArea.setForeground(foreground);
        textArea.setEditable(false);

        scrollPane.setViewportView(textArea);
        scrollPane.setPreferredSize(new Dimension(765, 150));

        frame.getContentPane().add(scrollPane, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getWindows()[0].add(frame.getContentPane(), BorderLayout.SOUTH);
        frame.getWindows()[0].setSize(765, 760);
        frame.pack();
        log("Successfully attached console to RSBot");
    }

    public static void log(String s) {
        textArea.append("[" + new SimpleDateFormat("hh:mm:ss z").format(Calendar.getInstance().getTime()) + "] " + s + System.getProperty("line.separator"));
        textArea.scrollRectToVisible(new Rectangle(0, textArea.getHeight() - 2, 1, 1));
        System.out.println(s);
    }

    public static void remove() {
        frame.getWindows()[0].remove(frame.getContentPane());
        System.out.println("Removing log pane");
        frame.getWindows()[0].setSize(765, 612);
        System.out.println("Restoring RSBot");
    }
}
