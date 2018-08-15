package com.sunyard.ipp.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据导出到excel的工具类
 */
public class ExportExcelUtils<T> {
    /**
     * @param workbook  传入创建excel表格的对象
     * @param title  excel表名称
     * @param headers  excel中导入数据的字段名称，格式： 中文名称-属性名
     * @param dataset  导出的数据集合
     * @param sheetNum  表的下标
     */
    public void exportExcel(HSSFWorkbook workbook, String title, String[] headers, Collection<T> dataset,int sheetNum){
        HSSFSheet sheet = workbook.createSheet();
        //设置表格
        workbook.setSheetName(sheetNum,title);
        sheet.setDefaultColumnWidth(20);

        Iterator<T> it = dataset.iterator();
        int rowNum = 0;
        T dataPo = null;
        try{
            while(it.hasNext()){
                HSSFRow row = sheet.createRow(rowNum);
                //第一次设置表头，所以游标不下滑
                if(rowNum != 0){
                    dataPo = (T)it.next();
                }
                for(int colNum = 0;colNum < headers.length;colNum++){
                    HSSFCell cell = row.createCell(colNum);
                    String[] header = headers[colNum].split("-");
                    //设置第一行表头
                    if(rowNum == 0){
                        cell.setCellValue(new HSSFRichTextString(header[0]));
                    }else{
                        //设置其他行的数据
                        Field field = dataPo.getClass().getDeclaredField(header[1]);
                        field.setAccessible(true);
                        //交易类型设置为中文
                        if(StringUtils.equals(header[1],"transType")){
                            String transType = (String)field.get(dataPo);
                            if(StringUtils.equals(transType,"1001")){
                                cell.setCellValue(new HSSFRichTextString("支付收款"));
                            }else if(StringUtils.equals(transType,"2001")){
                                cell.setCellValue(new HSSFRichTextString("支付收款撤销"));
                            }else if(StringUtils.equals(transType,"2002")){
                                cell.setCellValue(new HSSFRichTextString("支付收款退款"));
                            }
                        }else if(StringUtils.equals(header[1],"absBusiType")){
                            //收单业务类型设置为中文
                            String absBusiType = (String)field.get(dataPo);
                            if(StringUtils.equals(absBusiType,"ALIPAY")){
                                cell.setCellValue(new HSSFRichTextString("支付宝支付"));
                            }else if(StringUtils.equals(absBusiType,"WECHAT")){
                                cell.setCellValue(new HSSFRichTextString("微信支付"));
                            }else if(StringUtils.equals(absBusiType,"POS")){
                                cell.setCellValue(new HSSFRichTextString("POS支付"));
                            }else if(StringUtils.equals(absBusiType,"NET")){
                                cell.setCellValue(new HSSFRichTextString("网关支付"));
                            }
                        } else{
                            Object value = field.get(dataPo);
                            cell.setCellValue(new HSSFRichTextString(value.toString()));
                        }
                    }
                }
                rowNum++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 与exportExcel方法不同的是传入的是map表示的要生成excel的表头,以避免字段与数据不对应
     * @param workbook   传入创建excel表格的对象
     * @param title   excel表名称
     * @param fieldMap   excel中导入数据的字段名称
     * @param list   导出的数据集合
     * @param sheetNum   表的下标
     * @param outputStream   输出流
     */
    public void exportExcel2(HSSFWorkbook workbook, String title, LinkedHashMap<String, String> fieldMap, Collection<T> list, int sheetNum, OutputStream outputStream) {
        try {
            // 定义存放英文字段名和中文字段名的数组
            int size = fieldMap.size();
            String[] enFields = new String[size];
            String[] cnFields = new String[size];
            // 填充数组
            int count = 0;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                enFields[count] = entry.getKey();
                cnFields[count] = entry.getValue();
                count++;
            }
            HSSFSheet sheet = workbook.createSheet();
            //设置表格
            workbook.setSheetName(sheetNum, title);
            sheet.setDefaultColumnWidth(20);
            //生成行
            HSSFRow row = sheet.createRow(0);
            // 填充表头
            for (int i = 0; i < cnFields.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(cnFields[i]);
                cell.setCellValue(text);
            }
            // 填充内容
            //遍历集合数据
            Iterator<T> it = list.iterator();
            int index = 0;
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T dataVo = (T) it.next();
                //通过反射获取获取对象所有字段值
                Field[] fields = dataVo.getClass().getDeclaredFields();
                HSSFCell cell = null;
                for (int i = 0; i < enFields.length; i++) {
                    for(int j = 0; j < fields.length; j++){
                        if(enFields[i].equals(fields[j].getName())){
                            cell = row.createCell(i);
                            fields[j].setAccessible(true);
                            Object value = fields[j].get(dataVo);
                            cell.setCellValue(value == null ? null : value.toString());
                            break;
                        }else{
                            cell = row.createCell(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
