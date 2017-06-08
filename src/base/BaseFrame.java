package base;

import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import adapter.ComponentAdapter;
import control.Configure;
import control.ScreenUtils;

public class BaseFrame extends JFrame {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5751070889761013350L;
	private static final Integer DEFAULT_WINDOW_WIDTH = 500;
	private static final Integer DEFAULT_WINDOW_HEIGHT = 600;
	
	private static ArrayList<BaseFrame> frameList = new ArrayList<>();	
	
	protected Configure configure = new Configure(this.getClass().getSimpleName());
	
	public BaseFrame() {
		super();
		onCreate();
	}
	
	public BaseFrame(String title) {
		super(title);
		onCreate();
	}
	
	protected void onCreate() {
		if(!frameList.contains(this)) {
			frameList.add(this);
		}
		int width = (Integer) 
			configure.getConfig(Configure.WINDOW_WIDTH, DEFAULT_WINDOW_WIDTH);
		int height = (Integer) 
			configure.getConfig(Configure.WINDOW_HEIGHT, DEFAULT_WINDOW_HEIGHT);
		setSize(width, height);
		setLocation(
				((int)ScreenUtils.getWidth() - width) / 2,
				((int)ScreenUtils.getHeight() - height) / 2);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				configure.saveConfig(
						Configure.WINDOW_WIDTH, getWidth());
				configure.saveConfig(
						Configure.WINDOW_HEIGHT, getHeight());
			}
		});
	}
	
	protected void onFinish() {
		if(frameList.contains(this)) {
			frameList.remove(this);
		}
	}
	
	@Override
	public void dispose() {
		onFinish();
		super.dispose();
	}
	
	/**
	 * 
	 */
	public static void exit() {
		Iterator<BaseFrame> iterator = frameList.iterator();
		BaseFrame frame;
		while(iterator.hasNext()) {
			frame = iterator.next();
			iterator.remove();
			frame.dispose();
		}
	}

}
