package top.mumudm.tmp.util.wolai.file;

import java.io.File;
import java.util.Comparator;

/**
 * @author mumu
 * @date 2021/1/9 17:21
 */
public class FileComparator implements Comparator<File> {

    FileNameComparator fileNameComparator = new FileNameComparator();

    @Override
    public int compare(File o1, File o2) {
        return fileNameComparator.compare(o1.getName(), o2.getName());
    }
}
