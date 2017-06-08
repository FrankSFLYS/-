/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package function;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import model.FilesTargetPair;

/**
 *
 * @author FrankFLY
 */
public class FileMover {

    public static void move(ArrayList<FilesTargetPair> ftps, FileArranger arranger) {
        File newFile;
        File targetPath;
        for (FilesTargetPair ftp : ftps) {
            for (Path f : ftp.filesToMove) {
                try {

                    targetPath = new File(
                        ftp.target.getParent() + "\\" + ftp.target.getFileName().toString() + "");
                    if (!targetPath.exists()) {
                        targetPath.mkdir();
                    }
                    newFile = new File(ftp.target.getParent() + "\\"
                        + ftp.target.getFileName().toString() + "\\" + f.getFileName());
                    Files.move(f, newFile.toPath(), ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException ex) {
                    arranger.addFailure(f.toFile());
                } catch (IOException e) {
                    arranger.addFailure(f.toFile());
                }
                arranger.addSuccess();
            }
        }
    }

}
