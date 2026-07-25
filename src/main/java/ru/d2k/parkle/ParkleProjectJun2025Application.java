package ru.d2k.parkle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class ParkleProjectJun2025Application {

	public static void main(String[] args) {
		SpringApplication.run(ParkleProjectJun2025Application.class, args);

		startLogs();
	}

	private static void startLogs() {
		System.out.println("App was started!");
	}
}