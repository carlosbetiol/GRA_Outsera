package com.outsera.goldenraspberryawards.core.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

@UtilityClass
public class ResourceUriHelper {

	public static void addUriInResponseHeader(Object resourceId) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(resourceId).toUri();
		
		HttpServletResponse response = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
		
		response.setHeader(HttpHeaders.LOCATION, uri.toString());
	}

	public static String getCurrentUrl() throws MalformedURLException, URISyntaxException {
		HttpServletRequest request =
				((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
						.getRequest();
		URL url = new URL(request.getRequestURL().toString());
		String host  = url.getHost();
		String userInfo = url.getUserInfo();
		String scheme = url.getProtocol();
		int port = url.getPort();
		URI uri = new URI(scheme,userInfo,host,port,null,null,null);
		return uri.toString();
	}

}
