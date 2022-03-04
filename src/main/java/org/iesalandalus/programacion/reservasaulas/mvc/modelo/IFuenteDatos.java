/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IAulas;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IProfesores;
import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.IReservas;

/**
 * @author laura
 *
 */
public interface IFuenteDatos {//extends IAulas, IProfesores, IReservas{

	public IAulas crearAulas();
	public IProfesores crearProfesores();
	public IReservas crearReservas();
}
//en las interfaces no hace falta poner public??