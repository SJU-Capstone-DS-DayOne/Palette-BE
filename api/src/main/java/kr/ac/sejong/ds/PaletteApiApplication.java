package kr.ac.sejong.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PaletteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaletteApiApplication.class, args);
	}

}
