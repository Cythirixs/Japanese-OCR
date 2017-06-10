import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class Snapper implements MouseListener{
	public static int startX, startY;
	public static int endX, endY;
	
	public static void partialSnap(){
		try{
			Robot robot = new Robot();
			Rectangle capture = new Rectangle(startX, startY, Math.abs(startX - endX), Math.abs(startY-endY));
			//System.out.println(startX + " " + startY + " " + capture.width + " " + capture.height);
			BufferedImage screenCap = robot.createScreenCapture(capture);
			ImageIO.write(screenCap, "jpg", new File("screencap.jpg"));
			Change c = new Change();
			BufferedImage binary = c.convert(screenCap);
			ImageIO.write(binary, "jpg", new File("binarySC.jpg"));
			Trim t = new Trim();
			BufferedImage crop = t.trim(binary);
			ImageIO.write(crop, "jpg", new File("trim.jpg"));
			Matching m = new Matching();
			m.check(crop);

		}
		catch (AWTException | IOException ex) {
			System.err.println(ex);
		}
	}


	public void mousePressed(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();

		//System.out.println(startX + " " + startY + " 1");
	}

	public void mouseReleased(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();

	//	System.out.println(endX + " " + endY + " 2");
		partialSnap();
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {}




}
