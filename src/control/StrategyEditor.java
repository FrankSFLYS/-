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
import model.Strategy;

/**
 *
 * @author FrankFLY
 */
public class StrategyEditor {

    public static String getFileName(long index) {
        return "strategy_" + index + ".dat";
    }

    private final long index;
    private final ArrayList<Strategy> strategies;

    public StrategyEditor(long index) {
        this.index = index;
        strategies = new ArrayList<>();
        loadStrategies();
    }

    public ArrayList<Strategy> getStrategyList() {
        return strategies;
    }

    public void saveStrategies(Strategy[] strategyList) {
        try {
            FileOutputStream fStream = new FileOutputStream(getFileName(index));
            OutputStreamWriter oWriter = new OutputStreamWriter(fStream, "UTF-8");
            BufferedWriter bWriter = new BufferedWriter(oWriter);
            for (Strategy strategy : strategyList) {
                for (String exts : strategy.getExtensions()) {
                    bWriter.write(exts + " ");
                }
                bWriter.write("\n" + strategy.getTarget() + "\n");
            }
            bWriter.close();
        } catch (FileNotFoundException e) {
            Dialog dialog =
                new Dialog("错误", "无法写入整理策略文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (UnsupportedEncodingException e) {
            Dialog dialog =
                new Dialog("错误", "整理策略文件编码错误，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法写入文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

    private void loadStrategies() {
        try {
            File file = new File(getFileName(index));
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileInputStream fStream = new FileInputStream(file);
                InputStreamReader iReader = new InputStreamReader(fStream, "UTF-8");
                BufferedReader bReader = new BufferedReader(iReader);
                Pattern extsPattern = Pattern.compile("(\\.(\\w+) )+");
                Pattern targPattern = Pattern.compile("\\w.+");
                String extsLine, targLine;
                Matcher extsMatcher, targMatcher;
                Strategy strategy;
                while ((extsLine = bReader.readLine()) != null
                    && (targLine = bReader.readLine()) != null) {
                    extsMatcher = extsPattern.matcher(extsLine);
                    targMatcher = targPattern.matcher(targLine);
                    strategy = new Strategy();
                    if (extsMatcher.find()) {
                        strategy.addExtensions(extsMatcher.group().split(" "));
                    }
                    if (targMatcher.find()) {
                        strategy.setTarget(targMatcher.group());
                    }
                    strategies.add(strategy);
                }
                bReader.close();
            }
        } catch (FileNotFoundException e) {
            Dialog dialog =
                new Dialog("错误", "无法读取整理策略文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (UnsupportedEncodingException e) {
            Dialog dialog =
                new Dialog("错误", "整理策略文件编码错误，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        } catch (IOException e) {
            Dialog dialog = new Dialog("错误", "无法读取文件，文件可能已经损坏。", Dialog.DialogType.CONFIRM_ONLY);
            dialog.switchIn();
        }
    }

}
