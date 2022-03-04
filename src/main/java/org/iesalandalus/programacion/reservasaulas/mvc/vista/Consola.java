package org.iesalandalus.programacion.reservasaulas.mvc.vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Tramo;
import org.iesalandalus.programacion.utilidades.Entrada;

public class Consola {

	private static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private Consola() {

	}

	public static void mostrarMenu() {

		mostrarCabecera("MENÚ");
		for (Opcion opcion : Opcion.values()) {
			System.out.println(opcion);
		}
	}

	public static void mostrarCabecera(String mensaje) {
		System.out.printf("%n%s%n", mensaje);

	}

	public static int elegirOpcion() {
		int opcionOrdinal;
		do {
			System.out.println("Elige una opción: ");
			opcionOrdinal = Entrada.entero();

		} while (!Opcion.esOrdinalValido(opcionOrdinal));

		return opcionOrdinal;
	}

	// lee los datos del aula y trata las excepciones
	public static Aula leerAula() {
		Aula aula = null;

		do {

			try {
				aula = new Aula(leerNombreAula(), leerNumeroPuestos());

			} catch (NullPointerException e) {
				System.out.println(e.getMessage());

			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}

		} while (aula == null);

		return aula;

	}

	// Lee el número de puestos que va a tener un aula.
	public static int leerNumeroPuestos() {
		System.out.println("Introduzca el número de puestos:");
		return Entrada.entero();
	}

	// A partir de un nombre obtiene un objeto de tipo Aula.
	public static Aula leerAulaFicticia() {
		String nombreAula = leerNombreAula();

		return Aula.getAulaFicticia(nombreAula);
	}

	// Lee el nombre de un aula.
	public static String leerNombreAula() {
		System.out.println("Introduzca el nombre del aula:");

		return Entrada.cadena();
	}

	// Lee todos los datos de un profesor y trata las excepciones
	public static Profesor leerProfesor() {
		Profesor profesor = null;

		do {
			String nombreProfesor = leerNombreProfesor();

			System.out.println("Introduzca el correo del profesor");
			String correoProfesor = Entrada.cadena();

			System.out.println("Introduzca el telefono profesor");
			String telefonoProfesor = Entrada.cadena();

			try {
				if (telefonoProfesor.isBlank()) {
					profesor = new Profesor(nombreProfesor, correoProfesor);
				} else
					profesor = new Profesor(nombreProfesor, correoProfesor, telefonoProfesor);

			} catch (NullPointerException e) {
				System.out.println(e.getMessage());

			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}

		} while (profesor == null);

		return profesor;

	}

	public static String leerNombreProfesor() {

		System.out.println("Introduzca el nombre del profesor");
		return Entrada.cadena();

	}

	// A partir de un correo obtiene un profesor.
	public static Profesor leerProfesorFicticio() {
		System.out.println("Introduzca el correo del profesor");
		String correoProfesor = Entrada.cadena();
		return new Profesor("nombreFicticio", correoProfesor);

	}

	// Lee los datos de un tramo y trata las excepciones
	public static Tramo leerTramo() {
		Tramo tramoEnum = null;

		do {
			System.out.println("Introduzca el tramo para la reserva");
			for (Tramo tramo : Tramo.values()) {
				System.out.println(String.format("%d.- %s", tramo.ordinal(), tramo));
			}
//TODO ARRIBA
			int tramo = Entrada.entero();

			try {
				tramoEnum = Tramo.values()[tramo];

			} catch (NullPointerException e) {
				System.out.println(e.getMessage());

			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());

			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("ERROR: " + tramo + " no es una opción válida.");
			}

		} while (tramoEnum == null);

		return tramoEnum;

	}

	// Lee los datos de un dia y trata las excepciones
	public static LocalDate leerDia() {
		LocalDate diaLocalDate = null;

		while (diaLocalDate == null) {
			System.out.println("Introduzca la fecha, con el formato: dd/MM/yyyy");
			String diaIntroducido = Entrada.cadena();

			try {
				diaLocalDate = LocalDate.parse(diaIntroducido, (FORMATO_DIA));

				if (diaLocalDate.isBefore(LocalDate.now())) {
					System.out.println("ERROR: El día no puede ser anterior al día de hoy.");

					diaLocalDate = null;
				}

			} catch (DateTimeParseException e) {
				System.out.println("ERROR: El formato de la fecha no es correcto.");
			}
		}
		return diaLocalDate;
	}

	// Para elegir entre una permanencia por tramo o por hora.
	public static int elegirPermanencia() throws OperationNotSupportedException {
		System.out.println("Introduzca la permanencia deseada, por tramo(0) o por hora(1)");
		int numIntroducido = Entrada.entero();

		if (numIntroducido != 0 && numIntroducido != 1) {
			throw new OperationNotSupportedException(
					"ERROR: Esta no es una opción válida. \nIntroduzca 0 para elegir una permanencia por tramo o 1 para elegir una permanencia por hora.");
		}
		return numIntroducido;

	}

	// Según la permanencia elegida por el usuario, devuelve una new permanencia de
	// tipo tramo o de tipo hora. El do while(true) se repite hasta que la hora que
	// lee en leerHora es válida (cumple el horario marcado y el patrón).
	// Se tratan excepciones
	public static Permanencia leerPermanencia() {

		try {

			LocalDate dia = leerDia();
			if (elegirPermanencia() == 0) {

				return new PermanenciaPorTramo(dia, leerTramo());
			}

			do {
				try {
					return new PermanenciaPorHora(dia, leerHora());

				} catch (IllegalArgumentException e) {

					System.out.println(e.getMessage());

				} catch (NullPointerException e) {

					System.out.println(e.getMessage());
				}
			} while (true);

		} catch (OperationNotSupportedException e) {

			System.out.println(e.getMessage());

		} catch (IllegalArgumentException e) {

			System.out.println(e.getMessage());

		} catch (NullPointerException e) {

			System.out.println(e.getMessage());
		}

		return null;

	}

	// Pide al usuario una hora en formato HH:mm de la que se hará
	// uso en el caso de leer una permanencia por hora.
	private static LocalTime leerHora() {

		LocalTime horaLocalTime = null;
		while (horaLocalTime == null) {

			System.out.println("Introduzca una hora con el formato: HH:mm");
			String hora = Entrada.cadena();

			try {
				horaLocalTime = LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));

			} catch (DateTimeParseException e) {
				System.out.println("ERROR: El formato de la hora no es correcto.");
			}
		}
		return horaLocalTime;
	}

	// A partir de un aula ficticia, de un profesor ficticio y de una permanencia,
	// devuelve una reserva.

	public static Reserva leerReserva() {
		Aula aulaFicticia = leerAulaFicticia();
		Profesor profesorFicticio = leerProfesorFicticio();
		Permanencia p = leerPermanencia();
		return new Reserva(profesorFicticio, aulaFicticia, p);
	}

//TODO
	// A partir de un aula ficticia y de una permanencia, devuelve reserva ficticia.
	public static Reserva leerReservaFicticia() {

		Aula aulaFicticia = leerAulaFicticia();
		Reserva reser = leerReserva();
		Permanencia p = leerPermanencia();
		return new Reserva(reser);
	}

}
