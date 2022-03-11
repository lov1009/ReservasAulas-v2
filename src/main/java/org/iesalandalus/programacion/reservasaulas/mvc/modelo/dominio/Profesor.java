package org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profesor implements Serializable{

	

	private static final String ER_TELEFONO = "^[69][0-9]{8}$";
	private static final String ER_CORREO = "^[a-z]+([a-z0-9\\-\\_\\.]*[a-z0-9])*+@([a-z]*\\.[a-z]{2,})+$";
	private String nombre;
	private String correo;
	private String telefono;

	public Profesor(String nombre, String correo) {
		setNombre(nombre);
		setCorreo(correo);
	}

	public Profesor(String nombre, String correo, String telefono) {

		setNombre(nombre);
		setCorreo(correo);
		setTelefono(telefono);
	}

	public Profesor(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede copiar un profesor nulo.");
		}

		if (profesor.telefono != null) {
			setTelefono(profesor.telefono);

		}
		setNombre(profesor.nombre);
		setCorreo(profesor.correo);

	}
	/*
	 * Este método debe normalizar un nombre eliminando caracteres en blanco de
	 * sobra y poniendo en mayúsculas la primera letra de cada palabra y en
	 * minúsculas las demás.
	 */

	private String formateaNombre(String nombre) {

		String[] nombreDividido = nombre.split(" ");

		StringBuilder nombreFormateado = new StringBuilder();
		


		for (int i = 0; i < nombreDividido.length; i++) {
			if (nombreDividido[i].isEmpty()) {
				continue;
			}

			nombreFormateado.append(nombreDividido[i].substring(0, 1).toUpperCase());
			nombreFormateado.append(nombreDividido[i].substring(1, nombreDividido[i].length()).toLowerCase());

			if (i + 1 < nombreDividido.length) {
				nombreFormateado.append(" ");
			}
		}
		return nombreFormateado.toString();

	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		if (nombre == null) {
			throw new NullPointerException("ERROR: El nombre del profesor no puede ser nulo.");
		}
		if (nombre.isEmpty() || nombre.isBlank()) {
			throw new IllegalArgumentException("ERROR: El nombre del profesor no puede estar vacío.");
		}

		this.nombre = formateaNombre(nombre);
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		if (correo == null) {
			throw new NullPointerException("ERROR: El correo del profesor no puede ser nulo.");

		}

		// No compruebo que sea vacío o esté en blanco, porque eso ocurriría si no
		// cumple el patrón que es lo que compruebo abajo.

		Pattern p = Pattern.compile(ER_CORREO);
		Matcher m = p.matcher(correo);

		if (m.find()) {
			this.correo = correo;

		} else {
			throw new IllegalArgumentException("ERROR: El correo del profesor no es válido.");
		}

	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {

		if (telefono == null) {
			return;
		}

		Pattern p = Pattern.compile(ER_TELEFONO);
		Matcher m = p.matcher(telefono);

		if (m.find()) {
			this.telefono = telefono;

		} else {
			throw new IllegalArgumentException("ERROR: El teléfono del profesor no es válido.");
		}

	}
	
	//devuelve un profesor a partir de un correo del mismo.
	public static Profesor getProfesorFicticio(String correo) {
		if (correo == null) {
			throw new NullPointerException("ERROR: El correo del profesor no puede ser nulo.");
		}

		String nombre = "nombreFicticio";
		return new Profesor(nombre, correo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(correo);
	}

	@Override // profesores son iguales si tienen el mismo correo
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profesor other = (Profesor) obj;
		return Objects.equals(correo, other.correo);
	}

	@Override
	public String toString() {
		StringBuilder profesor = new StringBuilder("nombre=" + nombre + ", correo=" + correo);
		
		if (telefono != null) {
			profesor.append(", teléfono=" + telefono);
		}
		return profesor.toString();

	}

}
