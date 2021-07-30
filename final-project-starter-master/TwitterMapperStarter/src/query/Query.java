package query;

import filters.Filter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import twitter4j.Status;
import twitter4j.User;
import ui.OtherMapMarker;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


/**
 * A query over the twitter stream.
 * TO DO: Task 4: you are to complete this class.
 */
public class Query implements Observer {
    // The map on which to display markers when the query matches
    private final JMapViewer map;
    // The color of the outside area of the marker
    private final Color color;
    // The string representing the filter for this query
    private final String queryString;
    // The filter parsed from the queryString
    private final Filter filter;
    public Util util;
    // Each query has its own "layer" so they can be turned on and off all at once
    private Layer layer;
    // The checkBox in the UI corresponding to this query (so we can turn it on and off and delete it)
    private JCheckBox checkBox;
    // The marker on the map
    //private MapMarkerSimple mapMarkerSimple;
    private OtherMapMarker otherMapMarker;


    public Query(String queryString, Color color, JMapViewer map) {
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
    }

    public Color getColor() {
        return color;
    }

    public String getQueryString() {
        return queryString;
    }

    public Filter getFilter() {
        return filter;
    }

    public Layer getLayer() {
        return layer;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public boolean getVisible() {
        return layer.isVisible();
    }

    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     * <p>
     * TO DO: Implement this method
     */
    public void terminate() {
        setVisible(false);
        map.removeMapMarker(otherMapMarker);
    }

    @Override
    public void update(Observable o, Object arg) {
        // get object from observable
        Status status = (Status) arg;
        // check whether the query matches a tweet
        if (filter.matches(status)) {
            // get coordinate from from object
            Coordinate coordinate = Util.getStatusCoordinate(status);
            // create a marker
            //mapMarkerSimple = new MapMarkerSimple(getLayer(), coordinate);
            User user = status.getUser();
            String profileImageURL = user.getProfileImageURL();
            String tweet = status.getText();
            otherMapMarker = new OtherMapMarker(getLayer(), coordinate, getColor(), profileImageURL, tweet);
            // add the marker on the map
            // map.addMapMarker(mapMarkerSimple);
            map.addMapMarker(otherMapMarker);
        }
    }
}
