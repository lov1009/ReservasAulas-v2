package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;

public class Aulas implements IAulas {

	private static final String NOMBRE_FICHERO_AULAS = "datos/aulas.dat";

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

	@Override
	public void comenzar() {
		leer();

	}

	private void leer() {
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS); // crear File que accede al fichero (constante creada antes)
		// crear flujo de entrada y el ObjectInputStream (para leer objetos), voy
		// leyendo los objetos, casteándolos a Aula y voy insertando cada aula que lea.
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroAulas))) {
			Aula aula = null;
			do {
				aula = (Aula) entrada.readObject();
				insertar(aula);

			} while (aula != null);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de aulas.");
		} catch (EOFException e) {
			System.out.println("Fichero aulas leído correctamente.");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida.");
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void terminar() {
		escribir();

	}

	private void escribir() {
		File ficheroAulas = new File(NOMBRE_FICHERO_AULAS); // crear fichero
		// crear flujo de salida y el ObjectOutputStream (para escribir objetos) y
		// escribir cada aula que existe
		// en aulas
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroAulas))) {
			for (Aula aula : aulas)
				salida.writeObject(aula);
			System.out.println("Fichero aulas escrito correctamente.");

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de aulas.");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida.");
		}

	}

	private void setAulas(Aulas aulas) {

		this.aulas = copiaProfundaAulas(aulas.getAulas());
	}

	public List<Aula> getAulas() {

		// hago uso de la interfaz comparator para comparar la coleccion de aulas por su
		// nombre y las ordeno.

		Comparator<Aula> comparator = Comparator.comparing(Aula::getNombre);
		// palabras.sort(Comparator.comparing(String::length).thenComparing(String::compareTo));
		// para saber lo del thenComparing TODO
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
