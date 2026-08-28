package fr.stefangeorgesco.rsockettradingservice;

import org.springframework.boot.SpringApplication;

public class TestRSocketTradingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RSocketTradingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
