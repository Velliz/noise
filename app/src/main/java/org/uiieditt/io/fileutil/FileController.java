package org.uiieditt.io.fileutil;

import java.io.File;
import java.io.IOException;

public class FileController {

    private File file;

    public FileController(File file){
        this.file = file;
    }

    @Override
    public String toString() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {

        }
        return null;
    }
}
