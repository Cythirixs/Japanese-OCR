import java.awt.image.BufferedImage;


public class Trim {

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
	
	public static int[] measureX(BufferedImage image){
		int[][] hist = new int[image.getWidth()][2];
		
		for(int i=0; i<image.getWidth(); i++){
			for(int j=0; j<image.getHeight(); j++){
				if(image.getRGB(i, j) == -1) continue;
				//System.out.println(image.getRGB(i, j));
				if(image.getRGB(i, j) == -16777216){
					hist[i][1]++;
				}
			}
		}
		
		int L1 = 0;
		int L2 = 0;
		int L3 = 0;
		int x1 = 0;
		int x2 = 0;
		int x3 = 0;
		for(int i=0; i<hist.length; i++){
			int n = hist[i][1];
			
			if(n > L1){
				L1 = n;
				x1 = i;
			}
			else if(n > L2){
				L2 = n;
				x2 = i;
			}
			else if(n > L3){
				L3 = n;
				x3 = i;
			}
		}
		
		//System.out.println(L1 + " " + L2 + " " + L3);
		//System.out.println(x1 + " " + x2 + " " + x3);
		int xd = 0;
		
		int d12 = Math.abs(x1 - x2);
		int d13 = Math.abs(x1 - x3);
		int d23 = Math.abs(x2 - x3);
		if(d12 <= d13 && d12 <= d23){
			xd = (x1 + x2)/2;
		}
		else if(d13 <= d12 && d13 <= d23){
			xd = (x1 + x3)/2;
		}
		else if(d23 <= d12 && d23 <= d13){
			xd = (x2 + x3)/2;
		}
		int rightX = xd;
		int i;
		for(i=xd; i<hist.length-1; i++){
			if(hist[i][1] == 0 && hist[i+1][1] == 0){
				break;
			}
		}
		rightX = i;
		int leftX = xd;
		for(i = xd; i > 1; i--){
			if(hist[i][1] == 0 && hist[i-1][1] == 0){
				break;
			}
		}
		leftX = i;
				
		return new int[]{rightX, leftX};
	}
	
	public static int[] measureY(BufferedImage image){
		int[][] hist = new int[image.getHeight()][2];
		
		for(int i=0; i<image.getHeight(); i++){
			for(int j=0; j<image.getWidth(); j++){
				if(image.getRGB(j, i) == -1) continue;
				if(image.getRGB(j, i) == -16777216){
					hist[i][1]++;
				}
			}
		}
		
		int L1 = 0;
		int L2 = 0;
		int L3 = 0;
		int x1 = 0;
		int x2 = 0;
		int x3 = 0;
		for(int i=0; i<hist.length; i++){
			int n = hist[i][1];
			
			if(n > L1){
				L1 = n;
				x1 = i;
			}
			else if(n > L2){
				L2 = n;
				x2 = i;
			}
			else if(n > L3){
				L3 = n;
				x3 = i;
			}
		}
		int xd = 0;
		
		int d12 = Math.abs(x1 - x2);
		int d13 = Math.abs(x1 - x3);
		int d23 = Math.abs(x2 - x3);
		if(d12 <= d13 && d12 <= d23){
			xd = (x1 + x2)/2;
		}
		else if(d13 <= d12 && d13 <= d23){
			xd = (x1 + x3)/2;
		}
		else if(d23 <= d12 && d23 <= d13){
			xd = (x2 + x3)/2;
		}
		int rightX = xd;
		int i;
		for(i=xd; i<hist.length-1; i++){
			if(hist[i][1] == 0 && hist[i+1][1] == 0){
				break;
			}
		}
		rightX = i;
		int leftX = xd;
		for(i = xd; i > 1; i--){
			if(hist[i][1] == 0 && hist[i-1][1] == 0){
				break;
			}
		}
		leftX = i;
				
		return new int[]{rightX, leftX};
	}
	
	public BufferedImage trim(BufferedImage image){
		int[] x = measureX(image);
		int[] y = measureY(image);
		int x0 = x[1];
		int y0 = y[1];
		if(x[0] < x[1]){
			x0 = x[0];
		}
		if(y[0] < y[1]){
			y0 = y[0];
		}
		//System.out.println(x[0] + " " + x[1]);
		//System.out.println(y[0] + " " + y[1]);
		
		BufferedImage crop = image.getSubimage(x0, y0, Math.abs(x[0] - x[1]), Math.abs(y[0] - y[1]));
		return crop;
	}
}


