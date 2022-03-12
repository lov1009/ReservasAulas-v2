package org.iesalandalus.programacion.reservasaulas;

import org.iesalandalus.programacion.reservasaulas.mvc.controlador.Controlador;
import org.iesalandalus.programacion.reservasaulas.mvc.controlador.IControlador;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.IModelo;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.Modelo;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.IVista;
import org.iesalandalus.programacion.reservasaulas.mvc.vista.Vista;
import org.iesalandalus.programacion.utilidades.Entrada;

public class MainApp {

	public static void main(String[] args) {
		System.out.println("Programa para la gestión de reservas de espacios del IES Al-Ándalus");

		int opcion;
		do {
			System.out.println("Elige el tipo de almacenamiento de datos:\n 1. MEMORIA\n 2. FICHEROS");
			opcion = Entrada.entero();
			if(opcion != 1 || opcion != 2) {
				System.out.println("ERROR: Opción no válida.\n");
			}
			
		} while (opcion != 1 && opcion != 2);

		IModelo modelo;

		if (opcion == 1) {
			modelo = new Modelo(FactoriaFuenteDatos.MEMORIA.crear());
		} else {
			modelo = new Modelo(FactoriaFuenteDatos.FICHEROS.crear());
		}

		// IModelo modelo = new Modelo();
		// IModelo modelo = new Modelo(FactoriaFuenteDatos.MEMORIA.crear());
//		IModelo modelo = new Modelo(FactoriaFuenteDatos.FICHEROS.crear());
		IVista vista = new Vista();
		try {
			IControlador controlador = new Controlador(modelo, vista);
			controlador.comenzar();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	IControlador controlador;

	public MainApp() {

	}

}
