package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.ficheros;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Permanencia;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorHora;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.PermanenciaPorTramo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;

public class Reservas implements IReservas {

	private static final String NOMBRE_FICHERO_RESERVAS = "datos/reservas.dat";

	/*
	 * Haz las modificaciones necesarias en la clase Reserva (creo que será
	 * Reservas?) para que Un aula se pueda reservar por un profesor para una
	 * permanencia por tramo o por horas y que implemente el método getPuntos. Haz
	 * un commit. Se tengan en cuenta las restricciones comentadas en el enunciado
	 * sobre no poder reservar aulas para el mes en curso y que no se sobrepase el
	 * límite de puntos de un profesor para el mes en el que quiere realizar la
	 * reserva. Haz un commit.
	 */

	private static final float MAX_PUNTOS_PROFESOR_MES = 200;
	private List<Reserva> reservas;

	public Reservas() {

		reservas = new ArrayList<>();
	}

	public Reservas(Reservas reservas) {

		if (reservas == null) {
			throw new NullPointerException("ERROR: No se pueden copiar reservas nulas.");
		}

		setReservas(reservas);
	}

	@Override
	public void comenzar() {
		leer();

	}

	private void leer() {

		File ficheroReservas = new File(NOMBRE_FICHERO_RESERVAS); // crear File que accede al fichero (constante creada
																	// antes)
		// crear flujo de entrada y el ObjectInputStream (para leer objetos), voy
		// leyendo los objetos, casteándolos a Reserva y voy insertando cada reserva que
		// lea.
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroReservas))) {
			Reserva reserva = null;
			do {
				reserva = (Reserva) entrada.readObject();
				insertar(reserva);

			} while (reserva != null);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No puedo encontrar la clase que tengo que leer.");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de reservas.");
		} catch (EOFException e) {
			System.out.println("Fichero reservas leído correctamente.");
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

		File ficheroReservas = new File(NOMBRE_FICHERO_RESERVAS); // crear fichero
		// crear flujo de salida y el ObjectOutputStream (para escribir objetos) y
		// escribir cada reserva que existe en reservas
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroReservas))) {
			for (Reserva reserva : reservas)
				salida.writeObject(reserva);
			System.out.println("Fichero reservas escrito correctamente.");

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No puedo abrir el fichero de reservas.");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida.");
		}

	}

	private void setReservas(Reservas reservas) {

		this.reservas = copiaProfundaReservas(reservas.getReservas());
	}

	public List<Reserva> getReservas() {
		/*
		 * Utilizo la interfaz Comparator con las funciones lambda para comparar las
		 * reservas. En el comparator le indico como quiero que se haga la comparacion:
		 * primero comparo el nombre del aula, y si su nombre es igual (==0), comparo su
		 * dia, y nuevamente si su dia es igual, comparo su permanencia usando los
		 * puntos gastados por permanencia, que si son 10, son de una reserva de tramo y
		 * si no serían de una reserva de hora. Finalmente abajo en collections.sort se
		 * va comparando la lista de reservas (copiaReservas) con el comparator y se
		 * ordena.
		 */
		List<Reserva> copiaReservas = copiaProfundaReservas(reservas);

		Comparator<Reserva> comparator = (Reserva r1, Reserva r2) -> {
			int resultadoPorNombreAula = r1.getAula().getNombre().compareTo(r2.getAula().getNombre());
			if (resultadoPorNombreAula == 0) {
				int resultadoPorDia = r1.getPermanencia().getDia().compareTo(r2.getPermanencia().getDia());
				if (resultadoPorDia == 0) {
					if (r1.getPermanencia().getPuntos() == 10) {
						return ((PermanenciaPorTramo) r1.getPermanencia()).getTramo()
								.compareTo(((PermanenciaPorTramo) r2.getPermanencia()).getTramo());
					}
					return ((PermanenciaPorHora) r1.getPermanencia()).getHora()
							.compareTo(((PermanenciaPorHora) r2.getPermanencia()).getHora());
				}
				return resultadoPorDia;
			}
			return resultadoPorNombreAula;
		};

		Collections.sort(copiaReservas, comparator);

		return copiaReservas;
	}

	private List<Reserva> copiaProfundaReservas(List<Reserva> reservas) {

		List<Reserva> copiaReservas = new ArrayList<>();

		Iterator<Reserva> it = reservas.iterator();

		while (it.hasNext()) {
			copiaReservas.add(new Reserva(it.next()));

		}
		return copiaReservas;
	}

	public int getNumReservas() {

		return reservas.size();
	}

	/*
	 * Inserta una reserva, comprueba que no sea nula, que no exista justo la misma
	 * reserva, que si existe ya una reserva para ese día, su permanencia sea del
	 * mismo tipo (o salta excepcion), que sea para el mes siguiente, y se
	 * comprueban las reservas del profesor y si sus puntos gastados, mas los que
	 * supondría una nueva reserva superan los puntos max que tiene por mes pues no
	 * se puede insertar su reserva.
	 */
	public void insertar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede insertar una reserva nula.");
		}

		if (buscar(reserva) != null) {
			throw new OperationNotSupportedException(
					"ERROR: Ya existe una reserva para ese aula con la misma permanencia.");
		}

		Reserva aulaDia = getReservaAulaDia(reserva.getAula(), reserva.getPermanencia().getDia());
		if (aulaDia != null) {
			if (aulaDia.getPermanencia().getPuntos() != reserva.getPermanencia().getPuntos()) {
				throw new OperationNotSupportedException(
						"ERROR: Ya se ha realizado una reserva de otro tipo de permanencia para este día.");
			}
		}

		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException(
					"ERROR: Sólo se pueden hacer reservas para el mes que viene o posteriores.");
		}

		List<Reserva> reservasProfesor = getReservasProfesor(reserva.getProfesor());
		float puntosGastados = 0;
		for (Reserva reservaActual : reservasProfesor) {
			puntosGastados += getPuntosGastadosReserva(reservaActual);
		}

		if ((puntosGastados + getPuntosGastadosReserva(reserva)) > MAX_PUNTOS_PROFESOR_MES) {
			throw new OperationNotSupportedException(
					"ERROR: Esta reserva excede los puntos máximos por mes para dicho profesor.");
		}

		reservas.add(new Reserva(reserva));
		System.out.println("La reserva " + reserva + " se ha realizado correctamente.");
	}

	private boolean esMesSiguienteOPosterior(Reserva reserva) {

		if (reserva.getPermanencia().getDia().getYear() > LocalDate.now().getYear()) {
			return true;

		} else if (reserva.getPermanencia().getDia().getYear() < LocalDate.now().getYear()) {
			return false;

		} else {

			if (reserva.getPermanencia().getDia().getMonthValue() > LocalDate.now().getMonthValue()) {
				return true;
			}
			return false;
		}
	}

	private float getPuntosGastadosReserva(Reserva reserva) {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No puede ser una reserva nula."); // Creo que no salta esta excepción
		}
		return reserva.getPuntos();

	}

	// TODO tengo que pensar donde usarlo, supongo que tiene algo que ver con
	// getReservasProfesor
	private List<Reserva> getReservasProfesorMes(Profesor profesor, LocalDate mes) {

		if (profesor == null) {
			throw new NullPointerException("ERROR: el profesor no puede ser nulo.");
		}

		List<Reserva> reservasProfeMes = new ArrayList<>();

		Iterator<Reserva> it = reservas.iterator();

		while (it.hasNext()) {
			Reserva reservaActual = it.next();
			if (reservaActual.getProfesor().equals(profesor)
					&& reservaActual.getPermanencia().getDia().getMonth().equals(mes.getMonth())) {
				reservasProfeMes.add(new Reserva(reservaActual));
			}
		}
		return reservasProfeMes;

	}

	// Devuelve una nueva reserva con el aula y dia recibidos si la encuentra en las
	// reservas.
	private Reserva getReservaAulaDia(Aula aula, LocalDate dia) {

		Iterator<Reserva> it = reservas.iterator();

		while (it.hasNext()) {
			Reserva reservaActual = it.next();
			if (reservaActual.getAula().equals(aula) && reservaActual.getPermanencia().getDia().equals(dia)) {
				return new Reserva(reservaActual);
			}
		}
		return null;
	}

	/*
	 * indexOf nos da el indice (como la posicion donde se encuentra) de la reserva
	 * si la encuentra en la coleccion de reservas. Si el indice es >= 0 es que la
	 * encontró, y entonces devolvemos una nueva reserva igual a la reserva que
	 * había en ese indice: reservas.get(indice).
	 */
	public Reserva buscar(Reserva reserva) {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede buscar una reserva nula.");
		}

		int indice = reservas.indexOf(reserva);
		if (indice >= 0) {
			return new Reserva(reservas.get(indice));
		}
		return null;

		/*
		 * quizas se puede hacer asi tb int i;
		 * 
		 * if (reservas.contains(reserva)) { i = reservas.indexOf(reserva); return new
		 * Reserva(reservas.get(i)); }
		 * 
		 * return null;
		 */

	}

	// Antes de borrar comprueba que la reserva no sea nula, que sea para el mes
	// siguiente y que exista justamente la misma reserva que se quiere borrar.
	public void borrar(Reserva reserva) throws OperationNotSupportedException {
		if (reserva == null) {
			throw new NullPointerException("ERROR: No se puede borrar una reserva nula.");
		}

		if (!esMesSiguienteOPosterior(reserva)) {
			throw new OperationNotSupportedException(
					"ERROR: Sólo se pueden anular reservas para el mes que viene o posteriores.");
		}

		Reserva reservaEncontrada = buscar(reserva);
		if (reservaEncontrada == null) {
			throw new OperationNotSupportedException("ERROR: No existe ninguna reserva igual.");
		}

		reservas.remove(reservaEncontrada);
		System.out.println("La reserva " + reservaEncontrada + " se ha borrado con éxito.");
	}

	public List<String> representar() {

		List<String> representacion = new ArrayList<>();

		Iterator<Reserva> it = reservas.iterator();

		while (it.hasNext()) {
			representacion.add(it.next().toString());
		}

		return representacion;
	}

	public List<Reserva> getReservasProfesor(Profesor profesor) {
		if (profesor == null) {
			throw new NullPointerException("ERROR: El profesor no puede ser nulo.");
		}

		Iterator<Reserva> it = getReservas().iterator();

		List<Reserva> reservasProfesor = new ArrayList<>();

		while (it.hasNext()) {
			Reserva reservaActual = it.next();

			if (reservaActual.getProfesor().equals(profesor)) {
				reservasProfesor.add(new Reserva(reservaActual));
			}
		}
		return reservasProfesor;

	}

	public List<Reserva> getReservasAula(Aula aula) {
		if (aula == null) {
			throw new NullPointerException("ERROR: El aula no puede ser nula.");
		}

		Iterator<Reserva> it = getReservas().iterator();

		List<Reserva> reservasAula = new ArrayList<>();

		while (it.hasNext()) {
			Reserva reservaActual = it.next();

			if (reservaActual.getAula().equals(aula)) {
				reservasAula.add(new Reserva(reservaActual));
			}
		}
		return reservasAula;
	}

	public List<Reserva> getReservasPermanencia(Permanencia permanencia) {
		if (permanencia == null) {
			throw new NullPointerException("ERROR: La permanencia no puede ser nula.");
		}

		Iterator<Reserva> it = reservas.iterator();

		List<Reserva> reservasPermanencia = new ArrayList<>();

		while (it.hasNext()) {
			Reserva reservaActual = it.next();

			if (reservaActual.getPermanencia().equals(permanencia)) {
				reservasPermanencia.add(new Reserva(reservaActual));
			}
		}
		return reservasPermanencia;

	}

	public boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) {
		if (aula == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de un aula nula.");
		}

		if (permanencia == null) {
			throw new NullPointerException("ERROR: No se puede consultar la disponibilidad de una permanencia nula.");
		}

		List<Reserva> reservasAula = getReservasAula(aula);
		for (Reserva reserva : reservasAula) {
			if (permanencia.equals(reserva.getPermanencia())) {
				return false;
			}
		}

		Reserva aulaDia = getReservaAulaDia(aula, permanencia.getDia());
		if (aulaDia != null) {
			if (aulaDia.getPermanencia().getPuntos() != permanencia.getPuntos()) {
				return false;

			}

		}
		return true;
	}
}
