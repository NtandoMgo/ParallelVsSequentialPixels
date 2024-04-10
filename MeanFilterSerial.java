// MNGNTA011
// PCP1

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class MeanFilterSerial {

    public static void mean(String input, String output, int ww) {
        BufferedImage image = null, img = null;
        File f = null, fNew = null;

        try {
            f = new File(input);
            image = ImageIO.read(f);
            img = ImageIO.read(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = image.getWidth(), h = image.getHeight();

        //BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int strtNum = ww/2;
        
        for (int i = strtNum; i < w - strtNum; i++) {
            for (int j = strtNum; j < h - strtNum; j++) {
                double[] values = new double[3]; // 0 = reds, 1 = greens, 2 = blues
                //for (int j2 = 0; j2 < values.length; j2++) {
                    //values[j2] = 0;
                //}
                for (int x = i - strtNum; x <= i + strtNum; x++) {
                    for (int y = j - strtNum; y <= j+ strtNum; y++) {
                        int p = image.getRGB(x, y);
                        values[0] += (p >> 16) & 0xff;
                        values[1] += (p >> 8) & 0xff;
                        values[2] += p & 0xff;
                    }
                }
                for (int j2 = 0; j2 < values.length; j2++) {
                    values[j2] = values[j2] / (ww*ww);
                }
                int p = 255<<24 | (int) values[0] << 16 | ((int) values[1] << 8) | (int) values[2];
                img.setRGB(i, j, p);
            }
        }
        
        try {
            fNew = new File(output);
            //System.out.println("printed");
            ImageIO.write(img, "png", fNew);
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

    public static void main(String[] args) {

        final long startTime = System.currentTimeMillis();
       
        mean(args[0], args[1], Integer.parseInt(args[2]));

        final long endTime = System.currentTimeMillis();
        System.out.println("Time completed in seconds: " + (endTime - startTime)/1000.00);
    }
}