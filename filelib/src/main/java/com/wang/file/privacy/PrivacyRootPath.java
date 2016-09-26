package com.wang.file.privacy;

import com.wang.file.BaseFile;

import java.io.File;
import java.net.URI;

/**
 * Created by wangguang.
 * Date:2016/7/19
 * Description:
 */
public class PrivacyRootPath extends PrivacyPath{

    public PrivacyRootPath(String parent, String child) {
        super(parent, child);
    }

    public PrivacyRootPath(String pathname) {
        super(pathname);
    }

    public PrivacyRootPath(File parent, String child) {
        super(parent, child);
    }

    public PrivacyRootPath(URI uri) {
        super(uri);
    }

    @Override
    public BaseFile create(String name) {
        return null;
    }
}
