package util;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import twitter4j.GeoLocation;
import twitter4j.Status;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Helpful methods that don't clearly fit anywhere else.
 */
public class Util {
    public static final BufferedImage DEFAULT_IMAGE = getImageFromURL("http://png-2.findicons.com/files/icons/1995/web_application/48/smiley.png");
    private static final int EARTH_RADIUS_IN_METERS = 6371000;

    public static BufferedImage getImageFromURL(String url) {
        try {
            BufferedImage img = ImageIO.read(new URL(url));
            if (img == null) {
                return DEFAULT_IMAGE;
            }
            return img;
        } catch (IOException e) {
            return DEFAULT_IMAGE;
        }
    }

    public static Coordinate getCoordinateFromGeoLocation(GeoLocation geoLocation) {
        return new Coordinate(geoLocation.getLatitude(), geoLocation.getLongitude());
    }

    public static GeoLocation getStatusLocation(Status status) {
        return new GeoLocation(getLatitudeFromStatus(status), getLongitudeFromStatus(status));
    }

    public static Coordinate getStatusCoordinate(Status status) {
        return new Coordinate(getLatitudeFromStatus(status), getLongitudeFromStatus(status));
    }

    private static double getLatitudeFromStatus(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLatitude() + topLeft.getLatitude()) / 2;
    }

    private static double getLongitudeFromStatus(Status status) {
        GeoLocation bottomRight = status.getPlace().getBoundingBoxCoordinates()[0][0];
        GeoLocation topLeft = status.getPlace().getBoundingBoxCoordinates()[0][2];
        return (bottomRight.getLongitude() + topLeft.getLongitude()) / 2;
    }

    public static double getDistanceBetween(ICoordinate p1, ICoordinate p2) {
        double lat1 = p1.getLat() / 180.0 * Math.PI;
        double lat2 = p2.getLat() / 180.0 * Math.PI;
        double deltaLon = (p2.getLon() - p1.getLon()) / 180.0 * Math.PI;
        double deltaLat = (p2.getLat() - p1.getLat()) / 180.0 * Math.PI;

        double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2.0) * Math.sin(deltaLon / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return c * EARTH_RADIUS_IN_METERS;
    }
}
