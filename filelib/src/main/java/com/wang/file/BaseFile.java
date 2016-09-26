package com.wang.file;

import java.io.File;
import java.net.URI;

/**
 * Created by wangguang.
 * Date:2016/7/15
 * Description:
 */
public  class BaseFile extends File{

    public BaseFile(URI uri) {
        super(uri);
    }

    public BaseFile(String pathname) {
        super(pathname);
    }

    public BaseFile(File parent, String child) {
        super(parent, child);
    }

    public BaseFile(String parent, String child) {
        super(parent, child);
    }


}
