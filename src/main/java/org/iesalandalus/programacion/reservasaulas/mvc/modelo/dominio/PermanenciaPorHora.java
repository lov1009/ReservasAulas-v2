package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PermanenciaPorHora extends Permanencia {

	private static final int PUNTOS = 3;
	private static final LocalTime HORA_INICIO = LocalTime.of(8, 0); // TODO
	private static final LocalTime HORA_FIN = LocalTime.of(22, 0); // TODO
	protected static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("00:00");
	private LocalTime hora;

	public PermanenciaPorHora(LocalDate dia, LocalTime hora) {
		super(dia);
		setHora(hora); 
	}

	public PermanenciaPorHora(PermanenciaPorHora permanencia) {
		super(permanencia);
		setHora(LocalTime.of(12, 0));

	
	}

	public LocalTime getHora() {

		return hora;
	}

	private void setHora(LocalTime hora) {
		if (hora == null) {
			throw new NullPointerException("ERROR: La hora de una permanencia no puede ser nula.");
		}

		if (hora.isBefore(HORA_INICIO) || hora.isAfter(HORA_FIN)) {
			throw new IllegalArgumentException("ERROR: La hora de una permanencia no es válida.");
		}
		
		if (hora.getMinute() != 0) {
			throw new IllegalArgumentException("ERROR: La hora de una permanencia debe ser una hora en punto.");
		}
		this.hora = hora;
	}

	@Override
	public int getPuntos() {
		return PUNTOS;
	}

	@Override
	public int hashCode() {

		return Objects.hash(getDia(), hora);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermanenciaPorHora other = (PermanenciaPorHora) obj;

		return Objects.equals(getDia(), other.getDia()) && Objects.equals(hora, other.hora);
	}

	public String toString() {
		return "día=" + getDia().format(FORMATO_DIA) + ", hora=" + hora.format(FORMATO_HORA); 
	}

}
