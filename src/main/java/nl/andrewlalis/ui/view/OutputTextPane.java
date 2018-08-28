package nl.andrewlalis.ui.view;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom JTextPane object which provides easy methods to print to the screen.
 */
public class OutputTextPane extends JTextPane {

    /**
     * A list of named styles.
     */
    private Map<String, Style> styles;

    /**
     * A basic constructor to set default properties on this text pane.
     */
    public OutputTextPane() {
        this.initStyles();
        this.setEditable(false);
        this.setAutoscrolls(true);
    }

    private void initStyles() {
        this.styles = new HashMap<>();
        Style defaultStyle = this.addStyle("default", null);
        defaultStyle.addAttribute(StyleConstants.FontFamily, "Lucida Consonle");
        defaultStyle.addAttribute(StyleConstants.FontSize, 12);
        this.styles.put("default", defaultStyle);

        Style grayItalics = this.addStyle("gray_italics", defaultStyle);
        grayItalics.addAttribute(StyleConstants.Foreground, new Color(128, 128, 128));
        grayItalics.addAttribute(StyleConstants.Italic, true);
        this.styles.put("gray_italics", grayItalics);

        Style bold = this.addStyle("bold", defaultStyle);
        bold.addAttribute(StyleConstants.Bold, true);
        this.styles.put("bold", bold);

        Style smaller = this.addStyle("smaller", defaultStyle);
        smaller.addAttribute(StyleConstants.FontSize, 11);
        this.styles.put("smaller", smaller);
    }

    /**
     * Prints some text styled with a style that is defined in initStyles.
     * @param text The text to display.
     * @param styleName The name of the style to use.
     */
    public void printStyled(String text, String styleName) {
        StyledDocument doc = this.getStyledDocument();
        Style style = this.styles.get(styleName);
        if (style == null) {
            style = this.styles.get("default");
        }
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a line of text, with a newline character appended at the bottom.
     * @param text The text to append.
     */
    public void printLine(String text) {
        StyledDocument doc = this.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), (text + '\n'), null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
