package com.banregio.microservicios;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sethluis
 *
 */
public class Microservicio {

	/**
	 * Genera una URI a partir de los datos que la componen
	 * 
	 * @param urlHost     - url del host donde se encuentra el microservicio
	 *                    incluyendo el protocolo:
	 *                    https://call-center-swat.brmdocp.banregio.com
	 * @param resourceUrl - recurso a consultar: ivr/nip/encriptacion
	 * @param params      - parámetros que se usan en la uri: [clave], [cuenta]
	 * @param values      - valores de cada parámetro, un valor por cada parámetro:
	 *                    [123], [123]
	 * @return
	 */
	public URI makeURI(String urlHost, String resourceUrl, List<String> params, List<String> values) {
		StringBuilder paramsUrl = new StringBuilder("");

		if (params != null && !params.isEmpty()) {
			paramsUrl.append("?");
			for (int i = 0; i < params.size(); i++) {
				paramsUrl.append(params.get(i)).append("=").append(values.get(i));
			}
		}

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(urlHost.concat("/").concat(resourceUrl).concat(paramsUrl.toString()));

		return builder.build().toUri();
	}

	/**
	 * Crea un HttpHeaders con las cabeceras indicadas
	 * 
	 * @param token
	 * @return
	 */
	public HttpHeaders getHeaders(String accept, String contentType, String authorization) {
		HttpHeaders headers = new HttpHeaders();

		if (accept != null && !accept.isEmpty()) {
			headers.set("Accept", accept);
		}

		if (authorization != null && !authorization.isEmpty()) {
			headers.set("Authorization", authorization);
		}

		if (contentType != null && !contentType.isEmpty()) {
			headers.set("Content-Type", contentType);
		}

		return headers;
	}

	/**
	 * Crea un HttpHeaders con cabecera Accept y Content-Type=application/json
	 * 
	 * @return
	 */
	public HttpHeaders getDefaultHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON.toString());
		headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());

		return headers;
	}

	public String request(URI uri, String token) {

		return "";
	}

	public void invocacion(String claveEncriptar, String numCuenta) {
		String urlHost = "https://call-center-swat.brmdocp.banregio.com/";
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJhcHAiOiJjYWxsLWNlbnRlciIsInNlYyI6ZmFsc2UsInN1YiI6ImNhbGwtY2VudGVyIiwiYXVkIjoidW5rbm93biIsImlzcyI6IkJhbnJlZ2lvIiwidGlwIjp0cnVlLCJpYXQiOjE1NzkxMDM0NDYsImp0aSI6IkxIay9OWXdsV3RIUEljQlIzMkZ6bVF0OXFrclhNMlliTko1ZVhqM3UycTZrSFFiRjkxekJpbk1lTUFDSjZ5WU8ifQ.7J9iTbIxlZork-rxBNOQ3RWCpYCawiYT7NYB_QyU6qiG_kJuMGsbF1DLl6N3iXfY4vV-3TaA_xemQMfiAV0bfA";

		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(urlHost.concat("/ivr/nip/encriptacion".concat("?clave=")).concat(claveEncriptar)
						.concat("&cuenta=").concat(numCuenta));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json;charset=utf-8");
		headers.set("Authorization", token);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, requestEntity,
				String.class);

		String resultado = "";

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(response.getBody());

			JsonNode respuesta = root.path("respuesta");

			System.out.println("json: " + response.getBody());

			resultado = respuesta.asText() != null && !respuesta.asText().isEmpty() ? respuesta.asText() : "";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Resultado= " + resultado);
	}

	public static void main(String[] args) {
		System.out.println((MediaType.APPLICATION_JSON.toString()));

		Microservicio micro = new Microservicio();

		ArrayList<String> params = new ArrayList<String>();
		params.add("clave");
		params.add("cuenta");

		ArrayList<String> values = new ArrayList<String>();
		values.add("12345");
		values.add("123345");

		URI url = micro.makeURI("https://call-center-swat.brmdocp.banregio.com", "/ivr/nip/encriptacion", params,
				values);

		String token = "eyJhbGciOiJIUzUxMiJ9.eyJhcHAiOiJjYWxsLWNlbnRlciIsInNlYyI6ZmFsc2UsInN1YiI6ImNhbGwtY2VudGVyIiwiYXVkIjoidW5rbm93biIsImlzcyI6IkJhbnJlZ2lvIiwidGlwIjp0cnVlLCJpYXQiOjE1NzkxMDM0NDYsImp0aSI6IkxIay9OWXdsV3RIUEljQlIzMkZ6bVF0OXFrclhNMlliTko1ZVhqM3UycTZrSFFiRjkxekJpbk1lTUFDSjZ5WU8ifQ.7J9iTbIxlZork-rxBNOQ3RWCpYCawiYT7NYB_QyU6qiG_kJuMGsbF1DLl6N3iXfY4vV-3TaA_xemQMfiAV0bfA";

		HttpHeaders headers = micro.getHeaders(MediaType.APPLICATION_JSON.toString(),
				MediaType.APPLICATION_JSON.toString(), token);

		System.out.println(url.toString());
	}

}
