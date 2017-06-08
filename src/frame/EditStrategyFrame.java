package frame;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

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
import model.Strategy;
import model.StrategyModifier;

public class EditStrategyFrame extends Window {
    
    private static final long serialVersionUID = 2605772073925677067L;
    private static final String EXTENSION_NAME = "扩展名：", TARGET = "移动到：", BROSWER = "浏览",
        COMPLETE = "完成", CANCEL = "取消", TIP = "用逗号分割。例如：.doc,.txt";
    private static final int MODE_ADD = 1, MODE_EDIT = 2;
    
    private StrategyModifier modifier;
    private int index, mode;
    String folder = "";
    
    public EditStrategyFrame(StrategyModifier modifier) {
        super("编辑整理策略");
        this.modifier = modifier;
        mode = MODE_ADD;
        initView();
    }
    
    public EditStrategyFrame(StrategyModifier modifier, int index) {
        super("编辑整理策略");
        this.modifier = modifier;
        this.index = index;
        folder = modifier.s.get(index).getTarget();
        mode = MODE_EDIT;
        initView();
    }
    
    private void initView() {
        modifier.modified = false;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JLabel eLabel = new JLabel(EXTENSION_NAME);
        JTextField sText = new JTextField();
        JPanel ePanel = new JPanel();
        ePanel.setLayout(new BoxLayout(ePanel, BoxLayout.X_AXIS));
        ePanel.add(Box.createHorizontalStrut(15));
        ePanel.add(eLabel);
        ePanel.add(Box.createHorizontalStrut(10));
        ePanel.add(sText);
        ePanel.add(Box.createHorizontalStrut(15));
        
        JLabel tip = new JLabel(TIP);
        tip.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        JPanel tPanel = new JPanel();
        tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.X_AXIS));
        tPanel.add(Box.createHorizontalStrut(25));
        tPanel.add(tip);
        tPanel.add(Box.createHorizontalGlue());
        
        JLabel fLabel = new JLabel(TARGET);
        JTextField fText = new JTextField();
        JButton broswer = new JButton(BROSWER);
        broswer.addActionListener(l -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(fText.getText()));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                fText.setText(file.getPath());
            }
        });
        JPanel fPanel = new JPanel();
        fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.X_AXIS));
        fPanel.add(Box.createHorizontalStrut(15));
        fPanel.add(fLabel);
        fPanel.add(Box.createHorizontalStrut(10));
        fPanel.add(fText);
        fPanel.add(Box.createHorizontalStrut(10));
        fPanel.add(broswer);
        fPanel.add(Box.createHorizontalStrut(15));
        
        JButton complete = new JButton(COMPLETE);
        complete.addActionListener(l -> {
            Strategy strategy = new Strategy();
            if (fText.getText().replaceAll(" ", "").equals("")
                || sText.getText().replaceAll(" ", "").equals("")) {
                Dialog dialog = new Dialog("注意", "任何字段都不可以留空，请输入内容。", DialogType.CONFIRM_ONLY);
                dialog.switchIn();
                return;
            }
            strategy.setTarget(fText.getText());
            String exts = sText.getText().replaceAll(" ", "");
            strategy.addExtensions(exts.split(","));
            switch (mode) {
                case MODE_ADD:
                    modifier.s.add(strategy);
                    break;
                case MODE_EDIT:
                    modifier.s.remove(index);
                    modifier.s.add(strategy);
                    break;
            }
            modifier.modified = true;
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
        bPanel.add(complete);
        bPanel.add(Box.createHorizontalStrut(15));
        bPanel.add(cancel);
        bPanel.add(Box.createGlue());
        
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.add(Box.createVerticalStrut(20));
        rootPanel.add(ePanel);
        rootPanel.add(Box.createVerticalStrut(10));
        rootPanel.add(tPanel);
        rootPanel.add(Box.createVerticalStrut(25));
        rootPanel.add(fPanel);
        rootPanel.add(Box.createVerticalStrut(20));
        rootPanel.add(bPanel);
        rootPanel.add(Box.createVerticalStrut(25));
        
        if (mode == MODE_EDIT) {
            StringBuilder builder = new StringBuilder();
            ArrayList<String> exts = modifier.s.get(index).getExtensions();
            for (String s : exts) {
                builder.append(s);
                builder.append(",");
            }
            sText.setText(builder.toString());
            fText.setText(folder);
        }
        
        add(rootPanel);
    }
    
}
