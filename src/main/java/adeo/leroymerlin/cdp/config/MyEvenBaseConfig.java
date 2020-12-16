package adeo.leroymerlin.cdp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The application's base configuration that provides :
 * <ul>
 * <li>App base package to scan for annotated components reading (Autodiscovery).</li>
 * <li>App configuration files (properties files) localization.</li>
 * <li>App persistent object's base package.</li>
 * <li>Enabled JPA repositories.</li>
 * <li>Enabled Transaction Management for JPA repositories.</li>
 * </ul>
 * 
 * @author Vincent Otchoun
 */
@Configuration
@ComponentScan(basePackages = "adeo.leroymerlin.cdp")
@PropertySource(value = { "classpath:application.yml" }, ignoreResourceNotFound = false)
@EntityScan("adeo.leroymerlin.cdp.model")
@EnableJpaRepositories(basePackages = "adeo.leroymerlin.cdp.repository")
@EnableTransactionManagement
public class MyEvenBaseConfig
{
    //
}
