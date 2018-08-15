package com.sunyard.aps.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/30.
 */
public class TxtUtils {
    private static Logger log = LoggerFactory.getLogger(TxtUtils.class);

    /**
     * 将字符串保存到指定位置
     *
     * @param content       字符串
     * @param localFilePath 指定位置
     * @return 文件名
     */
    public static String saveFile(String content, String localFilePath) {
        Date date = new Date();
        String targetName = date.getTime() + ".txt";
        FileWriter fileWriter = null;
        BufferedWriter bf = null;
        StringBuffer sb = new StringBuffer();
        sb.append(localFilePath);
        if (localFilePath.endsWith(File.separator)) {
            sb.append(File.separator);
        }
        sb.append(File.separator).append(targetName);
        try {
            fileWriter = new FileWriter(sb.toString(), false);
            bf = new BufferedWriter(fileWriter);
            bf.write(content);
            bf.flush();
            log.info("保存文件成功,位置为:"+sb.toString());
        } catch (IOException e) {
            log.error("写入txt文件出错", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
        return targetName;
    }

    /**
     * 解析txt文件到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
     * @param filepath 文件路径
     * @param spliteType 分割符号，但第一个字符不能为分隔符
     * @return
     */
    public static List<List<String>> readTxtFile(String filepath,String spliteType) {
        List<List<String>> list = new ArrayList<>();
        InputStreamReader input = null;
        BufferedReader reader = null;
        String line = null;
        try {
            input = new InputStreamReader(new FileInputStream(filepath));
            reader = new BufferedReader(input);
            while ((line = reader.readLine()) != null){
                String [] linelist;
                if(line.indexOf("`")!=0){
                    linelist = line.split(spliteType);
                }else{
                    linelist = line.split(",`");
                    linelist[0] = linelist[0].substring(1);
                }
                List<String> cells = Arrays.asList(linelist);
                list.add(cells);
            }
        } catch (FileNotFoundException e) {
            log.error("文件不存在",e);
        } catch (IOException e) {
            log.error("读取文件异常",e);
        } finally {
            if (input != null) {
                try {
                    reader.close();
                    input.close();
                } catch (IOException e) {
                    log.error("流关闭异常",e);
                }
            }
        }
        return list;
    }

}
