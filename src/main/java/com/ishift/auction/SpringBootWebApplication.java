/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.ishift.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication
public class SpringBootWebApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
    	SpringApplication app = new SpringApplication(SpringBootWebApplication.class);
    	app.run(args);
    }
}
