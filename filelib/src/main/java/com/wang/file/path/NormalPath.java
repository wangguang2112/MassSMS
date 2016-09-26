package com.wang.file.path;

import com.wang.file.BaseFile;

import java.io.File;
import java.net.URI;

/**
 * Created by wangguang.
 * Date:2016/7/19
 * Description:
 */
public class NormalPath extends BasePath {

    public NormalPath(URI uri) {
        super(uri);
    }

    public NormalPath(String pathname) {
        super(pathname);
    }

    public NormalPath(File parent, String child) {
        super(parent, child);
    }

    public NormalPath(String parent, String child) {
        super(parent, child);
    }

    @Override
    public BaseFile create(String name) {
        BaseFile file=new BaseFile(getAbsolutePath()+name);
        return file;
    }
}
