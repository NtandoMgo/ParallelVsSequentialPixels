import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MedianFilterParallel extends RecursiveAction{
    static int picWidth, ww;    //'ww' is the window width
    int hi, lo;        // variable 'hi' is the pic height 
    static BufferedImage image = null;
    static BufferedImage img = null;
    static int seqT = 10000;
    int width = picWidth;

    MedianFilterParallel (int width, int start, int end) {
        picWidth = width;
        hi = end;
        lo = start;
        /*if (width < 500) {seqT = width/3;} 
        else {seqT = width/4;}*/
    }
    /*void computeDirectly() {
        
    }*/
    public static void main(String[] args) {
        //int[][] arr = {{1,2},{3,4}};
        String inputPaths = args[0], outputPath = args[1];
        int windowWidth = Integer.parseInt(args[2]);
        ww = windowWidth;

        final long startTime = System.currentTimeMillis();
        try {
            File f = new File(inputPaths);
            image = ImageIO.read(f);
            img =  ImageIO.read(f);
        } catch (Exception e) {
            System.out.println("Reading input error.");
        }
        int w = image.getWidth(), h = image.getHeight();
        MedianFilterParallel mfp = new MedianFilterParallel(w, 0, h);
        ForkJoinPool p = new ForkJoinPool();
        p.invoke(mfp);

        try {
            File f1 = new File(outputPath);
            ImageIO.write(img, "png", f1);
        } catch (Exception e) {
            System.out.println("Writing output error.");
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Time completed in seconds: " + (endTime - startTime)/1000.00);
        
    }

    @Override
    protected void compute() {
        int area =(hi - lo)*picWidth;
        //System.out.println("Area: "+area+", Hi: "+hi+", Lo: "+lo+", PicWidth: "+picWidth+", width: "+width);
        if (area< seqT) {
            int borderGuard = ww/2;         // mainly to guide where the iteration starts, pass where cannot make a window
            int midPos = (ww*ww)/2 + 1;
            //final long startTime = System.currentTimeMillis();
            for (int i = borderGuard; i < picWidth - borderGuard; i++) {
                for (int j = borderGuard; j < hi - borderGuard; j++) {
                    // storing colors individually to sort them out
                    ArrayList<Integer> alphas = new ArrayList<>();
                    ArrayList<Integer> reds = new ArrayList<>();
                    ArrayList<Integer> greens = new ArrayList<>();
                    ArrayList<Integer> blues = new ArrayList<>();

                    for (int x = i - borderGuard; x <= i + borderGuard; x++) {
                        for (int y = j - borderGuard; y <= j+ borderGuard; y++) {
                            // Extracting colors to integer values from the pixel value of the image
                            int p = image.getRGB(x, y);
                            alphas.add((p>>24) & 0xff);
                            reds.add((p >> 16) & 0xff);
                            greens.add((p >> 8) & 0xff);
                            blues.add(p & 0xff);
                        }
                    }
                    // sorting and get the middle value to replace the color at the coordinates on the copy image
                    Collections.sort(alphas); Collections.sort(reds); Collections.sort(greens); Collections.sort(blues);

                    // converting the colors back to the pixel value
                    int p = alphas.get(midPos) <<24 |
                            reds.get(midPos) << 16 | 
                            greens.get(midPos) << 8 | 
                            blues.get(midPos);
                    img.setRGB(i, j, p);
                }
            }
            return;
        }// if the work if large enough or still large, break it down
        int spliting = (hi - lo)/2+lo;
        invokeAll(new MedianFilterParallel(picWidth, lo, spliting), new MedianFilterParallel(picWidth, spliting, hi));
    }
    
}
