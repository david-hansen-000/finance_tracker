package home.david.finances;

import java.io.File;

/**
 * Created by david on 2/22/18.
 */

public class PictureFile {
    private File file;
    private String fileName;
    public PictureFile(File f) {
        file=f;
        fileName=f.getName();
    }

    public String toString() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
