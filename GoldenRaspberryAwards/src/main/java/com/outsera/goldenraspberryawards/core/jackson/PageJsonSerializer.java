package com.outsera.goldenraspberryawards.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {

	@Override
	public void serialize(Page<?> page, JsonGenerator gen, 
			SerializerProvider serializers) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("content", page.getContent());
		map.put("size", page.getSize());
		map.put("totalElements", page.getTotalElements());
		map.put("totalPages", page.getTotalPages());
		map.put("number", page.getNumber());

		serializers.defaultSerializeValue(map, gen);

	}

}
