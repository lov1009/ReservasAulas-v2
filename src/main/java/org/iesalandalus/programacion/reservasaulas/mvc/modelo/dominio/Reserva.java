package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.util.Objects;

public class Reserva implements Serializable{

	private Profesor profesor;
	private Aula aula;
	private Permanencia permanencia;

	// Constructor con parámetros
	public Reserva(Profesor profesor, Aula aula, Permanencia permanencia) {

		setProfesor(profesor);
		setAula(aula);
		setPermanencia(permanencia);

	}

	// constructor copia
	public Reserva(Reserva reserva) {

		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede copiar una reserva nula.");
		}

		setProfesor(reserva.profesor);
		setAula(reserva.aula);
		setPermanencia(reserva.permanencia);

	}

	public Profesor getProfesor() {

		return new Profesor(profesor);
	}

	private void setProfesor(Profesor profesor) {
		if (profesor == null) {

			throw new NullPointerException("ERROR: La reserva debe estar a nombre de un profesor.");
		}

		this.profesor = new Profesor(profesor);
	}

	public Aula getAula() {

		return new Aula(aula);
	}

	private void setAula(Aula aula) {

		if (aula == null) {
			throw new NullPointerException("ERROR: La reserva debe ser para un aula concreta.");
		}
		this.aula = new Aula(aula);

	}

	public Permanencia getPermanencia() {

		return permanencia;
	}

	private void setPermanencia(Permanencia permanencia) {

		if (permanencia == null) {
			throw new NullPointerException("ERROR: La reserva se debe hacer para una permanencia concreta.");
		}

		// casteo para el tipo de permanencia, para que la reserva pueda ser para una
		// permanencia de tramo o de hora
		if (permanencia.getPuntos() == 10) {
			this.permanencia = new PermanenciaPorTramo(permanencia.getDia(),
					((PermanenciaPorTramo) permanencia).getTramo());
		} else {
			this.permanencia = new PermanenciaPorHora(permanencia.getDia(),
					((PermanenciaPorHora) permanencia).getHora());
		}

	}

	/*
	 * método getReservaFicticia que a través de un aula y de una permanencia
	 * recibidas como parámetros, obtenga un profesor ficticio y devuelve una
	 * reserva.
	 */

	public static Reserva getReservaFicticia(Aula aula, Permanencia permanencia) {
		if (aula == null) {
			throw new NullPointerException("ERROR: La reserva debe ser para un aula concreta.");
		}
		if (permanencia == null) {
			throw new NullPointerException("ERROR: La reserva se debe hacer para una permanencia concreta.");
		}
		String nombre = "nombreFicticio";
		String correo = "correo@ficticio.es";
		Profesor profesorFicticio = new Profesor(nombre, correo);

		return new Reserva(profesorFicticio, aula, permanencia);

	}

	// Una reserva restará la suma del número de puntos de la permanencia más el
	// número de puntos del aula.

	public float getPuntos() {
		return getAula().getPuntos() + getPermanencia().getPuntos();
	}

	@Override
	public int hashCode() {

		return Objects.hash(aula, permanencia);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;

		return Objects.equals(aula, other.aula) && Objects.equals(permanencia, other.permanencia);
	}


	@Override
	public String toString() {
		return profesor + ", " + aula + ", " + permanencia + ", puntos="
				+ Float.toString(getPuntos()).replace('.', ',');
	}

}
