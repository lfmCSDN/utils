package com.sunyard.aps.common.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * Write class comments here
 * <p/>
 * User: liumeng
 * Date: 2017/12/6 10:41
 * version $Id: FileUtils.java, v 0.1 Exp $
 */


public class FileUtils {
    static boolean flag = false;
    public static String generatorTempFile(String fileName){
        String u="";
        try {
            File directory = new File("");// 参数为空
            u = directory.getCanonicalPath();
            System.out.println(u);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(StringUtils.isBlank(u)){
            u=FileUtils.class.getResource(File.separator).getPath();
        }
        StringBuffer sb=new StringBuffer(256);//本地下载地址 如E：//xiazai,用来保存下载的东西，最后会删掉
        sb.append(u);
        if(!u.endsWith(File.separator)){
            sb.append(File.separator);
        }
        sb.append(fileName).append(File.separator);
        File temp=new File(sb.toString());
        if(!temp.exists()){
            temp.mkdir();
        }
        String savePath=sb.toString();//生成zip文件以及解压文件的地址
        return savePath;
    }

    public static boolean deleteDirectory(String dirPath) {// 删除目录（文件夹）以及目录下的文件
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
        for (int i = 0; i < files.length; i++) {// 循环遍历删除文件夹下的所有文件(包括子目录)
            if (files[i].isFile()) {// 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                System.out.println(files[i].getAbsolutePath() + " 删除成功");
                if (!flag)
                    break;// 如果删除失败，则跳出
            } else {// 运用递归，删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;// 如果删除失败，则跳出
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {// 删除当前目录
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteFile(String filePath) {// 删除单个文件
        flag = false;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
            flag = true;
        }
        return flag;
    }
}
