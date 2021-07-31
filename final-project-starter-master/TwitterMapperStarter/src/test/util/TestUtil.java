package test.util;//package test.util;

import filters.*;
import org.junit.jupiter.api.Test;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import util.ObjectSource;
import util.Util;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil {
    @Test
    public void testDistanceBetweenTwoPoints() {
        double distance = Util.getDistanceBetween(new Coordinate(3, 4), new Coordinate(3, 7));
        assertTrue(distance != 0);
    }

    @Test
    public void testImageFromURL() {
        assertNull(Util.getImageFromURL("www.uendi.com/wronglink"));
    }

    @Test
    public void testImageFromWrongURL() {
        assertNotNull(Util.getImageFromURL("https://lumiere-a.akamaihd.net/v1/images/b_thelionking2019_header_poststreet_mobile_18276_8dd5ba33.jpeg?region=0,0,640,430"));
    }

    @Test
    public void testObjectSource(){
        ObjectSource objectSource = new ObjectSource("data/TwitterCapture.jobj");
        Object object = objectSource.read();
        objectSource.close();
        assertNotNull(object);
    }
}
