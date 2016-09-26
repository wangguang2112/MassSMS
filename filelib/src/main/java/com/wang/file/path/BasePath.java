package com.wang.file.path;

import com.wang.file.BaseFile;
import com.wang.file.FileType;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangguang.
 * Date:2016/7/15
 * Description:
 */
public abstract class BasePath extends BaseFile {

    private String mFileType= FileType.NOMAL;

    public BasePath(URI uri) {
        super(uri);
    }

    public BasePath(String pathname) {
        super(pathname);
    }

    public BasePath(File parent, String child) {
        super(parent, child);
    }

    public BasePath(String parent, String child) {
        super(parent, child);
    }

    protected void setFileType(String fileType){
        this.mFileType=fileType;
    }
    public void deleteItem(int index){
//        TODO deleteItem
    }
    public void deleteItemByName(String name){
        //        TODO deleteItem
    }

    /**
     * 在当前目录下创建文件
     * @param name
     * @return 是否创见成功
     */
    public abstract BaseFile create(String name);

    /**
     * 获取当前目录下的所有的文件
     * @return
     */
    public List<BaseFile> getFiles(){
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString = this.list();
        for (int i = 0; i <= arrayOfString.length; i++)
        {
            if(arrayOfString[i].endsWith(mFileType)) {
                localArrayList.add(new BaseFile(this.getAbsolutePath(), arrayOfString[i]));
            }
        }
        return localArrayList;
    }

}
