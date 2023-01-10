package com.ishift.auction.configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources(value = {
        @PropertySource("classpath:config/global.properties"),
        @PropertySource("classpath:config/jdbc-#{systemProperties['spring.profiles.active']}.properties")
})
public class PropertiesWithJavaConfiguration {

}