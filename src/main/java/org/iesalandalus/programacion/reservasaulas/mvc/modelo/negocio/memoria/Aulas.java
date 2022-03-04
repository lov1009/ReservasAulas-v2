package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {

	private List<Aula> aulas;

	public Aulas() {

		aulas = new ArrayList<>();

	}

	public Aulas(Aulas aulas) {

		if (aulas == null) {
			throw new NullPointerException("ERROR: No se pueden copiar aulas nulas.");
		}
		setAulas(aulas);

	}

	private void setAulas(Aulas aulas) {

		this.aulas = copiaProfundaAulas(aulas.getAulas());
	}

	public List<Aula> getAulas() {

		// hago uso de la interfaz comparator para comparar la coleccion de aulas por su
		// nombre y las ordeno.

		Comparator<Aula> comparator = Comparator.comparing(Aula::getNombre);
// palabras.sort(Comparator.comparing(String::length).thenComparing(String::compareTo)); pa saber lo del thenComparing TODO
		List<Aula> copiaAulas = copiaProfundaAulas(aulas);

		Collections.sort(copiaAulas, comparator);

		return copiaAulas;
	}

	private List<Aula> copiaProfundaAulas(List<Aula> aulas) {

		List<Aula> copiaAulas = new ArrayList<>();

		Iterator<Aula> it = aulas.iterator();

		while (it.hasNext()) {
			copiaAulas.add(new Aula(it.next()));

		}
		return copiaAulas;
	}

	public int getNumAulas() {

		return aulas.size();
	}

	public void insertar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede insertar un aula nula.");
		}
		if (buscar(aula) != null) {
			throw new OperationNotSupportedException("ERROR: Ya existe un aula con ese nombre.");
		}
		aulas.add(new Aula(aula));
		System.out.println("El aula " + aula.getNombre() + " se ha insertado correctamente.");

	}

	/*
	 * indexOf nos da el indice (como la posicion donde se encuentra) del aula si la
	 * encuentra en la coleccion. Si el indice es >= 0 es que la encontró, y
	 * entonces devolvemos una nueva aula igual a la que había en ese indice.
	 */
	public Aula buscar(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede buscar un aula nula.");
		}

		int indice = aulas.indexOf(aula);
		if (indice >= 0) {
			return new Aula(aulas.get(indice));
		}
		return null;
	}

	public void borrar(Aula aula) throws OperationNotSupportedException {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede borrar un aula nula.");
		}
		if (buscar(aula) == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún aula con ese nombre.");
		}
		aulas.remove(new Aula(aula));
		System.out.println("El aula " + aula.getNombre() + " se ha eliminado con éxito.");
	}

	public List<String> representar() {

		List<String> representacion = new ArrayList<>();

		Iterator<Aula> it = aulas.iterator();

		while (it.hasNext()) {
			representacion.add(it.next().toString());

		}
		return representacion;

	}
}
