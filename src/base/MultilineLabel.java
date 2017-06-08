package base;

import javax.swing.JTextArea;
import javax.swing.UIManager;

class MultilineLabel extends JTextArea {
	
		private static final long serialVersionUID = -1235052932829774966L;
				
		public MultilineLabel() {
	        super();
	        setEditable(false);  
	        setCursor(null);  
	        setOpaque(false);  
	        setFocusable(false);  
	        setFont(UIManager.getFont("Label.font"));      
	        setWrapStyleWord(true);  
	        setLineWrap(true);
	        setAutoscrolls(true);
	    }
		
		public MultilineLabel(String text) {
			this();
			setText(text);
		}
	} 