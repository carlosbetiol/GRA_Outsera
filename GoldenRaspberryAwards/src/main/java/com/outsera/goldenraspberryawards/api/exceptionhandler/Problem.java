package com.outsera.goldenraspberryawards.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problem {

	@Schema(example= "400")
	private Integer status;

	@Schema(example= "2023-09-11T03:46:02.70844Z")
	private OffsetDateTime timestamp;

	@Schema(example= "https://urlsite/invalid-data")
	private String type;

	@Schema(example= "Invalid data")
	private String title;

	@Schema(example= "One or more fields are invalid. Please complete correctly and try again.")
	private String detail;

	@Schema(example= "One or more fields are invalid. Please complete correctly and try again.")
	private String userMessage;

	@Schema(example= "List of fields that generated the error")
	private List<Object> objects;

	@Schema(name="ProblemObject")
	@Getter
	@Builder
	public static class Object {

		@Schema(example="email")
		private String name;

		@Schema(example="Email is required")
		private String userMessage;
		
	}
	
}
