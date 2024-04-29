package in.nucleusteq.plasma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
public class TokenServiceApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		SpringApplication.run(TokenServiceApplication.class, args);
	}

}
