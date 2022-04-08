package Test.Demo.td.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class SpringJpaConfiguration {
    
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		DataSourceProperties dataSourceProperties = new DataSourceProperties();
		return dataSourceProperties;
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		DataSourceProperties dataSourceProperties = dataSourceProperties();
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

}