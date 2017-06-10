import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class Main{

	public static final int SCALE = 2;

	public static JFrame frame;

	Main(){
		frame = new JFrame();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frame.setBounds(0, 0, screenSize.width, screenSize.height);

		
		
		frame.setBackground(Color.white);
		
		JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 0));
        
        frame.add(panel);
        
		Snapper snap = new Snapper();
		
		frame.addMouseListener(snap);
		frame.setUndecorated(true);
		frame.setOpacity(0.1f);
		frame.setVisible(true);
	}

	public static void main(String []args){
		new Main();
	}


}
