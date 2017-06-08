package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import base.Dialog;
import base.Dialog.DialogType;
import base.Window;
import control.Editor;
import inherited.DocumentListenerAdapter;
import inherited.SchemePassable;
import model.FolderModifier;
import model.Scheme;
import model.Strategy;
import model.StrategyModifier;

public class EditorFrame extends Window implements SchemePassable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7545675621768910003L;
    private static final String ADD = "添加", EDIT = "编辑", DELETE = "删除";
    private static final String NAME_OF_SCHEME = "方案名称：";
    private static final String SAVE_SCHEME = "保存方案";
    private static final String TITLE_ORIGIN_FOLDERS = "要整理的文件夹", TITLE_STRATEGIES = "整理策略";
    private static final String[] STRATEGY_COLUMN_NAMES = {"扩展名", "目标文件夹"};
    private static final Dimension PREF_MIDDLE_PANEL_DIMENSION = new Dimension(300, 400);
    private static final Dimension MIN_FRAME_SIZE = new Dimension(520, 530);
    
    private Scheme currentScheme;
    private Editor editor;
    private boolean schemeModified;
    
    public EditorFrame() {
        super("方案编辑器");
    }
    
    private JPanel topPanel, middlePanel, folderPanel, strategyPanel;
    private JButton saveSchemeButton, fAddButton, fEditButton, fDeleteButton, sAddButton,
        sEditButton, sDeleteButton;
    private JList<String> folderJList;
    private JTextField schemeNameField;
    
    private void initView() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                if (schemeModified) {
                    Dialog dialog =
                        new Dialog("提示", "方案还未保存，是否现在保存？", Dialog.DialogType.YES_NO_CANCEL);
                    dialog.switchIn(c -> {
                        switch (c) {
                            case CONFIRM_YES_TERMINATE:
                                if (saveScheme()) {
                                    dispose();
                                }
                                break;
                            case NO_RETRY:
                                dispose();
                                break;
                            case CANCEL_IGNORE:
                            default:
                                break;
                        }
                    });
                } else {
                    dispose();
                }
            }
        });
        setMinimumSize(MIN_FRAME_SIZE);
        
        JLabel schemeNameLabel = new JLabel(NAME_OF_SCHEME);
        schemeNameField = new JTextField(currentScheme.getName());
        schemeNameField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            
            @Override
            public void modified() {
                schemeModified = true;
            }
        });
        saveSchemeButton = new JButton(SAVE_SCHEME);
        saveSchemeButton.addActionListener(l -> {
            saveScheme();
        });
        
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(schemeNameLabel);
        topPanel.add(schemeNameField);
        topPanel.add(Box.createGlue());
        topPanel.add(saveSchemeButton);
        add(topPanel, BorderLayout.NORTH);
        
        initFolderPanel();
        initStrategyPanel();
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 2));
        middlePanel.add(folderPanel);
        middlePanel.add(strategyPanel);
        
        add(middlePanel, BorderLayout.CENTER);
    }
    
    private void initFolderPanel() {
        folderJList = new JList<String>(currentScheme.getOrigins().toArray(new String[0]));
        folderJList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                int[] selectedIndices = folderJList.getSelectedIndices();
                if (selectedIndices == null || selectedIndices.length == 0) {
                    // No Selection
                    fWhenNoSelection();
                } else if (selectedIndices.length == 1) {
                    // Single Selection
                    fWhenSingleSelection();
                } else {
                    // Multi Selection
                    fWhenMultiSelection();
                }
            }
        });
        JScrollPane folderScrollPane = new JScrollPane(folderJList);
        folderScrollPane.setPreferredSize(PREF_MIDDLE_PANEL_DIMENSION);
        folderScrollPane.setBorder(BorderFactory.createTitledBorder(TITLE_ORIGIN_FOLDERS));
        
        fAddButton = new JButton(ADD);
        fAddButton.addActionListener(l -> {
            FolderModifier modifier = new FolderModifier();
            modifier.s = currentScheme.getOrigins();
            EditFolderFrame folderAdderFrame = new EditFolderFrame(modifier);
            setVisible(false);
            folderAdderFrame.switchTo(() -> {
                setVisible(true);
                refreshLists();
                if (modifier.modified) {
                    schemeModified = true;
                }
            });
        });
        fEditButton = new JButton(EDIT);
        fEditButton.addActionListener(l -> {
            int index = folderJList.getSelectedIndex();
            FolderModifier modifier = new FolderModifier();
            modifier.s = currentScheme.getOrigins();
            EditFolderFrame folderEditorFrame = new EditFolderFrame(modifier, index);
            setVisible(false);
            folderEditorFrame.switchTo(() -> {
                refreshLists();
                setVisible(true);
                if (modifier.modified) {
                    schemeModified = true;
                }
            });
        });
        fDeleteButton = new JButton(DELETE);
        fDeleteButton.addActionListener(l -> {
            int[] indices = folderJList.getSelectedIndices();
            for (int index : indices) {
                currentScheme.getOrigins().remove(index);
            }
            refreshLists();
            schemeModified = true;
        });
        fAddButton.setEnabled(true);
        fEditButton.setEnabled(false);
        fDeleteButton.setEnabled(false);
        
        JPanel fButtonPanel = new JPanel();
        fButtonPanel.setLayout(new BoxLayout(fButtonPanel, BoxLayout.X_AXIS));
        fButtonPanel.add(Box.createHorizontalGlue());
        fButtonPanel.add(fAddButton);
        fButtonPanel.add(Box.createHorizontalStrut(10));
        fButtonPanel.add(fEditButton);
        fButtonPanel.add(Box.createHorizontalStrut(10));
        fButtonPanel.add(fDeleteButton);
        fButtonPanel.add(Box.createHorizontalGlue());
        
        folderPanel = new JPanel();
        folderPanel.setLayout(new BoxLayout(folderPanel, BoxLayout.Y_AXIS));
        folderPanel.add(Box.createHorizontalStrut(15));
        folderPanel.add(folderScrollPane);
        folderPanel.add(Box.createHorizontalStrut(13));
        folderPanel.add(fButtonPanel);
        folderPanel.add(Box.createVerticalStrut(15));
    }
    
    JTable strategyTable;
    
    private void initStrategyPanel() {
        strategyTable = new JTable(new StrategyTableModel());
        strategyTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        DefaultListSelectionModel sModel = new DefaultListSelectionModel();
        sModel.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                int[] selectedIndices = strategyTable.getSelectedRows();
                if (selectedIndices == null || selectedIndices.length == 0) {
                    // No Selection
                    sWhenNoSelection();
                } else if (selectedIndices.length == 1) {
                    // Single Selection
                    sWhenSingleSelection();
                } else {
                    // Multi Selection
                    sWhenMultiSelection();
                }
            }
        });
        strategyTable.setSelectionModel(sModel);
        JScrollPane strategyPane = new JScrollPane(strategyTable);
        strategyPane.setPreferredSize(PREF_MIDDLE_PANEL_DIMENSION);
        strategyPane.setBorder(BorderFactory.createTitledBorder(TITLE_STRATEGIES));
        
        sAddButton = new JButton(ADD);
        sAddButton.addActionListener(l -> {
            StrategyModifier modifier = new StrategyModifier();
            modifier.s = currentScheme.getStrategies();
            EditStrategyFrame strategyAdder = new EditStrategyFrame(modifier);
            setVisible(false);
            strategyAdder.switchTo(() -> {
                setVisible(true);
                refreshLists();
                if (modifier.modified) {
                    schemeModified = true;
                }
            });
        });
        sEditButton = new JButton(EDIT);
        sEditButton.addActionListener(l -> {
            StrategyModifier modifier = new StrategyModifier();
            modifier.s = currentScheme.getStrategies();
            EditStrategyFrame strategyEditor =
                new EditStrategyFrame(modifier, strategyTable.getSelectedRow());
            setVisible(false);
            strategyEditor.switchTo(() -> {
                setVisible(true);
                refreshLists();
                if (modifier.modified) {
                    schemeModified = true;
                }
            });
        });
        sDeleteButton = new JButton(DELETE);
        sDeleteButton.addActionListener(l -> {
            int[] indices = strategyTable.getSelectedRows();
            for (int index : indices) {
                currentScheme.getStrategies().remove(index);
            }
            refreshLists();
            schemeModified = true;
        });
        
        sAddButton.setEnabled(true);
        sEditButton.setEnabled(false);
        sDeleteButton.setEnabled(false);
        
        JPanel sButtonPanel = new JPanel();
        sButtonPanel.setLayout(new BoxLayout(sButtonPanel, BoxLayout.X_AXIS));
        sButtonPanel.add(Box.createHorizontalGlue());
        sButtonPanel.add(sAddButton);
        sButtonPanel.add(Box.createHorizontalStrut(10));
        sButtonPanel.add(sEditButton);
        sButtonPanel.add(Box.createHorizontalStrut(10));
        sButtonPanel.add(sDeleteButton);
        sButtonPanel.add(Box.createHorizontalGlue());
        
        strategyPanel = new JPanel();
        strategyPanel.setLayout(new BoxLayout(strategyPanel, BoxLayout.Y_AXIS));
        strategyPanel.add(Box.createHorizontalStrut(15));
        strategyPanel.add(strategyPane);
        strategyPanel.add(Box.createHorizontalStrut(13));
        strategyPanel.add(sButtonPanel);
        strategyPanel.add(Box.createVerticalStrut(15));
    }
    
    private void refreshLists() {
        currentScheme.setOrigins(editor.getFolderList());
        currentScheme.setStrategies(editor.getStrategyList());
        DefaultListModel<String> folderModel = new DefaultListModel<>();
        currentScheme.getOrigins().forEach((s) -> {
            folderModel.addElement(s);
        });
        folderJList.setModel(folderModel);
        strategyTable.setModel(new StrategyTableModel());
    }
    
    private boolean saveScheme() {
        String name = schemeNameField.getText();
        if (name.replaceAll(" ", "").equals("")) {
            Dialog dialog = new Dialog("注意", "方案名称不能为空。", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
            return false;
        }
        if (editor.isNameAvailable(name, currentScheme.getIndex())) {
            currentScheme.setName(name);
            editor.saveScheme(currentScheme);
            schemeModified = false;
            return true;
        } else {
            Dialog dialog =
                new Dialog("注意", "这个方案的名称与已有方案重名了，请重新命名。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
            return false;
        }
    }
    
    @Override
    public void passScheme(Scheme scheme, boolean saved) {
        editor = new Editor();
        editor.initEditors(scheme.getIndex());
        schemeModified = !saved;
        
        currentScheme = scheme;
        
        currentScheme.setStrategies(editor.getStrategyList());
        currentScheme.setOrigins(editor.getFolderList());
        currentScheme.setStrategies(editor.getStrategyList());
        
        initView();
    }
    
    private void fWhenNoSelection() {
        fAddButton.setEnabled(true);
        fEditButton.setEnabled(false);
        fDeleteButton.setEnabled(false);
    }
    
    private void fWhenSingleSelection() {
        fAddButton.setEnabled(true);
        fEditButton.setEnabled(true);
        fDeleteButton.setEnabled(true);
    }
    
    private void fWhenMultiSelection() {
        fAddButton.setEnabled(true);
        fEditButton.setEnabled(false);
        fDeleteButton.setEnabled(true);
    }
    
    private void sWhenNoSelection() {
        sAddButton.setEnabled(true);
        sEditButton.setEnabled(false);
        sDeleteButton.setEnabled(false);
    }
    
    private void sWhenSingleSelection() {
        sAddButton.setEnabled(true);
        sEditButton.setEnabled(true);
        sDeleteButton.setEnabled(true);
    }
    
    private void sWhenMultiSelection() {
        sAddButton.setEnabled(true);
        sEditButton.setEnabled(false);
        sDeleteButton.setEnabled(true);
    }
    
    class StrategyTableModel implements TableModel {
        
        @Override
        public int getRowCount() {
            return currentScheme.getStrategies().size();
        }
        
        @Override
        public int getColumnCount() {
            return STRATEGY_COLUMN_NAMES.length;
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            return STRATEGY_COLUMN_NAMES[columnIndex];
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Strategy.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                ArrayList<String> extList =
                    currentScheme.getStrategies().get(rowIndex).getExtensions();
                StringBuilder builder = new StringBuilder();
                if (extList != null) {
                    for (int i = 0; i < extList.size(); i++) {
                        builder.append(extList.get(i));
                        if (i != extList.size() - 1) {
                            builder.append(",");
                        }
                    }
                }
                return builder.toString();
            } else {
                return currentScheme.getStrategies().get(rowIndex).getTarget();
            }
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                String str = (String) aValue;
                String[] exts = str.split(",");
                ArrayList<String> extList = new ArrayList<>();
                extList.addAll(Arrays.asList(exts));
                currentScheme.getStrategies().get(rowIndex).resetExtensions(extList);
            } else {
                String target = (String) aValue;
                currentScheme.getStrategies().get(rowIndex).setTarget(target);
            }
        }
        
        @Override
        public void addTableModelListener(TableModelListener l) {
        }
        
        @Override
        public void removeTableModelListener(TableModelListener l) {
        }
        
    }
    
}
