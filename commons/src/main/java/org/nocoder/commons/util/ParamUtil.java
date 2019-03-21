package org.nocoder.commons.util;

import org.apache.commons.lang.Validate;

/**
 * Created by wangwenqiang on 2018/6/15.
 */
public class ParamUtil {

    private static ThreadLocal<Object> threadLocal = new ThreadLocal();

    public static void put(Object o){
        threadLocal.set(o);
    }

    public static Object get(){
       return threadLocal.get();
    }

    public static String transitionBarNumber(String param){
        param=param==null?param:param.toUpperCase().trim();
        Validate.notEmpty(param,"条码号不能为空");
        String fullBarNumber=param,hallCode="",barNumber="";
        if(param.length()>4){
            if(param.substring(0,4).matches("[A-Z]{4}")){
                hallCode=param.substring(0,4);
            }
            if(param.substring(0,5).matches("[A-Z]{5}")){
                hallCode=param.substring(0,5);
            }
            barNumber=param.substring(hallCode.length());
            if(hallCode.length()>0 && barNumber.length()>0 && "-".equals(barNumber.substring(0,1))){
                barNumber=barNumber.substring(1);
            }
        }else{
            barNumber=param;
        }
        if(hallCode.length()>0){
            if(barNumber.length()>0 && barNumber.length()<=7 && barNumber.matches("[0-9]+")){
                String code="";
                int zeroNum = 7 - barNumber.length();
                for (int i = 0; i < zeroNum; i++) {
                    code += "0";
                }
                code += barNumber;
                barNumber=code;
                fullBarNumber=hallCode+"-"+barNumber;
            }
        }
        return fullBarNumber;
    }

}
