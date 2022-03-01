package org.iesalandalus.programacion.reservasaulas.mvc.modelo.negocio;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.mvc.modelo.dominio.Profesor;

public interface IProfesores {
	//Declaramos los métodos que forman parte de la interfaz (El comportamiento)
	public List<Profesor> getProfesores();
	public int getNumProfesores();
	public void insertar(Profesor profesor) throws OperationNotSupportedException;
	public Profesor buscar(Profesor profesor);
	public void borrar(Profesor profesor) throws OperationNotSupportedException;
	public List<String> representar();
}
