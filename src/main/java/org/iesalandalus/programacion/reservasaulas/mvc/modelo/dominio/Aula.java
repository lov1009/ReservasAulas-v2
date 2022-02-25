package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.util.Objects;


public class Aula {

	/*
	 * Haz las modificaciones necesarias en la clase Aula para incluir el atributo
	 * puestos e implementar el método getPuntos. Haz un commit.
	 * 
	 * Las aulas deben tener información sobre el número de puestos de cada una.
	 * 
	 * Una reserva restará la suma del número de puntos de la permanencia 
	 * (3 puntos por hora o 10 puntos por tramo) 
	 * más el número de puntos del aula 
	 * (0,5 puntos por el número de puestos del aula). 
	 * 
	 * Un profesor tiene disponibles cada mes 200 puntos 
	 * por lo que si cuando va a realizar una reserva el 
	 * número de puntos gastados ese mes más 
	 * el número de puntos de la reserva que quiere realizar 
	 * supera 200 no dejará realizar la reserva.
	 */

	// atributos
	private static final float PUNTOS_POR_PUESTO = 0.5f;
	private static final int MIN_PUESTOS = 10;
	private static final int MAX_PUESTOS = 100;
	private String nombre;
	private int puestos;

	// constructor con parámetros
	public Aula(String nombre, int puestos) {
		setNombre(nombre);
		setPuestos(puestos);

	}

	// constructor copia
	public Aula(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede copiar un aula nula.");
		}
		setNombre(aula.nombre);
		setPuestos(MIN_PUESTOS);
	}

	private void setNombre(String nombre) {
		if (nombre == null) {
			throw new NullPointerException("ERROR: El nombre del aula no puede ser nulo.");

		}
		if (nombre.isEmpty() || nombre.isBlank()) {
			throw new IllegalArgumentException("ERROR: El nombre del aula no puede estar vacío.");
		}
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	//TODO 
	private void setPuestos(int puestos) {
		if (puestos < MIN_PUESTOS || puestos > MAX_PUESTOS) {
			throw new IllegalArgumentException("ERROR: El número de puestos no es correcto.");
		}
		this.puestos = puestos;
	}

	public int getPuestos() {
		return puestos;
	}

	
	public float getPuntos() {
		float resultado = puestos*PUNTOS_POR_PUESTO;
		return resultado;
	}

	public static Aula getAulaFicticia(String nombreAula) {
		
		return new Aula(nombreAula, MIN_PUESTOS);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aula other = (Aula) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "nombre=" + nombre + ", puestos=" + puestos;
	}

}
