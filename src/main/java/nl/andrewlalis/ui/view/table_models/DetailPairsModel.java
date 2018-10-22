package nl.andrewlalis.ui.view.table_models;

import nl.andrewlalis.util.Pair;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the small (2 column) table to display properties of a detailable entity.
 */
public class DetailPairsModel extends AbstractTableModel {

    /**
     * The pairs of properties.
     */
    private List<Pair<String, String>> pairs;

    /**
     * Columns for this model.
     */
    private String[] columns = {"Property", "Value"};

    /**
     * Constructs an empty list of pairs.
     */
    public DetailPairsModel() {
        this.pairs = new ArrayList<>();
    }

    public void setPairs(List<Pair<String, String>> pairs) {
        this.pairs = pairs;
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.pairs.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public String getColumnName(int i) {
        return this.columns[i];
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Pair pair = this.pairs.get(i);

        switch (i1) {
            case 0:
                return pair.getFirst();
            case 1:
                return pair.getSecond();
            default:
                return null;
        }
    }
}
