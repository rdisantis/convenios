package cl.decalink.tgr.convenios.ejb.entity.persona;

import java.rmi.*;
import javax.ejb.*;
import java.sql.*;
import java.io.Serializable;

/**
*  Mantenimiento de datos de personas (contribuyentes)
*  @author Decalink Ltda.
*  @version 1.0
*/

public class personaBean implements EntityBean, Serializable {
  EntityContext entityContext;
  /** C�digo identificador de la persona */
  public Integer idPersona;
  /** Rut de la persona */
  public Integer rut;
  /** D�gito verificador del Rut */
  public String dv;
  /** Apellido paterno de la Persona*/
  public String apellidoPaterno;
  /** Apellido materno de la Persona*/
  public String apellidoMaterno;
  /** Nombres de la Persona*/
  public String nombres;
  /** Fecha de nacimiento de la Persona*/
  public Timestamp fechaNacimiento;
  /** Fecha de defunci�n de la Persona*/
  public Timestamp fechaDefuncion;
  /** Correo electr�nico de la Persona*/
  public String email;
  /** Campo no utilizado */
  public Integer idEstadoPersona;
  /** Campo no utilizado */
  public Integer idTipoContribuyente;
  /**  Campo no utilizado*/
  public String idTipoPersona;
  /**  Campo no utilizado*/
  public Integer idSexo;
  /** Campo no utilizado*/
  public Integer idEstadoValidado;
  /**  Campo no utilizado*/
  public Integer idFuenteDato;
  /** Fecha de creaci�n de los datos de la persona*/
  public Timestamp fechaCreacion;

