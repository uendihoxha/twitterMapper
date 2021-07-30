package ui;

import query.Query;

import javax.swing.*;
import java.awt.*;

public class QueryPanel extends JPanel {
    private final MainPanel mainPanel;
    private final JPanel parentPanel;
    private final Query query;

    public QueryPanel(MainPanel mainPanel, JPanel parentPanel, Query query) {
        this.mainPanel = mainPanel;
        this.parentPanel = parentPanel;
        this.query = query;
        buildQueryPanel();
    }

    private void buildQueryPanel() {
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        newQueryPanel.add(createCheckBox(), createGridBagConstraint(newQueryPanel));
        newQueryPanel.add(createRemoveButton(newQueryPanel));
        parentPanel.add(newQueryPanel);
        validate();
    }

    private GridBagConstraints createGridBagConstraint(JPanel queryPanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        queryPanel.add(createColorPanel(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        return gridBagConstraints;
    }

    private JPanel createColorPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        return colorPanel;
    }


    private JCheckBox createCheckBox() {
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        checkbox.addActionListener(e -> mainPanel.updateVisibility());
        query.setCheckBox(checkbox);
        return checkbox;
    }

    private JButton createRemoveButton(JPanel queryPanel) {
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(30, 20));
        removeButton.addActionListener(e -> {
            mainPanel.terminateQuery(query);
            query.terminate();
            parentPanel.remove(queryPanel);
            revalidate();
        });
        return removeButton;
    }
}
