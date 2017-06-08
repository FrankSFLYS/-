/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import base.Dialog;
import model.Scheme;
import model.Strategy;

/**
 *
 * @author FrankFLY
 */
public class Editor {

    private static final SchemeListEditor schemeListEditor = new SchemeListEditor();

    private FolderEditor folderEditor;
    private StrategyEditor strategyEditor;

    public Editor() {
    }

    public void saveScheme(Scheme scheme) {
        FolderEditor fEditor = new FolderEditor(scheme.getIndex());
        fEditor.saveFolders(scheme.getOrigins().toArray(new String[0]));
        StrategyEditor sEditor = new StrategyEditor(scheme.getIndex());
        sEditor.saveStrategies(scheme.getStrategies().toArray(new Strategy[0]));
        schemeListEditor.addScheme(scheme);
        saveList();
    }

    public boolean isNameAvailable(String name, Long index) {
        for (Scheme s : getSchemeList()) {
            if (s.getName().equals(name)) {
                if (s.getIndex() != index) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private void removeAScheme(long index) {
        try {
            schemeListEditor.removeScheme(index);
            File folderFile = new File(FolderEditor.getFileName(index));
            File strategyFile = new File(StrategyEditor.getFileName(index));
            Files.deleteIfExists(folderFile.toPath());
            Files.deleteIfExists(strategyFile.toPath());
        } catch (IOException ex) {
            Dialog dialog = new Dialog("错误", "删除方案时发生错误，方案可能已经删除了。\n错误信息：" + ex.toString(),
                Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

    public void removeScheme(long index) {
        removeAScheme(index);
        schemeListEditor.saveList();
    }

    public void removeSchemes(long[] indices) {
        for (long index : indices) {
            removeAScheme(index);
        }
        schemeListEditor.saveList();
    }

    public void initEditors(long index) {
        folderEditor = new FolderEditor(index);
        strategyEditor = new StrategyEditor(index);
    }

    public ArrayList<String> getFolderList() {
        return folderEditor.getFolderList();
    }

    public ArrayList<Strategy> getStrategyList() {
        return strategyEditor.getStrategyList();
    }

    public ArrayList<Scheme> getSchemeList() {
        return schemeListEditor.getSchemeList();
    }

    public long getUnusedIndex() {
        return schemeListEditor.getUnusedIndex();
    }

    public long getMaxIndex() {
        return schemeListEditor.getMaxIndex();
    }

    public void saveList() {
        schemeListEditor.saveList();
    }
}
