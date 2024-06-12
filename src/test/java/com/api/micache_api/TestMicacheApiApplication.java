package com.api.micache_api;

import org.springframework.boot.SpringApplication;

public class TestMicacheApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicacheApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
