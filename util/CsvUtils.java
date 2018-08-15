package com.sunyard.aps.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Write class comments here
 * <p/>
 * User: liumeng
 * Date: 2017/10/23 14:07
 * version $Id: CsvUtils.java, v 0.1 Exp $
 */


public class CsvUtils {

    private static Logger log = LoggerFactory.getLogger(CsvUtils.class);

    static boolean flag = false;

    /**
     * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
     */
    public static List<List<String>> readCSVFile(String filePath) {
         InputStreamReader fr = null;
         BufferedReader br = null;
        String rec = null;// 一行
        String str;// 一个单元格
        List<List<String>> listFile = new ArrayList<List<String>>();
        try {
            fr = new InputStreamReader(new FileInputStream(filePath),"gbk");
            br = new BufferedReader(fr);
            // 读取一行
            while ((rec = br.readLine()) != null) {
                rec=rec.trim();
                if(rec.indexOf("#")!=0){
                    String[] zifus=rec.split(",");
                    List<String> cells =Arrays.asList(zifus);
                    listFile.add(cells);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(fr != null){
                    fr.close();
                }
                if(br != null) {
                    br.close();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return listFile;
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

    public static void main(String[] args) {
//        CsvUtils test = new CsvUtils();
//        test.deleteDirectory("E:\\xiazai");
        List<List<String>> list = new ArrayList<>();
        List<String> strs = new ArrayList<>();
        strs.add("123");
        strs.add("321");
        strs.add("12345");
        strs.add("54321");
        list.add(strs);
       File file =  createAlipayCSVFile(list,"D:\\sunyard","asert","1234567qwe","20181102");
        System.out.println(file.getPath());
    }

    public static File createAlipayCSVFile(List<List<String>> exportData, String outPutPath,
                                     String fileName,String pid,String transDate) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            //定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);
            //写第一行
            csvFileOutputStream.write("#支付宝业务明细查询-模拟");
            //写第二行
            csvFileOutputStream.write("#账号："+pid);
            //写第三行
            String endDate=DateUtil.addDate(transDate, 1, "yyyyMMdd");
            csvFileOutputStream.write("#起始日期:["+transDate+"] 终止日期:["+endDate+"]");
            csvFileOutputStream.newLine();
            csvFileOutputStream.write("#-----------------------------------------业务明细列表----------------------------------------");
            csvFileOutputStream.newLine();
            csvFileOutputStream.write("支付宝交易号,商户订单号,订单金额（元）,商户识别号,交易方式,支付宝交易号");
            csvFileOutputStream.newLine();
            // 写入文件内容
            for(int i=0;i<exportData.size();i++){
                List<String> row=exportData.get(i);
                for(int j=0;j<row.size();j++){
                    String cell=row.get(j);
                    csvFileOutputStream.write(cell);
                    if(j!=(row.size()-1)){
                        csvFileOutputStream.write(",");
                    }
                }
                csvFileOutputStream.newLine();

            }
            csvFileOutputStream.write("#----------------------------------------业务明细列表结束-------------------------------------");
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    public static <T>String listToCvs(List<T> list , LinkedHashMap<String,String> titleMap ,String savePath,String fileName){
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = new File(savePath+fileName);
            csvFile.createNewFile();
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(csvFile), "GBK"),1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = titleMap.entrySet().iterator();
                 propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry =
                        (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(
                        "" + (String) propertyEntry.getValue() != null ?
                                (String) propertyEntry.getValue() : "" + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = list .iterator(); iterator.hasNext();) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = titleMap.entrySet().iterator();
                     propertyIterator.hasNext();) {
                    java.util.Map.Entry propertyEntry =
                            (java.util.Map.Entry) propertyIterator.next();
                    csvFileOutputStream.write((String) BeanUtils.getProperty(
                            row, (String) propertyEntry.getKey()));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile.getPath();
    }

    public static String readCsv(String filePath){
        File file = new File(filePath);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            String content;
            while((content=bufferedReader.readLine())!=null) {
                stringBuilder.append(content).append("\r\n");
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            log.info("文件不存在",e);
        }catch (IOException e) {
            log.info("读取文件异常",e);
        }
        return null;

    }
}
