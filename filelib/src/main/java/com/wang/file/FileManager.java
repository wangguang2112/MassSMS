package com.wang.file;

import com.wang.file.path.BasePath;
import com.wang.file.path.ImagePath;
import com.wang.file.path.RootPath;
import com.wang.file.privacy.PrivacyPath;
import com.wang.file.privacy.PrivacyRootPath;

import java.util.ArrayList;

/**
 * Created by wangguang.
 * Date:2016/7/15
 * Description:单例模式
 */
public class FileManager implements IFileManager{
    private BasePath mRootPath;
    private ArrayList<BasePath> mBasePaths;
    private PrivacyRootPath mPrivacyRootPath;
    private ArrayList<PrivacyPath> mPrivacyPaths;
    private boolean isUsePrivacy=false;
    private static FileManager instence;

    public static IFileManager getDefaultInstence() {
        if(instence==null){
            instence=new FileManager();
        }
        return instence;
    }
    public FileManager(){
        mRootPath=new RootPath("./");
        mBasePaths=new ArrayList<BasePath>();
        mBasePaths.add((ImagePath) mRootPath.create(FileType.IMAGE));
        mBasePaths.add((BasePath) mRootPath.create("wang"));
        if(isUsePrivacy){
           openPrivacy();
        }
    }
    public  void setUsePrivacy(){
       if(!isUsePrivacy) {
           isUsePrivacy = true;
           openPrivacy();
       }
    }
    private void openPrivacy(){
        mPrivacyRootPath= (PrivacyRootPath) mRootPath.create(FileType.PRIVACY);
        mPrivacyPaths=new ArrayList<>();
        mPrivacyPaths.add((PrivacyPath) mPrivacyRootPath.create(FileType.NOMAL));
    }

}
