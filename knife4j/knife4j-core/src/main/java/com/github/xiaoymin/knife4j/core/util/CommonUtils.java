/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.github.xiaoymin.knife4j.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/***
 *
 * @since:swagger-bootstrap-ui 1.8.5
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2018/10/11 13:47
 */
public class CommonUtils {


    static Logger logger= LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String genSupperName(String name){
        String supperName="";
        if (name!=null&&!"".equals(name)){
            if (name.length()==1){
                supperName=name.toUpperCase();
            }else{
                supperName=name.substring(0,1).toUpperCase()+name.substring(1);
            }
        }
        return supperName;
    }

    public static String UpperCase(String str){
        StringBuffer  aa=new StringBuffer();
        int index = 0;
        int index22 = 0;
        int len = str.length();
        begin:
        for (int i = 1; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                //判断下一个是大写还是小写
                if(Character.isUpperCase(str.charAt(i+1))){
                    aa.append(str.substring(index, i)).append("");
                }else {
                    aa.append(str.substring(index, i)).append(" ");
                }
                index = i;
                index22=index22+1;
                continue begin;
            }
            index22=0;
        }
        aa.append(str.substring(index, len));
        return aa.toString();
    }


    public static byte[] readBytes(File file) {
        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new RuntimeException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        int readLength;
        try {
            in = new FileInputStream(file);
            readLength = in.read(bytes);
            if(readLength < len){
                throw new IOException("File length is ["+len+"] but read ["+readLength+"]!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuiltly(in);
        }

        return bytes;
    }

    public static byte[] readBytes(InputStream ins){
        if (ins==null){
            return null;
        }
        ByteArrayOutputStream byteOutArr=new ByteArrayOutputStream();
        int r=-1;
        byte[] bytes = new byte[1024*1024];
        try {
            while ((r=ins.read(bytes))!=-1){
                byteOutArr.write(bytes,0,r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuiltly(ins);
        }
        return byteOutArr.toByteArray();
    }

    public static void closeQuiltly(InputStream ins){
        if (ins!=null){
            try {
                ins.close();
            } catch (IOException e) {
                //logger.error(e.getMessage(),e);
                logger.warn("(Ignores) Failed to closeQuiltly,message:{}",e.getMessage());
            }
        }
    }

    public static void closeQuiltly(Reader reader){
        if (reader!=null){
            try {
                reader.close();
            } catch (IOException e) {
                logger.warn("(Ignores) Failed to closeQuiltly,message:{}",e.getMessage());
            }
        }
    }




}
