package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OtherMapMarker extends MapMarkerCircle {
    private static final double DEFAULT_MARKER_SIZE = 17.0;
    private BufferedImage image;
    private String tweetText;
    private String profileImageUrl;

    public OtherMapMarker(Layer layer, Coordinate coord, Color color, String profileImageURL, String tweet) {
        super(layer, null, coord, DEFAULT_MARKER_SIZE, STYLE.FIXED, getDefaultStyle());
        setColor(Color.BLACK);
        setBackColor(color);
        image = Util.getImageFromURL(profileImageURL);
        tweetText = tweet;
        profileImageUrl = profileImageURL;
    }

    public String getTweet() {
        return this.tweetText;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    @Override
    public void paint(Graphics graphics, Point position, int radius) {
        int size = radius * 2;
        if (graphics instanceof Graphics2D && this.getBackColor() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Composite oldComposite = graphics2D.getComposite();
            graphics2D.setComposite(AlphaComposite.getInstance(3));
            graphics2D.setPaint(this.getBackColor());
            graphics.fillOval(position.x - radius, position.y - radius, size, size);
            graphics2D.setComposite(oldComposite);
            graphics.drawImage(image, position.x - 10, position.y - 10, 20, 20, null);
        }
    }
}
