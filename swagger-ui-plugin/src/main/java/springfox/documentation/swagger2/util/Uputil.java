package springfox.documentation.swagger2.util;

/**
 * @Copyright: Zhejiang Drore Technology Co., Ltd  2018 <br/>
 * @Desc: <br/>
 * @ProjectName: e-commerce-base <br/>
 * @Date: 2018/9/20 23:02 <br/>
 * @Author: baoec@drore.com
 */
public class Uputil {
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
}
