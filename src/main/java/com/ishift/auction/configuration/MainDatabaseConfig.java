package com.ishift.auction.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 홈페이지 메인 Database Config
 * @author Yuchan
 */
@Configuration
public class MainDatabaseConfig {
	@Bean(name = "dataSource")
	@Profile("local")
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSourceLocal() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "dataSource")
	@Profile({"develop","production"})
	public DataSource dataSource() throws NamingException {
		JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		lookup.setResourceRef(true);
		DataSource ds = lookup.getDataSource("jdbc/nhlva");
		
		return ds;
	}
    /**
     * sessionfactory
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setConfigLocation(resolver.getResource("classpath:database/mybatis-config.xml"));
        sessionFactoryBean.setMapperLocations(resolver.getResources("classpath:database/mapper/tibero/**/*.xml"));
        sessionFactoryBean.setTypeAliasesPackage("com.ishift.auction.vo");
        sessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactoryBean.getObject();
    }

    /**
     * sqlsession
     */
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * transaction manager
     */
    @Bean(name = "txManager")
    public PlatformTransactionManager txManager(@Qualifier("dataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true); // nested
        return dataSourceTransactionManager;
    }


}
