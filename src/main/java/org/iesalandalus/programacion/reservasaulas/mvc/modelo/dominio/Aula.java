package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.util.Objects;

public class Aula implements Serializable{

	// atributos
	private static final float PUNTOS_POR_PUESTO = 0.5f; // (0,5 puntos por el número de puestos del aula)
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
		setPuestos(aula.puestos);
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

	private void setPuestos(int puestos) {
		if (puestos < MIN_PUESTOS || puestos > MAX_PUESTOS) {
			throw new IllegalArgumentException(
					"ERROR: El número de puestos no es correcto. \nDebe elegir un valor comprendido entre " + MIN_PUESTOS + " y " + MAX_PUESTOS + ".");
		}
		this.puestos = puestos;
	}

	public int getPuestos() {
		return puestos;
	}

	// calcula el numero de puntos según los puestos del aula
	public float getPuntos() {
		float resultado = puestos * PUNTOS_POR_PUESTO;
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
