package in.nucleusteq.plasma.configration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * DatabaseConfig configures the data source for the application.
 */
@Configuration
public class DatabaseConfig {

    /**
     * The environment for accessing application properties.
     */
    @Autowired
    private Environment environment;

    /**
     * Creates and configures the data source bean.
     *
     * @return The configured data source.
     * @throws Exception If an error occurs while configuring the data source.
     */
    @Bean
    public DataSource getDataSource() throws Exception {
        String userName = environment.getProperty("spring.datasource.username");
        String url = environment.getProperty("spring.datasource.url");
        String password = environment.getProperty("spring.datasource.password");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        dataSourceBuilder.url(url);
        return dataSourceBuilder.build();
    }

}
