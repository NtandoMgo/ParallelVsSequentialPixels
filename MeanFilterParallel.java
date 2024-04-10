import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MeanFilterParallel extends RecursiveAction{
    static int picWidth, ww;    //'ww' is the window width
    int hi, lo;        // variable 'hi' is the pic height 
    static BufferedImage image = null;
    static BufferedImage img = null;
    static int seqT = 10000;
    int width = picWidth;
    //int  seqTCopy = seqT;

    MeanFilterParallel (int width, int start, int end) {
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
        MeanFilterParallel mfp = new MeanFilterParallel(w, 0, h);
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
        
            //final long startTime = System.currentTimeMillis();
            for (int i = borderGuard; i < picWidth - borderGuard; i++) {
                for (int j = borderGuard; j < hi - borderGuard; j++) {
                    double[] values = new double[4]; // 0 = reds, 1 = greens, 2 = blues
                    //for (int j2 = 0; j2 < values.length; j2++) {
                        //values[j2] = 0;
                    //}
                    for (int x = i - borderGuard; x <= i + borderGuard; x++) {
                        for (int y = j - borderGuard; y <= j+ borderGuard; y++) {
                            int p = image.getRGB(x, y);
                            values[0] += (p>>24) & 0xff;
                            values[1] += (p >> 16) & 0xff;
                            values[2] += (p >> 8) & 0xff;
                            values[3] += p & 0xff;
                        }
                    }
                    for (int j2 = 0; j2 < values.length; j2++) {
                        values[j2] = values[j2] / (ww*ww);
                    }
                    int p = (int)values[0]<<24 | (int) values[1] << 16 | ((int) values[2] << 8) | (int) values[3];
                    img.setRGB(i, j, p);
                }
            }
            return;
        }// if the work if large enough or still large, break it down
        int spliting = (hi - lo)/2+lo;
        invokeAll(new MeanFilterParallel(picWidth, lo, spliting), new MeanFilterParallel(picWidth, spliting, hi));
    }
    
}
