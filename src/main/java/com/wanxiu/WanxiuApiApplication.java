package com.wanxiu;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WanxiuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WanxiuApiApplication.class, args);
	}

	@Bean
	Gson gson(){
		return new Gson();
	}

//	@Bean
//	RestTemplate restTemplate(){return new RestTemplate();}

}

