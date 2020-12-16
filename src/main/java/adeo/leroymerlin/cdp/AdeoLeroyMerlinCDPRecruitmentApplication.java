package adeo.leroymerlin.cdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import adeo.leroymerlin.cdp.config.MyEventBaseConfig;

@SpringBootApplication
@Import(MyEventBaseConfig.class)
public class AdeoLeroyMerlinCDPRecruitmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdeoLeroyMerlinCDPRecruitmentApplication.class, args);
	}
}
