package ui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    private JPanel existingQueryPanel;
    private JMapViewer mapViewer;
    private MainPanel mainPanel;

    public ContentPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initMapViewer();
        setLayout(new BorderLayout());

        JPanel newQueryPanel = new NewQueryPanel(mainPanel);
        JPanel layerPanelContainer = new JPanel();
        layerPanelContainer.setLayout(new BorderLayout());
        addBorderToPanel(layerPanelContainer);

        existingQueryPanel = new JPanel();
        existingQueryPanel.setLayout(new javax.swing.BoxLayout(existingQueryPanel, javax.swing.BoxLayout.Y_AXIS));
        layerPanelContainer.add(existingQueryPanel, BorderLayout.NORTH);

        add(createTopLevelSplitPane(createQuerySplitPane(newQueryPanel, layerPanelContainer)), "Center");
        revalidate();
        repaint();
    }

    private void initMapViewer() {
        mapViewer = new JMapViewer();
        mapViewer.setMinimumSize(new Dimension(100, 50));
    }

    private JSplitPane createTopLevelSplitPane(Component left) {
        JSplitPane topLevelSplitPane = new JSplitPane(1);
        topLevelSplitPane.setDividerLocation(150);
        topLevelSplitPane.setLeftComponent(left);
        topLevelSplitPane.setRightComponent(mapViewer);
        return topLevelSplitPane;
    }


    private JSplitPane createQuerySplitPane(Component top, Component bottom) {
        JSplitPane querySplitPane = new JSplitPane(0);
        querySplitPane.setDividerLocation(150);
        querySplitPane.setTopComponent(top);
        querySplitPane.setBottomComponent(bottom);
        return querySplitPane;
    }

    private void addBorderToPanel(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Current Queries"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    // Add a new query to the set of queries and update the UI to reflect the new query.
    public void addQuery(Query query) {
        existingQueryPanel.add(new QueryPanel(mainPanel, existingQueryPanel, query));
        validate();
    }

    public JMapViewer getViewer() {
        return mapViewer;
    }
}
