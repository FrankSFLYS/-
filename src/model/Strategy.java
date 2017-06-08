package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Strategy {

	private ArrayList<String> extensions = new ArrayList<>();
	private String target;
	
	public void modifyExtension(String oldExt, String newExt) {
		extensions.remove(oldExt);
		extensions.add(newExt);
	}
	
	public void resetExtensions(ArrayList<String> extensions) {
		this.extensions = extensions;
	}
	
    public void addExtension(String newExt) {
        extensions.add(newExt);
    }
    
    public void addExtensions(String[] newExts) {
        extensions.addAll(Arrays.asList(newExts));
    }
    
	public void addExtensions(ArrayList<String> newExts) {
		extensions.addAll(newExts);
	}
	
	public void removeExtensions(ArrayList<String> exts) {
		extensions.removeAll(exts);
	}
	
	public ArrayList<String> getExtensions() {
		return extensions;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void removeTarget() {
		target = null;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
}
