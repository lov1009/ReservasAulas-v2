package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Permanencia implements Serializable{

	/*
	 * Convierte la clase Permanencia en una clase abstracta y haz que el método
	 * getPuntos sea abstracto. Esta clase sólo tendrá como atributo el día de la
	 * permanencia. Haz un commit.
	 */

	// atributos
	private LocalDate dia;
	protected static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	

	// constructor con parámetros
	public Permanencia(LocalDate dia) {
		setDia(dia);

	}

	// constructor copia
	public Permanencia(Permanencia permanencia) {
		if (permanencia == null) {
			throw new NullPointerException("ERROR: No se puede copiar una permanencia nula.");
		}
		setDia(permanencia.dia);
		
	}

	// getter y setter
	public LocalDate getDia() {

		return dia;
	}

	private void setDia(LocalDate dia) {
		if (dia == null) {
			throw new NullPointerException("ERROR: El día de una permanencia no puede ser nulo.");
		}

		this.dia = dia;

	}
	

	public abstract int getPuntos();
	
	@Override
	public abstract int hashCode(); 
	

	@Override
	public abstract boolean equals(Object obj); 
	
	

	@Override
	public String toString() {
		return "día=" + dia.format(FORMATO_DIA) + ", puntos=" + getPuntos();

	}

}
