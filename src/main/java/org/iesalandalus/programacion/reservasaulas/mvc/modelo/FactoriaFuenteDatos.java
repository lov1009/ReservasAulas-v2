/**
 * 
 */
package org.iesalandalus.programacion.reservasaulas.mvc.modelo;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio.memoria.FactoriaFuenteDatosMemoria;

/**
 * @author laura
 *
 */
public enum FactoriaFuenteDatos {

	MEMORIA(){
		public IFuenteDatos crear() {
			return new FactoriaFuenteDatosMemoria();
		}
	};
	
	FactoriaFuenteDatos() {
	} //esto creo q no es necesario TODO
		
	public abstract IFuenteDatos crear();
	
}
