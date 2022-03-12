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

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;

public class Profesores implements IProfesores {

	private static final String NOMBRE_FICHERO_PROFESORES = "datos/profesores.dat";

	/*
	 * Modifica la clase Profesores para que utilice un ArrayList en vez de un
	 * Array. Modifica también los métodos, eliminando los necesarios, para que
	 * sigan haciendo lo mismo pero utilizando esta estructura. Ten en cuenta que el
	 * método representar ahora también devuelve una lista. Haz un commit.
	 */

	private List<Profesor> profesores;

	public Profesores() {

		profesores = new ArrayList<>();
	}

	public Profesores(Profesores profesores) {

		if (profesores == null) {
			throw new NullPointerException("ERROR: No se pueden copiar profesores nulos.");
		}
		setProfesores(profesores);

	}

	@Override
	public void comenzar() {
		leer();

	}

	private void leer() {

		File ficheroProfesores = new File(NOMBRE_FICHERO_PROFESORES); // crear File que accede al fichero (constante
																		// creada antes)
		// crear flujo de entrada y el ObjectInputStream (para leer objetos), voy
		// leyendo los objetos, casteándolos a Profesor y voy insertando cada profesor
		// que lea.
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroProfesores))) {
			Profesor profesor = null;
			do {
				profesor = (Profesor) entrada.readObject();
				insertar(profesor);

			} while (profesor != null);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de profesores.");
		} catch (EOFException e) {
			System.out.println("Fichero profesores leído correctamente.");
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
		File ficheroProfesores = new File(NOMBRE_FICHERO_PROFESORES); // crear fichero
		// crear flujo de salida y el ObjectOutputStream (para escribir objetos) y
		// escribir cada profesor que existe en profesores
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroProfesores))) {
			for (Profesor profesor : profesores)
				salida.writeObject(profesor);
			System.out.println("Fichero profesores escrito correctamente.");

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de profesores.");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida.");
		}
	}

	private void setProfesores(Profesores profesores) {

		this.profesores = copiaProfundaProfesores(profesores.getProfesores());
	}

	public List<Profesor> getProfesores() {
		// hago uso de la interfaz comparator para comparar la coleccion de profesores
		// por su correo y los ordeno.

		Comparator<Profesor> comparator = Comparator.comparing(Profesor::getCorreo);

		List<Profesor> copiaProfesores = copiaProfundaProfesores(profesores);

		Collections.sort(copiaProfesores, comparator);

		return copiaProfesores;
	}

	private List<Profesor> copiaProfundaProfesores(List<Profesor> profesores) {

		List<Profesor> copiaProfesores = new ArrayList<>();

		Iterator<Profesor> it = profesores.iterator();

		while (it.hasNext()) {
			copiaProfesores.add(new Profesor(it.next()));

		}
		return copiaProfesores;
	}

	public int getNumProfesores() {

		return profesores.size();
	}

	public void insertar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede insertar un profesor nulo.");
		}

		if (buscar(profesor) != null) {
			throw new OperationNotSupportedException("ERROR: Ya existe un profesor con ese correo.");
		}
		profesores.add(new Profesor(profesor));
		System.out.println("El profesor " + profesor.getNombre() + " se ha insertado correctamente.");

	}

	/*
	 * indexOf nos da el indice del profesor si lo encuentra en la coleccion
	 * profesores. Si el indice es >= 0 es que lo encontró, y entonces devolvemos un
	 * nuevo profesor igual al que había en ese indice.
	 */
	public Profesor buscar(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede buscar un profesor nulo.");
		}
		int indice = profesores.indexOf(profesor);
		if (indice >= 0) {
			return new Profesor(profesores.get(indice));
		}
		return null;
	}

	public void borrar(Profesor profesor) throws OperationNotSupportedException {
		if (profesor == null) {
			throw new NullPointerException("ERROR: No se puede borrar un profesor nulo.");
		}
		Profesor profesorEncontrado = buscar(profesor);
		if (profesorEncontrado == null) {
			throw new OperationNotSupportedException("ERROR: No existe ningún profesor con ese correo.");
		}
		
		profesores.remove(profesor);
		System.out.println("El profesor " + profesorEncontrado.getNombre() + " se ha eliminado con éxito.");
	}

	public List<String> representar() {

		List<String> representacion = new ArrayList<>();

		Iterator<Profesor> it = profesores.iterator();

		while (it.hasNext()) {
			representacion.add(it.next().toString());

		}

		return representacion;
	}

}
