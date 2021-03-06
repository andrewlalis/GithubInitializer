package nl.andrewlalis.ui.view.components;

import nl.andrewlalis.ui.view.table_models.DetailPairsModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The detail panel is meant for displaying the details of a specific entity. The actual content/details to display is
 * given by classes which implement the Detailable interface.
 */
public class DetailPanel extends JPanel {

    /**
     * The name field shows the entity's name.
     */
    private JTextField nameField;

    /**
     * A model to represent the key-value pairs of this entity.
     */
    private DetailPairsModel detailPairsModel;

    /**
     * Creates the panel with some basic empty components.
     */
    public DetailPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel("Details", SwingConstants.CENTER));
        this.add(this.buildNamePanel());
        this.add(this.buildPairsTablePanel());
    }

    /**
     * Sets this panel's properties according to the given entity.
     * @param entity The entity to get details from.
     */
    public void setDetailableEntity(Detailable entity) {
        if (entity == null) {
            this.nameField.setText(null);
            this.detailPairsModel.setPairs(new ArrayList<>());
        } else {
            this.nameField.setText(entity.getDetailName());
            this.detailPairsModel.setPairs(entity.getDetailPairs());
        }
    }

    /**
     * @return A JPanel containing the name field.
     */
    private JPanel buildNamePanel() {
        this.nameField = new JTextField();
        this.nameField.setEditable(false);

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel("Name:", SwingConstants.LEFT), BorderLayout.WEST);
        namePanel.add(this.nameField, BorderLayout.CENTER);

        return namePanel;
    }

    /**
     * @return A JPanel containing a table of properties.
     */
    private JPanel buildPairsTablePanel() {
        this.detailPairsModel = new DetailPairsModel();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Properties:", SwingConstants.LEFT), BorderLayout.NORTH);

        JTable pairsTable = new JTable(this.detailPairsModel);
        JScrollPane scrollPane = new JScrollPane(pairsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

}
