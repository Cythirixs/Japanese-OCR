import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;


public class Change {

	public static final int length = 10;
	public static BufferedImage img = null;
	
	public static void main(String []args){
		try{
			img = ImageIO.read(new File("test1.jpg"));
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		BufferedImage gray = toBufferedImage(convertGrayScale(img));
		saveImage(gray, "testgrayscale", "");
		int[][] grayData = getGrayData(gray);
		int mode = mode(grayData);
		BufferedImage binary = toBufferedImage(convertGraytoBinary(gray, grayData, mode));
		saveImage(binary, "testbinaryscale", "");
		
		System.out.println("completed");
		
		
		
	}
	
	public static BufferedImage convert(BufferedImage img){
		//BufferedImage gray = toBufferedImage(convertGrayScale(img));
		int[][] grayData = getGrayData(img);
		int mode = mode(grayData);
		BufferedImage binary = toBufferedImage(convertGraytoBinary(img, grayData, mode));
		return binary;
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static void saveImage(BufferedImage image, String fileName, String filePath){
		try{
			ImageIO.write(image, "png", new File(fileName+".png"));
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static int[][] getGrayData(BufferedImage image){
		final int height = image.getHeight();
		final int width = image.getWidth();
		
		int[][] result = new int[height][width];
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				result[row][col] = image.getRGB(col, row) &  0xff;
			}
		}
		return result;
	}

	public static int mode(int[][] data){
		int[][] hist = new int[data.length][2];
		
		for(int i=0; i<data.length; i++){
			for(int j=0; j<data[0].length; j++){
				int temp = contains(hist, data[i][j]) ;
				if(temp == -1){
					hist[i][0] = data[i][j];
				}
				else{
					hist[temp][1]++;
				}
			}
		}
		int larg =0;
		int index = -1;
		for(int i=0; i<hist.length; i++){
			//if(hist[i][0] != 0) System.out.println(hist[i][0]+ " " + hist[i][1]);
			if(hist[i][1] > larg){
				larg = hist[i][1];
				index = i;
			}
		}
		return hist[index][0];
	}
	
	public static int contains(int[][] hist, int n){
		for(int i=0; i<hist.length; i++){
			if(hist[i][0] == n) return i;
		}
		return -1;
	}

	public int[][] getDataWithoutRGB(BufferedImage image){
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int height = image.getHeight();
		final int width = image.getWidth();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}


	//returns converted b/w binary
	public static Image convertGrayScale(BufferedImage image){
		ImageFilter filter = new GrayFilter(true, 50);
		ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
		Image gray = Toolkit.getDefaultToolkit().createImage(producer);
		return gray;
	}
	public static Image convertGraytoBinary(BufferedImage image, int[][] grayData, int threshold){
		int height = image.getHeight();
		int width = image.getWidth();
		
		BufferedImage imageOut = image;
		for(int row = 0; row < height; row++){
			for(int col = 0; col < width; col++){
				if(grayData[row][col] < threshold){
					imageOut.setRGB(col, row, 0xff000000);
				}
				else{
					imageOut.setRGB(col, row, 0xffffffff);
				}
			}
		}
		return imageOut;
	}


}
