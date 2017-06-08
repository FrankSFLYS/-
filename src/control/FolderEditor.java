/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.Dialog;

/**
 *
 * @author FrankFLY
 */
public class FolderEditor {

    public static String getFileName(long index) {
        return "folder_" + index + ".dat";
    }

    private ArrayList<String> folderList;

    private final long index;

    public FolderEditor(long index) {
        this.index = index;
        loadFolder();
    }

    public ArrayList<String> getFolderList() {
        return folderList;
    }

    public void saveFolders(String[] folders) {
        try {
            FileOutputStream fStream = new FileOutputStream(getFileName(index));
            OutputStreamWriter oWriter = new OutputStreamWriter(fStream, "UTF-8");
            BufferedWriter bWriter = new BufferedWriter(oWriter);
            for (String folder : folders) {
                bWriter.write(folder);
                bWriter.write("\n");
            }
            bWriter.close();
        } catch (FileNotFoundException e) {
            Dialog dialog =
                new Dialog("错误", "无法写入文件夹列表文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (UnsupportedEncodingException e) {
            Dialog dialog =
                new Dialog("错误", "文件夹列表文件编码错误，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法写入文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

    private void loadFolder() {
        folderList = new ArrayList<>();
        try {
            File file = new File(getFileName(index));
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileInputStream fStream = new FileInputStream(file);
                InputStreamReader iReader = new InputStreamReader(fStream, "UTF-8");
                BufferedReader bReader = new BufferedReader(iReader);
                Pattern folderPattern = Pattern.compile("\\w.+");
                String line;
                String str;
                Matcher m;
                while ((line = bReader.readLine()) != null) {
                    m = folderPattern.matcher(line);
                    if (m.find()) {
                        str = m.group(0);
                        if (str.endsWith("\\")) {
                            folderList.add(str.substring(0, str.length() - 1));
                        } else {
                            folderList.add(str);
                        }
                    }
                }
                bReader.close();
            }
        } catch (FileNotFoundException e) {
            Dialog dialog =
                new Dialog("错误", "无法读取文件夹列表文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (UnsupportedEncodingException e) {
            Dialog dialog =
                new Dialog("错误", "文件夹列表文件编码错误，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法读取文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }
}
