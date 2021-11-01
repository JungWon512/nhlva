package com.ishift.auction.util;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created com.tirablue.generator.Util by photoprogrammer on 10/10/2018
 */
@Component
public class DateUtil {

    public static boolean isFreemarkerVariable()
    {

        //예약어이면
        //UUID이면

        return false;
    }
    //private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);


    


    public static Date getCurrentDate(){



        return (new Date(System.currentTimeMillis()));

    }

    public String getCurrentDateString(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public static String addSlashYYYYMMDD(String YYYYYMMDD){
        String returnValue = YYYYYMMDD;
        try{
            if(YYYYYMMDD.length()<8){
                returnValue = "2020/01/01";
            } else {
                returnValue = YYYYYMMDD.substring(0,4)+"/"+YYYYYMMDD.substring(5,6)+"/"+YYYYYMMDD.substring(7,8);
            }

        } catch(Exception ex){
            returnValue = "2020/01/01";
        }
        return returnValue;
    }
    public static String removeSlashYYYYMMDD(String YYYYYMMDD){
        String returnValue = YYYYYMMDD;
        try{
            if(YYYYYMMDD.length()<10){
                returnValue = "20200101";
            } else {
                returnValue =  YYYYYMMDD.replace("/","");
            }

        } catch(Exception ex){
            returnValue = "20200101";
        }
        return returnValue;
    }
}
