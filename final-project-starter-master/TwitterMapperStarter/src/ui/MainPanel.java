package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import query.Query;
import twitter.LiveTwitterSource;
import twitter.TwitterSource;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.*;

/**
 * The Twitter viewer application
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class MainPanel extends JFrame {
    // The content panel, which contains the entire UI
    private final ContentPanel contentPanel;
    // The provider of the tiles for the map, we use the Bing source
    private BingAerialTileSource bing;
    // All of the active queries
    private List<Query> queries = new ArrayList<>();
    // The source of tweets, a TwitterSource, either live or playback
    private TwitterSource twitterSource;


    /**
     * Constructs the {@code MainPanel}.
     */
    public MainPanel() {
        super("Twitter content viewer");
        setSize(300, 300);
        initialize();

        bing = new BingAerialTileSource();
        // Do UI initialization
        contentPanel = new ContentPanel(this);

        configUI();
        configMap();
        configTimer();
    }


    private void initialize() {
        // To use the live twitter stream, use the following line
        twitterSource = new LiveTwitterSource();

        // To use the recorded twitter stream, use the following line
        // The number passed to the constructor is a speedup value:
        //  1.0 - play back at the recorded speed
        //  2.0 - play back twice as fast
        // twitterSource = new PlaybackTwitterSource(60.0);

        queries = new ArrayList<>();
    }

    private void configUI() {
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void configMap() {
        // Always have map markers showing.
        map().setMapMarkerVisible(true);
        // Always have zoom controls showing,
        // and allow scrolling of the map around the edge of the world.
        map().setZoomContolsVisible(true);
        map().setScrollWrapEnabled(true);
        // Use the Bing tile provider
        map().setTileSource(bing);

        // Set up a motion listener to create a tooltip showing the tweets at the pointer position
        map().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                ICoordinate pos = map().getPosition(p);
                // TO DO: Use the following method to set the text that appears at the mouse cursor
                // map().setToolTipText("This is a tool tip");
                List<MapMarker> markerList = getMarkersCovering(pos, pixelWidth(p));
                if (markerList.size() > 0) {
                    MapMarker m = markerList.get(markerList.size() - 1);
                    String tweet = ((OtherMapMarker) m).getTweet();
                    String profileImgUrl = ((OtherMapMarker) m).getProfileImageUrl();
                    map().setToolTipText("<html><img src=" + profileImgUrl + " height=\"42\" width=\"42\">" + tweet + markerList + "</html>");
                }
            }
        });
    }

    private void configTimer() {
        //NOTE This is so that the map eventually loads the tiles once Bing attribution is ready.
        Coordinate coordinate = new Coordinate(0, 0);
        Timer bingTimer = new Timer();
        TimerTask bingAttributionCheck = new TimerTask() {
            @Override
            public void run() {
                // This is the best method we've found to determine when the Bing data has been loaded.
                // We use this to trigger zooming the map so that the entire world is visible.
                if (!bing.getAttributionText(0, coordinate, coordinate).equals("Error loading Bing attribution data")) {
                    map().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        bingTimer.schedule(bingAttributionCheck, 100, 200);
    }

    /**
     * A new query has been entered via the User Interface
     *
     * @param query The new query object
     */
    public void addQuery(Query query) {
        queries.add(query);
        Set<String> allTerms = getQueryTerms();
        twitterSource.setFilterTerms(allTerms);
        contentPanel.addQuery(query);
        // TO DO: This is the place where you should connect the new query to the twitter source
        twitterSource.addObserver(query);
        twitterSource.getFilterTerms();
    }

    /**
     * return a list of all getTerms mentioned in all queries. The live twitter source uses this
     * to request matching tweets from the Twitter API.
     *
     * @return
     */
    private Set<String> getQueryTerms() {
        Set<String> queryTerms = new HashSet<>();
        for (Query q : queries) {
            queryTerms.addAll(q.getFilter().getTerms());
        }
        return queryTerms;
    }

    // How big is a single pixel on the map?  We use this to compute which tweet markers
    // are at the current most position.
    private double pixelWidth(Point p) {
        ICoordinate center = map().getPosition(p);
        ICoordinate edge = map().getPosition(new Point(p.x + 1, p.y));
        return Util.getDistanceBetween(center, edge);
    }

    // Get those layers (of tweet markers) that are visible because their corresponding query is enabled
    private Set<Layer> getVisibleLayers() {
        Set<Layer> layers = new HashSet<>();
        for (Query q : queries) {
            if (q.getVisible()) {
                layers.add(q.getLayer());
            }
        }
        return layers;
    }

    // Get all the markers at the given map position, at the current map zoom setting
    private List<MapMarker> getMarkersCovering(ICoordinate pos, double pixelWidth) {
        List<MapMarker> mapMarkers = new ArrayList<>();
        Set<Layer> visibleLayers = getVisibleLayers();
        for (MapMarker m : map().getMapMarkerList()) {
            if (!visibleLayers.contains(m.getLayer())) {
                continue;
            }
            double distance = Util.getDistanceBetween(m.getCoordinate(), pos);
            if (distance < m.getRadius() * pixelWidth) {
                mapMarkers.add(m);
            }
        }
        return mapMarkers;
    }

    public JMapViewer map() {
        return contentPanel.getViewer();
    }

    // Update which queries are visible after any checkBox has been changed
    public void updateVisibility() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Recomputing visible queries");
                for (Query q : queries) {
                    JCheckBox box = q.getCheckBox();
                    Boolean state = box.isSelected();
                    q.setVisible(state);
                }
                map().repaint();
            }
        });
    }

    // A query has been deleted, remove all traces of it
    public void terminateQuery(Query query) {
        // TO DO: This is the place where you should disconnect the expiring query from the twitter source
        queries.remove(query);
        Set<String> allTerms = getQueryTerms();
        twitterSource.setFilterTerms(allTerms);
        twitterSource.deleteObserver(query);
    }
}
