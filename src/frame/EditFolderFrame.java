package frame;

import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import base.Dialog;
import base.Dialog.DialogType;
import base.Window;
import model.FolderModifier;

public class EditFolderFrame extends Window {
    
    private static final long serialVersionUID = 5974504387750716612L;
    private static final String FOLDER_LABEL = "文件夹：", BROSWER = "浏览", COMPLETE = "完成",
        CANCEL = "取消";
    private static final int MODE_ADD = 1, MODE_EDIT = 2;
    
    private int mode, index;
    String folder = "";
    private FolderModifier modifier;
    
    public EditFolderFrame(FolderModifier modifier, int index) {
        super("编辑要整理的文件夹");
        mode = MODE_EDIT;
        this.folder = modifier.s.get(index);
        this.modifier = modifier;
        this.index = index;
        initView();
    }
    
    public EditFolderFrame(FolderModifier modifier) {
        super("编辑要整理的文件夹");
        mode = MODE_ADD;
        this.modifier = modifier;
        initView();
    }
    
    private void initView() {
        modifier.modified = false;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JLabel fLabel = new JLabel(FOLDER_LABEL);
        JTextField fText = new JTextField();
        if (mode == MODE_EDIT) {
            fText.setText(folder);
        }
        JPanel fPanel = new JPanel();
        fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.X_AXIS));
        fPanel.add(Box.createHorizontalStrut(15));
        fPanel.add(fLabel);
        fPanel.add(Box.createHorizontalStrut(10));
        fPanel.add(fText);
        fPanel.add(Box.createHorizontalStrut(15));
        
        JButton broswer = new JButton(BROSWER);
        broswer.addActionListener(l -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(folder));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                fText.setText(file.getPath());
            }
        });
        JButton complete = new JButton(COMPLETE);
        complete.addActionListener(l -> {
            if (fText.getText().replaceAll(" ", "").equals("")) {
                Dialog dialog = new Dialog("注意", "文件夹名称不可以为空，请输入内容。", DialogType.CONFIRM_ONLY);
                dialog.switchIn();
                return;
            }
            switch (mode) {
                case MODE_ADD:
                    modifier.s.add(fText.getText());
                    modifier.modified = true;
                    break;
                case MODE_EDIT:
                    modifier.s.remove(index);
                    modifier.s.add(fText.getText());
                    modifier.modified = true;
                    break;
            }
            dispose();
        });
        JButton cancel = new JButton(CANCEL);
        cancel.addActionListener(l -> {
            modifier.modified = false;
            dispose();
        });
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
        bPanel.add(Box.createGlue());
        bPanel.add(broswer);
        bPanel.add(Box.createHorizontalStrut(15));
        bPanel.add(complete);
        bPanel.add(Box.createHorizontalStrut(15));
        bPanel.add(cancel);
        bPanel.add(Box.createGlue());
        
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.add(Box.createVerticalStrut(20));
        rootPanel.add(fPanel);
        rootPanel.add(Box.createVerticalStrut(15));
        rootPanel.add(bPanel);
        rootPanel.add(Box.createVerticalStrut(20));
        add(rootPanel);
    }
    
}
