package com.sunyard.aps.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Write class comments here
 * <p/>
 * User: liumeng
 * Date: 2017/10/22 21:15
 * version $Id: ZipUtils.java, v 0.1 Exp $
 */


public class ZipUtils {
    private static Logger log = LoggerFactory.getLogger(ZipUtils.class);

    public static String downloadFile(String remoteFilePath, String localFilePath)
    {
        Date date=new Date();
        String targetName =date.getTime() +".zip";
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        StringBuffer sb=new StringBuffer(512);
        sb.append(localFilePath);

        File f = new File(localFilePath);
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
              //目的压缩文件名
            if(localFilePath.endsWith(File.separator)){
                sb.append(File.separator);
            }
            sb.append(targetName);
            FileOutputStream outputStream = new FileOutputStream(sb.toString());
            bos = new BufferedOutputStream (outputStream);
            int len = 4*1024;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            log.error("下载文件失败",e);
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                log.error("关闭流失败",e);
            }
        }
        return targetName;
    }

    /**
     * 将zip文件解压到目标文件
     * @param filePath
     * @param saveDir
     */
    public static List<String> unZip(String filePath,String saveDir)
    {
        List<String> list=new ArrayList<String>();
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipFile zipFile =null;
        int l = 4*1024;
        // get a zip file instance
        file = new File ( filePath ) ;
        try {
            Charset charset=Charset.forName("gbk");
            // get a ZipFile instance
            zipFile = new ZipFile ( file ,charset) ;

            // create a ZipInputStream instance
            ZipInputStream zis = new ZipInputStream ( new FileInputStream (
                    file ) ,charset) ;

            // create a ZipEntry instance , lay the every file from
            // decompress file temporarily
            ZipEntry entry = null ;

            // a circle to get every file
            while ( ( entry = zis.getNextEntry ( ) ) != null )
            {
                System.out.println ( "decompress file :"
                        + entry.getName ( ) ) ;

                // define the path to set the file
                File outFile = new File ( saveDir
                        + entry.getName ( ) ) ;



                // create an input stream
                BufferedInputStream bis = new BufferedInputStream (
                        zipFile.getInputStream ( entry ) ) ;

                // create an output stream
                bos = new BufferedOutputStream (
                        new FileOutputStream ( outFile ) ) ;
                byte [ ] b = new byte [ l] ;
                while ( true )
                {
                    int len = bis.read ( b ) ;
                    if ( len == - 1 )
                        break ;
                    bos.write ( b , 0 , len ) ;
                }
                list.add(entry.getName());
                // close stream
                bis.close ( ) ;
                bos.close ( ) ;
            }
            zis.close() ;
        }catch (IOException e){
            log.error("解压文件到指定目录失败",e);
        }finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null) {
                    fos.close();
                }
                if(is != null){
                    is.close();
                }
                if(zipFile != null){
                    zipFile.close();
                }
            }catch(Exception e) {
                log.error("关闭流失败",e);
            }
        }
        return list;

    }
}
