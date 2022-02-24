
package com.ishift.auction.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.service.auction.AuctionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileDownloadController {
	@Autowired
	private AuctionService auctionService;
	
	
	@RequestMapping(value = "/auctDownload")
    public void filedownload(HttpServletResponse response, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
 
        URL url = null;
        HttpURLConnection con = null;
        try {
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("osType", "EXE");
			
			final Map<String, Object> appVersion = auctionService.selectAppVersionInfo(map);
            
            String fileName = "NHControllerSetup.exe";

            String version =((String)appVersion.getOrDefault("MAX_VERSION","1.0.0")).replace(".", "_");
            
            //String fileUrl = "https://xn--e20bw05b.kr/static/file/WhaleSetup.exe";            
            String fileUrl = "https://xn--e20bw05b.kr/static/file/NHControllerSetup_"+version+".exe";
            String httpsResult = "";
            
            url = new URL(fileUrl);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Cache-Control", "no-cache");
            int responseCode = con.getResponseCode();

            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            
            String header = request.getHeader("User-Agent");
 
            if(header.contains("MSIE") || header.contains("Trident")) {
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-Disposition", "attachment; filename="+ fileName +";");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
                response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName +"\"");
            }
            response.setHeader("Pragma", "no-cache;");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Transfer-Encoding", "binary");
            
            try {

                inputStream = con.getInputStream();
                outputStream = response.getOutputStream();

                int bytesRead = -1;
                byte[] buffer = new byte[4096];

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                   outputStream.write(buffer, 0, bytesRead);
                }

            } catch(Exception e) {
                log.error(e.getMessage(),e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                  if(outputStream != null) outputStream.close(); 
                  if(inputStream != null) inputStream.close();
            }            
        	if(con != null) con.disconnect();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
        	if(con != null) con.disconnect();
        }        
    }
}
