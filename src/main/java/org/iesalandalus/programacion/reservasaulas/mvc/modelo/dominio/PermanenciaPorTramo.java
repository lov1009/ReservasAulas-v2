package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.time.LocalDate;
import java.util.Objects;

public class PermanenciaPorTramo extends Permanencia {

	private static final int PUNTOS = 10; //puntos que supone una permanencia por tramo
	private Tramo tramo;

	public PermanenciaPorTramo(LocalDate dia, Tramo tramo) {
		super(dia);
		setTramo(tramo);
		
	}

	public PermanenciaPorTramo(PermanenciaPorTramo permanencia) {
		super(permanencia);
		setTramo(permanencia.getTramo()); 
	}

	public Tramo getTramo() {
		return tramo;
	}

	private void setTramo(Tramo tramo) {
		if (tramo == null) {
			throw new NullPointerException("ERROR: El tramo de una permanencia no puede ser nulo.");
		}

		this.tramo = tramo;
	}

	@Override
	public int getPuntos() {
		
		return PUNTOS;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDia(), tramo);
		//TODO
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermanenciaPorTramo other = (PermanenciaPorTramo) obj;

		return Objects.equals(getDia(), other.getDia()) && Objects.equals(tramo, other.tramo);
	
	}

	// llama a getdia pq no puedo llamar directamente a un atributo privado de la
	// clase abstracta padre 
	@Override
	public String toString() {
		return "día=" + getDia().format(FORMATO_DIA) + ", tramo=" + tramo;
	}
}