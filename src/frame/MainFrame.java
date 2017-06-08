package frame;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import base.Dialog;
import base.Window;
import control.Editor;
import function.FileArranger;
import model.Scheme;

public class MainFrame extends Window {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5988513125942516733L;
    private static final Dimension PREF_LIST_PANE_SIZE = new Dimension(350, 400);
    private static final Dimension MIN_DIMENSION = new Dimension(350, 440);
    private static final String SCHEME_LIST_BORDER = "方案列表", EXECUTE_SCHEMES = "执行方案",
        EDIT_SCHEME = "编辑", ADD_SCHEME = "添加", DELETE_SCHEMES = "删除", ARRANGING = "正在执行";
    private static final String EXECUTE_FUNCTION = "执行选中的整理方案", EDIT_FUNCTION = "编辑选中的整理方案",
        ADD_FUNCTION = "添加一个文件整理方案", DELETE_FUNCTION = "删除选中的整理方案";
    
    private final Editor editor = new Editor();
    private ArrayList<Scheme> schemeList;
    private JList<?> jSchemeList;
    private JPanel thisPanel;
    private boolean arranging = false;
    
    public MainFrame() {
        super("文件整理工具");
        initView();
    }
    
    private void initView() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(MIN_DIMENSION);
        
        thisPanel = new JPanel();
        thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
        thisPanel.add(Box.createVerticalStrut(4));
        
        jSchemeList = new JList();
        refreshList();
        jSchemeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jSchemeList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                int[] selectedIndices = jSchemeList.getSelectedIndices();
                if (selectedIndices == null || selectedIndices.length == 0) {
                    // No Selection
                    whenNoSelection();
                } else if (selectedIndices.length == 1) {
                    // Single Selection
                    whenSingleSelection();
                } else {
                    // Multi Selection
                    whenMultiSelection();
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(jSchemeList);
        listScrollPane.setPreferredSize(PREF_LIST_PANE_SIZE);
        listScrollPane.setBorder(BorderFactory.createTitledBorder(SCHEME_LIST_BORDER));
        
        thisPanel.add(listScrollPane);
        
        initButtons();
        add(thisPanel);
    }
    
    private void refreshList() {
        schemeList = editor.getSchemeList();
        DefaultListModel listModel = new DefaultListModel();
        schemeList.forEach(scheme -> {
            listModel.addElement(scheme);
        });
        jSchemeList.setModel(listModel);
    }
    
    private JButton exeSchemesButton, editSchemeButton, deleteSchemeButton, addSchemeButton;
    
    private void initButtons() {
        exeSchemesButton = new JButton(EXECUTE_SCHEMES);
        exeSchemesButton.setToolTipText(EXECUTE_FUNCTION);
        exeSchemesButton.setEnabled(false);
        exeSchemesButton.addActionListener(l -> {
            FileArranger arranger = new FileArranger();
            int[] selected = jSchemeList.getSelectedIndices();
            for (int index : selected) {
                arranger.addScheme(schemeList.get(index));
            }
            exeSchemesButton.setEnabled(false);
            exeSchemesButton.setText(ARRANGING);
            arranging = true;
            new Thread(() -> {
                arranger.arrange();
                exeSchemesButton.setEnabled(true);
                exeSchemesButton.setText(EXECUTE_SCHEMES);
                arranging = false;
            }).start();
        });
        editSchemeButton = new JButton(EDIT_SCHEME);
        editSchemeButton.setToolTipText(EDIT_FUNCTION);
        editSchemeButton.setEnabled(false);
        editSchemeButton.addActionListener(l -> {
            EditorFrame editFrame = new EditorFrame();
            int index = jSchemeList.getSelectedIndex();
            editFrame.passScheme(schemeList.get(index), true);
            setVisible(false);
            editFrame.switchTo(() -> {
                setVisible(true);
                refreshList();
                repaint();
            });
        });
        addSchemeButton = new JButton(ADD_SCHEME);
        addSchemeButton.setToolTipText(ADD_FUNCTION);
        addSchemeButton.setEnabled(true);
        addSchemeButton.addActionListener(l -> {
            long newIndex = editor.getUnusedIndex();
            EditorFrame addFrame = new EditorFrame();
            Scheme newScheme = new Scheme(newIndex);
            newScheme.setName("方案" + newIndex);
            addFrame.passScheme(newScheme, false);
            setVisible(false);
            addFrame.switchTo(() -> {
                setVisible(true);
                refreshList();
                repaint();
            });
        });
        deleteSchemeButton = new JButton(DELETE_SCHEMES);
        deleteSchemeButton.setToolTipText(DELETE_FUNCTION);
        deleteSchemeButton.setEnabled(false);
        deleteSchemeButton.addActionListener(l -> {
            int[] selected = jSchemeList.getSelectedIndices();
            String info;
            if (selected == null) {
                return;
            } else if (selected.length == 1) {
                info = "\"" + schemeList.get(selected[0]) + "\"";
            } else {
                info = "选中的" + selected.length + "个方案";
            }
            deleteSchemeButton.setEnabled(false);
            Dialog dialog =
                new Dialog("警告", "你确定要删除" + info + "吗？", Dialog.DialogType.CONFIRM_CANCEL);
            dialog.switchIn(c -> {
                switch (c) {
                    case CONFIRM_YES_TERMINATE:
                        // Delete selected
                        long[] indicesToDel = new long[selected.length];
                        int i = 0;
                        for (int index : selected) {
                            indicesToDel[i++] = schemeList.get(index).getIndex();
                        }
                        editor.removeSchemes(indicesToDel);
                        refreshList();
                    case CANCEL_IGNORE:
                    case NO_RETRY:
                    case NULL:
                    default:
                        deleteSchemeButton.setEnabled(true);
                        break;
                }
            });
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exeSchemesButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(editSchemeButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(addSchemeButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteSchemeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        thisPanel.add(Box.createVerticalStrut(11));
        thisPanel.add(buttonPanel);
        thisPanel.add(Box.createVerticalStrut(12));
    }
    
    private void whenNoSelection() {
        // No selection,
        // disables executint, editing and deleting
        // enables adding
        if (!arranging) {
            exeSchemesButton.setEnabled(false);
        }
        editSchemeButton.setEnabled(false);
        addSchemeButton.setEnabled(true);
        deleteSchemeButton.setEnabled(false);
    }
    
    private void whenSingleSelection() {
        // Single selection,
        // all enables
        if (!arranging) {
            exeSchemesButton.setEnabled(true);
        }
        editSchemeButton.setEnabled(true);
        addSchemeButton.setEnabled(true);
        deleteSchemeButton.setEnabled(true);
    }
    
    private void whenMultiSelection() {
        // Multi selection,
        // disables editing
        // enables executing, adding and deleting
        if (!arranging) {
            exeSchemesButton.setEnabled(true);
        }
        editSchemeButton.setEnabled(false);
        addSchemeButton.setEnabled(true);
        deleteSchemeButton.setEnabled(true);
    }
    
    @Override
    protected void onFinish() {
        editor.saveList();
        super.onFinish();
    }
    
}
