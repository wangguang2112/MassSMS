package com.wang.file.path;

import com.wang.file.BaseFile;
import com.wang.file.FileType;

import java.io.File;
import java.net.URI;

/**
 * Created by wangguang.
 * Date:2016/7/19
 * Description:
 */
public class RootPath extends BasePath{

    public RootPath(URI uri) {
        super(uri);
    }

    public RootPath(String pathname) {
        super(pathname);
    }

    public RootPath(File parent, String child) {
        super(parent, child);
    }

    public RootPath(String parent, String child) {
        super(parent, child);
    }

    @Override
    public BaseFile create(String name) {
        if (name.equals(FileType.IMAGE)) {
            return new ImagePath();
        } else if (name.equals(FileType.RECORD)) {
            return  new ImagePath();
        } else {
            return new NormalPath(name);
        }
    }
}
