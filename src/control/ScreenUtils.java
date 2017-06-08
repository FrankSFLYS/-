package control;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenUtils {
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static double getWidth() {
		return screenSize.getWidth();		
	}
	
	public static double getHeight() {
		return screenSize.getHeight();
	}
	
}
