package test.util;//package test.util;

import org.junit.jupiter.api.Test;
import util.Util;

import java.awt.image.BufferedImage;

public class TestImage {
    @Test
    public void testImage() {
        BufferedImage norm = Util.getImageFromURL("https://www.cs.ubc.ca/~norm");
    }
}
