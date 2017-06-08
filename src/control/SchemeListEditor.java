package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import base.Dialog.DialogType;
import model.Scheme;

public class SchemeListEditor {

    private static final String SCHEME_LIST_FILE = "scheme_list.dat";
    private ArrayList<Scheme> schemeList;

    private long maxIndex = 0;

    public SchemeListEditor() {
        schemeList = new ArrayList<>();
    }

    public ArrayList<Scheme> getSchemeList() {
        loadList();
        return schemeList;
    }

    public void addScheme(Scheme scheme) {
        if (schemeList.contains(scheme)) {
            schemeList.remove(scheme);
        }
        schemeList.add(scheme);
        saveList();
    }

    public void removeScheme(long index) {
        schemeList.remove(new Scheme(index));
    }

    public long getUnusedIndex() {
        loadList();
        boolean[] usedList = new boolean[schemeList.size()];
        for (Scheme s : schemeList) {
            if (s.getIndex() < usedList.length) {
                usedList[(int) s.getIndex()] = true;
            }
        }
        for (int i = 0; i < usedList.length; i++) {
            if (!usedList[i]) {
                return i;
            }
        }
        return maxIndex + 1;
    }

    public long getMaxIndex() {
        loadList();
        return maxIndex;
    }

    public void saveList() {
        try {
            FileOutputStream fStream = new FileOutputStream(SCHEME_LIST_FILE);
            OutputStreamWriter oWriter = new OutputStreamWriter(fStream, "UTF-8");
            BufferedWriter bWriter = new BufferedWriter(oWriter);
            for (Scheme s : schemeList) {
                bWriter.write(s.getIndex() + "," + s.getName() + "\n");
            }
            bWriter.close();
        } catch (FileNotFoundException e) {
            Dialog dialog = new Dialog("错误", "无法保存方案列表。", DialogType.RETRY_CANCEL);
            dialog.switchIn(c -> {
                switch (c) {
                    case NO_RETRY:
                        saveList();
                        break;
                    default:
                        break;
                }
            });
        } catch (UnsupportedEncodingException e) {
            Dialog dialog = new Dialog("错误", "不支持编码，方案列表文件可能已经损坏。", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法写入文件，方案列表文件可能已经损坏。", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

    private void loadList() {
        schemeList = new ArrayList<>();
        try {
            FileInputStream fStream = new FileInputStream(SCHEME_LIST_FILE);
            InputStreamReader iReader = new InputStreamReader(fStream, "UTF-8");
            BufferedReader bReader = new BufferedReader(iReader);
            Pattern pattern = Pattern.compile("([0-9]+),(.+)");
            Matcher m;
            Scheme scheme;
            String line;
            while ((line = bReader.readLine()) != null) {
                if (line.length() > 0) {
                    m = pattern.matcher(line);
                    if (m.find()) {
                        scheme = new Scheme();
                        long index = Long.parseLong(m.group(1));
                        if (index > maxIndex) {
                            maxIndex = index;
                        }
                        scheme.setIndex(index);
                        scheme.setName(m.group(2));
                        schemeList.add(scheme);
                    }
                }
            }
            bReader.close();
        } catch (FileNotFoundException e) {
            Dialog dialog = new Dialog("错误", "无法读取方案列表，方案可能都消失了……", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (UnsupportedEncodingException e) {
            Dialog dialog = new Dialog("错误", "方案文件损坏。", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法读取方案列表，方案可能都消失了……", DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

}