  public Integer ejbCreate(Integer idPersona, Integer rut, String dv, String apellidoPaterno, String apellidoMaterno, String nombres, Timestamp fechaNacimiento, Timestamp fechaDefuncion, String email, Integer idEstadoPersona, Integer idTipoContribuyente, String idTipoPersona, Integer idSexo, Integer idEstadoValidado, Integer idFuenteDato, Timestamp fechaCreacion) throws CreateException, RemoteException {
    this.idPersona = idPersona;
    this.rut = rut;
    this.dv = dv;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
    this.nombres = nombres;
    this.fechaNacimiento = fechaNacimiento;
    this.fechaDefuncion = fechaDefuncion;
    this.email = email;
    this.idEstadoPersona = idEstadoPersona;
    this.idTipoContribuyente = idTipoContribuyente;
    this.idTipoPersona = idTipoPersona;
    this.idSexo = idSexo;
    this.idEstadoValidado = idEstadoValidado;
    this.idFuenteDato = idFuenteDato;
    this.fechaCreacion = fechaCreacion;
    return null;
  }
  public Integer ejbCreate(Integer idPersona) throws RemoteException, CreateException, RemoteException {
    return ejbCreate(idPersona, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }
  public void ejbPostCreate(Integer idPersona, Integer rut, String dv, String apellidoPaterno, String apellidoMaterno, String nombres, Timestamp fechaNacimiento, Timestamp fechaDefuncion, String email, Integer idEstadoPersona, Integer idTipoContribuyente, String idTipoPersona, Integer idSexo, Integer idEstadoValidado, Integer idFuenteDato, Timestamp fechaCreacion) throws CreateException, RemoteException {
  }
  public void ejbPostCreate(Integer idPersona) throws CreateException, RemoteException {
    ejbPostCreate(idPersona, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }
  public void ejbLoad() throws RemoteException {
  }
  public void ejbStore() throws RemoteException {
  }
  public void ejbRemove() throws RemoveException, RemoteException {
  }
  public void ejbActivate() throws RemoteException {
  }
  public void ejbPassivate() throws RemoteException {
  }
  public void setEntityContext(EntityContext entityContext) throws RemoteException {
    this.entityContext = entityContext;
  }
  public void unsetEntityContext() throws RemoteException {
    entityContext = null;
  }

  /**
  *  Obtienen el valor del identificador de Persona.
  *  @return el identificador de persona
  */
  public Integer getIdPersona() {
    return idPersona;
  }

  /**
  *  Obtienen el valor del rut de la Persona.
  *  @return el rut de la persona
  */

  public Integer getRut() {
    return rut;
  }

  /**
  *  Asigna un valor al valor del rut de la Persona.
  *  @param rut rut a establecer para la persona actual
  */

  public void setRut(Integer rut) {
    this.rut = rut;
  }

  /**
  *  Obtiene el d�gito verificador para del rut de la Persona.
  *  @return dv D�gito verificador para el rut de la persona actual.
  */


  public String getDv() {
    return dv;
  }

  /**
  *  Modifica el valor del d�gito verificador para del rut de la Persona.
  *  @param dv D�gito verificador a establecer
  */

  public void setDv(String dv) {
    this.dv = dv;
  }

  /**
  *  Obtiene el apellido paterno de la Persona.
  *  @return apellidoPaterno el valor actual del apellido paterno de la persona
  */


  public String getApellidoPaterno() {
    apellidoPaterno = apellidoPaterno.replace('"',' ');
    return apellidoPaterno;
  }

  /**
  *  Cambia el valor del apellido paterno de la Persona.
  *  @param apellidoPaterno Apellido paterno a asignarle a la persona
  */


  public void setApellidoPaterno(String apellidoPaterno) {
    this.apellidoPaterno = apellidoPaterno;
  }


  /**
  *  Obtiene el apellido materno de la Persona.
  *  @return apellidoMaterno el valor actual del apellido materno de la persona
  */

  public String getApellidoMaterno() {
    if (apellidoMaterno == null)
    {
      apellidoMaterno = "";
    }
  apellidoMaterno = apellidoMaterno.replace('"',' ');
    return apellidoMaterno;
  }


  /**
  *  Cambia el valor del apellido materno de la Persona.
  *  @param apellidoMaterno Apellido materno a asignarle a la persona
  */


  public void setApellidoMaterno(String apellidoMaterno) {
    this.apellidoMaterno = apellidoMaterno;
  }

  /**
  *  Obtiene los nombres de la persona .
  *  @return valor de nombres para la persona actual.
  */

  public String getNombres() {
    if (nombres == null)
    {
      nombres = "";
    }
    nombres = nombres.replace('"',' ');
    return nombres;
  }

  /**
  *  Modifica los nombres de la persona .
  *  @param nombres valor de nombres a establecer
  */

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  /**
  *  Obtiene la fecha de nacimiento de la persona
  *  @return la fecha de nacimiento de la persona actual.
  */


  public Timestamp getFechaNacimiento() {
    return fechaNacimiento;
  }

  /**
  *  Establece el valor de la fecha de nacimiento para la persona
  *  @param fechaNacimiento valor de la fecha de nacimiento para persona actual.
  */

  public void setFechaNacimiento(Timestamp fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }


  /**
  *  Obtiene la fecha de defunci�n de la persona
  *  @return la fecha de defunci�n de la persona actual.
  */

  public Timestamp getFechaDefuncion() {
    return fechaDefuncion;
  }

  /**
  *  Establece el valor de la fecha de defunci�n para la persona
  *  @param fechaNacimiento valor de la fecha de defunci�n para persona actual.
  */

  public void setFechaDefuncion(Timestamp fechaDefuncion) {
    this.fechaDefuncion = fechaDefuncion;
  }

  /**
  * Obtiene la direcci�n de correo electr�nico de la persona
  * @return  e-mail de la persona.
  */
  public String getEmail() {
    return email;
  }

  /**
  * Establece la direcci�n de correo electr�nico para la persona actual
  * @param email  e-mail de la persona.
  */

  public void setEmail(String email) {
    this.email = email;
  }

  /**
  * Obtiene valor de campo IdEstadoPersona
  */
  public Integer getIdEstadoPersona() {
  return idEstadoPersona;
  }

  /**
  * Establece valor del campo IdEstadoPersona
  */
  public void setIdEstadoPersona(Integer idEstadoPersona) {
    this.idEstadoPersona = idEstadoPersona;
  }

  /**
  * Obtiene valor del campo IdTipoContribuyente
  */

  public Integer getIdTipoContribuyente() {
    return idTipoContribuyente;
  }

  /**
  * Establece valor del campo IdTipoContribuyente
  */

  public void setIdTipoContribuyente(Integer idTipoContribuyente) {
    this.idTipoContribuyente = idTipoContribuyente;
  }

  /**
  * Obtiene valor del campo IdTipoPersona
  */

  public String getIdTipoPersona() {
    return idTipoPersona;
  }

  /**
  * Establece valor del campo IdTipoPersona
  */
  public void setIdTipoPersona(String idTipoPersona) {
    this.idTipoPersona = idTipoPersona;
  }

  /**
  * Obtiene valor del campo IdSexo
  */

  public Integer getIdSexo() {
    return idSexo;
  }

  /**
  * Establece valor del campo idSexo
  */

  public void setIdSexo(Integer idSexo) {
    this.idSexo = idSexo;
  }

  /**
  * Obtiene el valor del campo IdEstadoValidado
  */

  public Integer getIdEstadoValidado() {
    return idEstadoValidado;
  }

  /**
  * Establece el valor del campo IdEstadoValidado
  */

  public void setIdEstadoValidado(Integer idEstadoValidado) {
    this.idEstadoValidado = idEstadoValidado;
  }

  /**
  * Obtiene el valor del campo IdFuenteDato
  */

  public Integer getIdFuenteDato() {
    return idFuenteDato;
  }

  /**
  * Establece el valor del campo IdFuenteDato
  */

  public void setIdFuenteDato(Integer idFuenteDato) {
    this.idFuenteDato = idFuenteDato;
  }

  /**
  *  Obtiene la fecha de creaci�n de los datos de la persona
  *  @return fecha de creaci�n de los datos
  */
  public Timestamp getFechaCreacion() {
    return fechaCreacion;
  }

  /**
  *  Establece la fecha de creaci�n de los datos de la persona
  *  @param fechaCreacion fecha de creaci�n de los datos a ser asignada
  */

  public void setFechaCreacion(Timestamp fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }
}