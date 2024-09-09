package com.outsera.goldenraspberryawards.core.database;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@Component
@ConfigurationProperties("awardsapi.database")
public class DatabaseProperties {

	@NotBlank
	private String host;

	@NotNull
	private Integer port = 3306;

	@NotBlank
	private String dbname;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	private String csvFilePath;

}
