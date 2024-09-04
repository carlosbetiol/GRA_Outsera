package com.outsera.goldenraspberryawards;

import com.outsera.goldenraspberryawards.core.io.Base64ProtocolResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.TimeZone;

@SpringBootApplication
public class GoldenRaspberryAwardsApplication {

	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		var app = new SpringApplication(GoldenRaspberryAwardsApplication.class);
		app.addListeners(new Base64ProtocolResolver());

		APPLICATION_CONTEXT = app.run(args);

	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}

}
