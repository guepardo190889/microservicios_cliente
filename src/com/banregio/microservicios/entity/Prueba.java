package com.banregio.microservicios.entity;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.banregio.microservicios.Microservicio;

public class Prueba {
	public static void main(String[] args) {
		Microservicio<String, String> micro = new Microservicio<String, String>();

		URI uri = micro.buildURI("http://localhost:8080", "testString", null, null);

		HttpHeaders headers = micro.getSimpletHeaders();

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

		ParameterizedTypeReference<String> responseParameterizedType = new ParameterizedTypeReference<String>() {
		};

		String respuesta = micro.request(uri, requestEntity, HttpMethod.GET, responseParameterizedType, true);

		System.out.println("respuesta: " + respuesta);
	}
}
