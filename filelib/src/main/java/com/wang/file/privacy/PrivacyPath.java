package com.wang.file.privacy;

import com.wang.file.FileType;
import com.wang.file.path.BasePath;

import java.io.File;
import java.net.URI;

/**
 * Created by wangguang.
 * Date:2016/7/15
 * Description:
 */
public abstract class  PrivacyPath  extends BasePath {

    public PrivacyPath(String parent, String child) {
        super(parent, child);
    }
    public PrivacyPath(String pathname) {
        super(pathname);
    }

    public PrivacyPath(File parent, String child) {
        super(parent, child);
    }

    public PrivacyPath(URI uri) {
        super(uri);
    }

    public void moveTo(String dirPath){
        //TODO
    }
}
