package com.msbookspayments.microservice.facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {
	private Long id;
	private String titulo;
	private String autor;
	private String fecha_de_publicacion;
	private String editorial;
	private String categoria;
	private String ISBN;
	private String portada;
	private String sinopsis;
	private String valoracion;
	private Boolean visible;
	private Boolean stock;
	private Double precio;
}