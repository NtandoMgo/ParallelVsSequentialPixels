import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;;

public class MedianFilterSerial {
    public static void median (String input, String output, int wWidth) {
        BufferedImage image = null, img = null;
        File f = null, fNew = null;

        try {
            f = new File(input);
            image = ImageIO.read(f);
            img = ImageIO.read(f);
        } catch (Exception e) {
            System.out.println(e);
        }

        int width = image.getWidth(), height = image.getHeight(), mid = (int) wWidth/2;
        // img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        for (int x = mid; x < width - mid; x++) {
            for (int y = mid; y < height - mid; y++) {

                ArrayList<Integer> reds = new ArrayList<>();
                ArrayList<Integer> grenns = new ArrayList<>();
                ArrayList<Integer> blues = new ArrayList<>();

                for (int x1 = x-mid; x1 <=x + mid; x1++) {
                    for (int y1 = y-mid; y1 <= y+mid; y1++) {
                        int p = image.getRGB(x1, y1);
                        reds.add((p>>16) & 0xff);
                        grenns.add((p>>8) & 0xff);
                        blues.add((p & 0xff));
                    } 
                }
                Collections.sort(reds); Collections.sort(grenns); Collections.sort(blues);
                int mid2 = (int)(reds.size()/2);
                int p = 255<<24 | (int) reds.get(mid2+1) << 16 | ((int) grenns.get(mid2+1) << 8) | (int) blues.get(mid2+1);
                img.setRGB(x, y, p);
            }
        }
        
        try {
            fNew = new File(output);
            ImageIO.write(img, "png", fNew);
            //System.out.println("Image printed");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {

        final long startTime = System.currentTimeMillis();

        median(args[0], args[1], Integer.parseInt(args[2]));

        final long endTime = System.currentTimeMillis();
        System.out.println("Time median serial filter took: " + (endTime-startTime)/1000.00);
    }
}
