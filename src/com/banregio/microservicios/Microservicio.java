package com.banregio.microservicios;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.banregio.microservicios.entity.DTO;

/**
 * Clase con métodos de utilería para invocar servicios REST usando RestTemplate
 * 
 * @author sethluis
 *
 */
public class Microservicio<REQUEST_CLASS, RESPONSE_CLASS> {

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
	public URI buildURI(String urlHost, String resourceUrl, List<String> params, List<String> values) {
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
	 * Devuelve un HttpHeaders con las cabeceras indicadas si no están vacías
	 * 
	 * @param accept
	 * @param contentType
	 * @param authorization
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
	public HttpHeaders getSimpletHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON.toString());
		headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());

		return headers;
	}

	/**
	 * Devuelve un convertidor de JSON con la propiedad de ignorar las propiedades
	 * desconocidas
	 * 
	 * @return
	 */
	private MappingJacksonHttpMessageConverter getMessageConverterIgnoreUnknownProperties() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.setObjectMapper(mapper);

		return converter;
	}

	private ParameterizedTypeReference<RESPONSE_CLASS> getParameterizedResponseClass() {
		ParameterizedTypeReference<RESPONSE_CLASS> parameterizedType = new ParameterizedTypeReference<RESPONSE_CLASS>() {
		};

		return parameterizedType;
	}

	/**
	 * 
	 * 
	 * @param uri
	 * @param requestEntity
	 * @param httpMethod
	 * @param ignoreUnknowProperties
	 * @param parameterizedResponseType
	 * @return
	 */
	public RESPONSE_CLASS request(URI uri, HttpEntity<REQUEST_CLASS> requestEntity, HttpMethod httpMethod,
			ParameterizedTypeReference parameterizedType, boolean ignoreUnknowProperties) {
		RestTemplate restTemplate = new RestTemplate();

		if (ignoreUnknowProperties) {
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
			messageConverters.add(getMessageConverterIgnoreUnknownProperties());

			restTemplate.setMessageConverters(messageConverters);
		}

		ResponseEntity<RESPONSE_CLASS> response = restTemplate.exchange(uri, httpMethod, requestEntity,
				parameterizedType);

		return response.getBody();
	}

}
