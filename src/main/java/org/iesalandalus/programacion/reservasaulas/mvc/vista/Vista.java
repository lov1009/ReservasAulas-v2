package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;

public class Vista implements IVista {

	private IControlador controlador;

	private static final String ERROR = "";
	private static final String NOMBRE_VALIDO = "MariPepi";
	private static final String CORREO_VALIDO = "pepi@gmail.com";

	public Vista() {

		Opcion.setVista(this);
	}

	@Override
	public void setControlador(IControlador controlador) {

		if (controlador == null) {
			throw new NullPointerException("ERROR: El controlador no puede ser nulo.");

		}
		this.controlador = controlador;
	}

	/*
	 * Bucle que muestra el menú, pide la opción deseada y la ejecuta,hasta que la
	 * opción elegida sea SALIR.
	 */

	@Override
	public void comenzar() {

		int ordinalOpcion;
		do {
			Consola.mostrarMenu();
			ordinalOpcion = Consola.elegirOpcion();
			Opcion opcion = Opcion.getOpcionSegunOrdinal(ordinalOpcion);
			opcion.ejecutar();
		} while (ordinalOpcion != Opcion.SALIR.ordinal());

	}

	@Override
	public void salir() {

		controlador.terminar();
		System.out.println("ADIÓS");

	}

	public void insertarAula() {

		Aula aula = Consola.leerAula();

		try {
			controlador.insertarAula(aula);

		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	public void borrarAula() {

		Aula aula = Consola.leerAula();

		try {
			controlador.borrarAula(aula);
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		}

	}

	public void buscarAula() {
		Aula aula = Aula.getAulaFicticia(Consola.leerNombreAula());

		Aula aulaEncontrada = controlador.buscarAula(aula);
		if (aulaEncontrada != null) {
			System.out.println(aulaEncontrada);
		} else
			System.out.println("ERROR: No existe el aula " + aula.getNombre());

	}

	public void listarAulas() {
		List<String> aulas = controlador.representarAulas();

		if (aulas.size() <= 0) {
			System.out.println("ERROR: No hay aulas registradas.");
		}
		for (String aula : aulas) {
			System.out.println(aula);
		}

	}

	public void insertarProfesor() {
		Profesor profesor = Consola.leerProfesor();

		try {
			controlador.insertarProfesor(profesor);

		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void borrarProfesor() {

		try {
			Profesor profesor = Consola.leerProfesorFicticio();

			controlador.borrarProfesor(profesor);

		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	public void buscarProfesor() {
		try {

			Profesor profesor = Consola.leerProfesorFicticio();
			Profesor profesorEncontrado = controlador.buscarProfesor(profesor);
			if (profesorEncontrado != null) {
				System.out.println(profesorEncontrado.toString());
			} else
				System.out.println("ERROR: No existe ningún profesor con el correo: " + profesor.getCorreo());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarProfesores() {
		List<String> profesores = controlador.representarProfesores();

		if (profesores.size() <= 0) {
			System.out.println("ERROR: No hay profesores dados de alta.");
		}
		for (String profesor : profesores) {
			System.out.println(profesor);
		}

	}

	public void realizarReserva() {
		try {
			Profesor profesor = Consola.leerProfesorFicticio();
			Profesor profesorEncontrado = controlador.buscarProfesor(profesor);

			if (profesorEncontrado == null) {
				System.out.println(
						"ERROR: No existe ningún profesor registrado con el correo " + profesor.getCorreo() + ".");
				return;
			}

			Aula aula = Consola.leerAulaFicticia();
			Aula aulaEncontrada = controlador.buscarAula(aula);

			if (aulaEncontrada == null) {
				System.out.println("ERROR: No existe el aula " + aula.getNombre());
				return;
			}

			Permanencia permanencia = Consola.leerPermanencia();
			Reserva reserva = new Reserva(profesorEncontrado, aulaEncontrada, permanencia);

			controlador.realizarReserva(reserva);

		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void anularReserva() {

		Profesor profesor = Consola.leerProfesorFicticio();
		Profesor profesorEncontrado = controlador.buscarProfesor(profesor);

		if (profesorEncontrado == null) {
			System.out.println("ERROR: No existe el profesor: " + profesor);
			return;
		}
		Reserva reserva = Consola.leerReservaFicticia();

		try {
			controlador.anularReserva(reserva);
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public void listarReservas() {

		List<String> reservas = controlador.representarReservas();

		if (reservas.size() <= 0) {
			System.out.println("ERROR: No hay reservas.");
		}
		for (String reserva : reservas) {
			System.out.println(reserva);
		}

	}

	public void listarReservasAula() {
		try {
			Aula aula = Consola.leerAulaFicticia();

			Aula aulaEncontrada = controlador.buscarAula(aula);

			if (aulaEncontrada == null) {
				System.out.println("ERROR: No existe el aula " + aula.getNombre());
				return;
			}
			List<Reserva> reservasAula = controlador.getReservasAula(aulaEncontrada);

			if (reservasAula.size() <= 0) {
				System.out.println("No hay reservas para esta aula.");
				return;
			}
			for (Reserva reserva : reservasAula) {
				System.out.println("Reservas por aula: " + reserva);
			}

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}

	}

	public void listarReservasProfesor() {
		try {
			Profesor profesor = Consola.leerProfesorFicticio();
			Profesor profesorEncontrado = controlador.buscarProfesor(profesor);
			if (profesorEncontrado == null) {
				System.out.println("ERROR: No existe el profesor: " + profesor);
				return;
			}
			List<Reserva> reservasProfesor = controlador.getReservasProfesor(profesorEncontrado);

			if (reservasProfesor.size() <= 0) {
				System.out.println("No hay reservas para este profesor.");
				return;
			}
			for (Reserva reserva : reservasProfesor) {
				System.out.println("Reservas por profesor: " + reserva);
			}

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());

		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}

	}

	//
	public void consultarDisponibilidad() {

		Aula aula = Consola.leerAulaFicticia();
		Aula aulaEncontrada = controlador.buscarAula(aula);

		if (aulaEncontrada == null) {
			System.out.println("ERROR: El aula " + aula.getNombre()
					+ " no existe, por lo que no se puede comprobar su disponibilidad.");
			return;
		}

		Permanencia permanencia = Consola.leerPermanencia();

		if (controlador.consultarDisponibilidad(aula, permanencia)) {
			System.out.println("El aula " + aula.getNombre() + ", para la permanencia " + permanencia + " está: DISPONIBLE.");

		} else {
			System.out.println("El aula " + aula.getNombre() + ", para la permanencia " + permanencia + " está: NO DISPONIBLE.");
		}

	}

}
