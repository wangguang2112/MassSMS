package com.wang.file.path;

import com.wang.file.FileType;

import java.io.File;
import java.net.URI;
import java.util.LinkedHashMap;

/**
 * Created by wangguang.
 * Date:2016/7/15
 * Description:
 */
public class ImagePath extends BasePath{
    public ImagePath(){
        super(FileType.IMAGE);
        init();
    }
    public ImagePath(URI uri) {
        super(uri);
        init();
    }

    public ImagePath(String pathname) {
        super(pathname);
        init();
    }

    public ImagePath(File parent, String child) {
        super(parent, child);
        init();
    }

    public ImagePath(String parent, String child) {
        super(parent, child);
        init();
    }
    private void init(){
        super.setFileType(FileType.IMAGE);
    }
    @Override
    public BasePath create(String name) {
        return null;
    }

}
