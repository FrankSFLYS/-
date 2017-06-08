/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package function;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import base.Dialog;
import control.Editor;
import model.FilesTargetPair;
import model.Scheme;
import model.Strategy;

/**
 *
 * @author FrankFLY
 */
public class FileArranger {

    private ArrayList<Scheme> schemeList;
    private ArrayList<FilesTargetPair> filesForTargets;
    private ArrayList<String> currentOrigins;
    private ArrayList<Strategy> currentStrategies;
    private Editor editor = new Editor();
    private int succeed, failed;
    private ArrayList<File> failedList;

    public FileArranger() {
        schemeList = new ArrayList<>();
    }

    public void addScheme(Scheme scheme) {
        editor.initEditors(scheme.getIndex());
        scheme.setOrigins(editor.getFolderList());
        scheme.setStrategies(editor.getStrategyList());
        schemeList.add(scheme);
    }

    public void arrange() {
        for (Scheme scheme : schemeList) {
            filesForTargets = new ArrayList<>();
            scheme.getStrategies().forEach(s -> {
                FilesTargetPair ftp = new FilesTargetPair();
                ftp.target = new File(s.getTarget() + "\\").toPath();
                filesForTargets.add(ftp);
            });
            currentOrigins = scheme.getOrigins();
            currentStrategies = scheme.getStrategies();
            exeScheme();
        }
        String suc;
        StringBuilder fal = new StringBuilder();
        if (succeed > 0) {
            suc = "有" + succeed + "个文件成功移动。";
        } else {
            suc = "没有移动任何文件。";
        }
        if (failed > 0) {
            fal.append("有").append(failed).append("个文件移动失败。它们分别是\n");
            for (File f : failedList) {
                fal.append(f.getName()).append("\n");
            }
        } else {
            fal.append("");
        }
        Dialog dialog =
            new Dialog("完成", "已完成文件整理。" + suc + fal.toString(), Dialog.DialogType.CONFIRM_ONLY);
        dialog.switchIn();
    }

    private void exeScheme() {
        for (String origin : currentOrigins) {
            Path path = new File(origin + "\\").toPath();
            getFiles(path);
        }
        FileMover.move(filesForTargets, this);
    }

    private int getTargetIndex(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        for (int i = 0; i < currentStrategies.size(); i++) {
            for (String ext : currentStrategies.get(i).getExtensions()) {
                if (fileName.endsWith(ext.toLowerCase())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void getFiles(Path path) {
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
                if (attrs.isRegularFile()) {
                    int index = getTargetIndex(file);
                    if (index != -1) {
                        filesForTargets.get(index).filesToMove.add(file);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException {
                if (dir.equals(path)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

        };
        try {
            java.nio.file.Files.walkFileTree(path, finder);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addSuccess() {
        succeed++;
    }

    public void addFailure(File failedFile) {
        failedList.add(failedFile);
        failed++;
    }

}
