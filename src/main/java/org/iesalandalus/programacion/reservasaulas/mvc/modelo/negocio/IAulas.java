package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Aula;


public interface IAulas {
	//Declaramos los métodos que forman parte de la interfaz (El comportamiento)
	public List<Aula> getAulas();
	public int getNumAulas();
	public void insertar(Aula aula) throws OperationNotSupportedException;
	public Aula buscar(Aula aula);
	public void borrar(Aula aula) throws OperationNotSupportedException;
	public List<String> representar();
}
