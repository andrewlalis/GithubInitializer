package nl.andrewlalis.ui.view.components;

import nl.andrewlalis.util.Pair;

import java.util.List;

/**
 * Objects which implement this interface must provide
 */
public interface Detailable {

    /**
     * @return The display name for this object.
     */
    String getDetailName();

    /**
     * @return A String-to-String mapping for some key value pairs of properties to display.
     */
    List<Pair<String, String>> getDetailPairs();

}
