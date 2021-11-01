package com.ishift.auction.util;


import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created com.tirablue.generator.Util by photoprogrammer on 10/10/2018
 */
@Component
@Configuration
public class Util {


    public static String changeCaseFormat(String inputText, CaseFormat input, CaseFormat output){
        return input.to(output, inputText);
    }
    public static boolean isFreemarkerVariable()
    {

        //예약어이면
        //UUID이면

        return false;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    @Value("${spring.mail.smtp.host}")
    public String host;

    @Value("${spring.mail.smtp.port}")
    public Integer port;

    @Value("${spring.mail.smtp.auth}")
    public String isAuth;

    @Value("${spring.mail.smtp.ssl.enable}")
    public String isSsl;





    public Date getCurrentDate(){

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        System.out.println(formatter.format(date));

        return (new Date(System.currentTimeMillis()));

    }

    public String getCurrentDateString(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public String getCurrentTimeString(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    public ModelAndView checkAuth(HttpServletRequest request, String authCode) throws Exception {
        Map<String, Object> result = new HashMap<>();
        ModelAndView mav = new ModelAndView();

        mav.addObject("success", true);
        mav.setViewName("lottecmManager/home/noAuth");

        request.getSession().setAttribute("userIp", getClientIP(request)  );

        if(!getClientIP(request).equals(request.getSession().getAttribute("userIp").toString())){
            mav.addObject("success", false);
            mav.addObject("message", "비정상적인 접근입니다.");
            LOGGER.debug("접속 아이피가 다릅니다.=> "+getClientIP(request)+ "!="+request.getSession().getAttribute("userIp").toString());
        }


        if(request.getSession().getAttribute("userAuth") !=null && request.getSession().getAttribute("userAuth").toString().indexOf(authCode)==-1) {

            mav.addObject("success", false);
            mav.addObject("message", "권한이 존재하지 않습니다.");
        }

        return mav;

    }

    public String replaceDot(String input){

        String returnValue ="";
        returnValue = input.replace(".","");
        returnValue = returnValue.replace("/","");
        return returnValue;
    }
    public String getClientIP(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // 마스킹 처리
    public String masking(String input){

        String returnValue = "";

        for(int i=0; i< input.length();i++){
            if(i%2==0){
                returnValue = returnValue+input.substring(i,i+1);
            }else{
                returnValue = returnValue+"*";
            }
        }
        return returnValue;
    }

    public void sendPasswordMail(String receiver, String sender, String title, String content) throws Exception{



        Properties prop = new Properties();
        prop.put("mail.smtp.host",host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", isAuth);
        prop.put("mail.smtp.ssl.enable", isSsl);
        prop.put("mail.smtp.starttls.enable", "true");
//        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");







        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("photoprogrammer@gmail.com", "dlQmsdl2!");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));

            //수신자메일주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

            // Subject
            message.setSubject(title); //메일 제목을 입력

            message.setContent(content,"text/html; charset=UTF-8");
            Transport.send(message); ////전송
            LOGGER.debug("message sent successfully...");
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            LOGGER.debug("message sent fail AddressException...");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            LOGGER.debug("message sent fail MessageException...");
        }
    }


    public String convertFileName(String input){
        String returnValue = Normalizer.normalize(input, Normalizer.Form.NFC);
        return returnValue;
    }


    public Map<String, Object> addUserInfo(HttpServletRequest request, Map<String, Object> input){
        Map<String, Object> result = new HashMap<>();

        result = input;

        result.put("createUuid",request.getSession().getAttribute("uuid").toString());
        result.put("createNm",request.getSession().getAttribute("userName").toString());
        result.put("createEmail",request.getSession().getAttribute("userId").toString());
        result.put("createDt",getCurrentDate());

        return result;
    }
//    public void logInsert(HttpServletRequest request, HashMap<String,Object> map ) throws Exception{
////        HashMap<String,Object> insertMap = new HashMap<String,Object>();
////        map.put("logUserId", map.get("logUserId").toString());
////        map.put("logUserNm", request.getSession().getAttribute("userNm").toString());
////        map.put("logUserGroup", request.getSession().getAttribute("userGroup").toString());
//
//        if(map.get("logMessage")!=null) {
//            map.put("logLoginFrom", request.getRequestURI() + "(" + map.get("logMessage").toString() + ")");
//        } else {
//            map.put("logLoginFrom", request.getRequestURI());
//        }
//        map.put("logLoginIp", getClientIP(request));
//        map.put("logLoginDt", getCurrentDate());
//
//
//        logTbService.insert(map);
//    }

    public Map<String, Object> validatePassword( String inputPassword)
    {


        Map<String, Object> result = new HashMap<>();
        result.put("success", true);

        if(inputPassword.length() ==0){
            result.put("message","비밀번호를 올바르게 입력하지 않았습니다.");
            result.put("success", false);
            return result;
        }
        if (inputPassword.length() < 8) {
            result.put("message","비밀번호는 영문, 숫자, 특수문자포함 8자이상 입력하세요.");
            result.put("success", false);
            return result;
        }
        Pattern pattern1 = Pattern.compile("[0-9]");
        Pattern  pattern2 = Pattern.compile("[a-zA-Z]");
        Pattern  pattern3 = Pattern.compile("[~!@#$%<>^&*]");
        if(!pattern1.matcher(inputPassword).find()||!pattern2.matcher(inputPassword).find()||!pattern3.matcher(inputPassword).find()){
            result.put("message","비밀번호는 영문, 숫자, 특수문자포함 8자이상 입력하세요.");
            result.put("success", false);
            return result;
        }
        if (inputPassword.length() > 40) {
            result.put("message","비밀번호는 40byte까지 입력가능합니다.");
            result.put("success", false);
            return result;
        }
        // 연속적인 숫자
        // 키보드의 연속적인 배열
        // 사용자 신상 및 회사명과 관계된 비밀번호(생일, 전화번호 등)
        // 계정명(ID)과 동일한 비밀번호
        // 이전에 사용한 비밀번호의 재사용

        Boolean pw_passed = true;
        Integer SamePass_0 = 0; // 동일문자 카운트
        Integer SamePass_1 = 0; // 연속성(+) 카운드
        Integer SamePass_2 = 0; // 연속성(-) 카운드
        for(int i=0; i<inputPassword.length(); i++) {
            Integer chr_pass_0=0;
            Integer chr_pass_1=0;
            Integer chr_pass_2=0;

            if(i >= 2) {
                chr_pass_0 = inputPassword.indexOf(i-2);
                chr_pass_1 = inputPassword.indexOf(i-1);
                chr_pass_2 = inputPassword.indexOf(i);

                // 동일문자 카운트
                if((chr_pass_0 == chr_pass_1) && (chr_pass_1 == chr_pass_2)) {
                    SamePass_0++;
                } else {
                    SamePass_0 = 0;
                }

                // 연속성(+) 카운드
                if (chr_pass_0 - chr_pass_1 == 1 && chr_pass_1 - chr_pass_2 == 1) {
                    SamePass_1++;
                } else {
                    SamePass_1 = 0;
                }
                // 연속성(-) 카운드
                if (chr_pass_0 - chr_pass_1 == -1 && chr_pass_1 - chr_pass_2 == -1) {
                    SamePass_2++;
                } else {
                    SamePass_2 = 0;
                }
            }

        /*
        if(SamePass_0 > 0) {
            alert("동일문자를 3자 이상 연속 입력할 수 없습니다.");
            pw_passed = false;
        }
        */

            if(SamePass_1 > 0 || SamePass_2 > 0 ) {
                result.put("message","영문, 숫자는 3자 이상 연속 입력할 수 없습니다.");
                result.put("success", false);
                return result;
//
//                pw_passed = false;
            }

//            if (!pw_passed) {
//                return false;
//                // break;
//            }

        }
        return result;
    }
    
    public String getToday(String format) {
    	if(format.isEmpty()) format = "yyyyMMdd";
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
