import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Matching{

	public BufferedImage img;
	public int xInc, yInc;

	public Matching(){
		try{
			img = ImageIO.read(new File("kanji.jpg"));
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}

		xInc = (int)(img.getWidth()/23);
		yInc = (int)(img.getHeight()/23);
		System.out.println(xInc + " " + yInc );
	}

	public String check(BufferedImage toCheck){
		Change change = new Change();
		Trim t = new Trim();
		for(int r = 0; r < 23; r++){
			for(int c = 0; c < 23; c++){

				BufferedImage check = img.getSubimage(xInc*c, yInc*r, xInc, yInc);
				BufferedImage binary = change.convert(check);
				BufferedImage crop = t.trim(binary);
				try{
					ImageIO.write(check, "jpg", new File("checking.jpg"));
					ImageIO.write(binary, "jpg", new File("checkingBinary.jpg"));
					ImageIO.write(crop, "jpg", new File("checkingCrop.jpg"));
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}

				if(compare(toCheck, crop) >= 0.8){
					return r + " " + c;
				}


			}
		}
		return "No Match";
	}

	public double compare(BufferedImage to, BufferedImage check){
		int wT = to.getWidth();
		int lT = to.getHeight();

		int wC = check.getWidth();
		int lC = check.getHeight();

		int toIncX = (wC - wT)/2;
		int toIncY = (lC - lT)/2;
		int chIncX = 0;
		int chIncY = 0;

		int fX = wT;
		int fY = lT;



		if(wT > wC){
			chIncX = (wT - wC)/2;
			toIncX = 0;
			fX = wC;
		}
		if(lT > lC){
			chIncY = (lT - lC)/2;
			toIncY = 0;
			fY = lC;
		}
		int toN = 0;
		int chN = 0;
		int share = 0;

		for(int i=0; i<fX; i++){
			for(int j=0; j<fY; j++){
				if(to.getRGB(i+toIncX, j+toIncY) == -16777216 && check.getRGB(i+chIncX, j+chIncY) == -16777216){
					toN++;
					chN++;
					share++;
				}
				else{
					if(to.getRGB(i+toIncX, j+toIncY) == -16777216) toN++;
					if(check.getRGB(i+chIncX, j+chIncY) == -16777216) chN++;
				}

			}
		}
		int larg = toN;
		if(toN < chN){
			larg = chN;
		}
		return share/larg;

	}



}
