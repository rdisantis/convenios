package cl.decalink.tgr.convenios.ejb.entity.persona;

import java.rmi.*;
import javax.ejb.*;
import java.util.Collection;
import java.sql.*;

public interface personaHome extends EJBHome {
  public persona create(Integer idPersona, Integer rut, String dv, String apellidoPaterno, String apellidoMaterno, String nombres, Timestamp fechaNacimiento, Timestamp fechaDefuncion, String email, Integer idEstadoPersona, Integer idTipoContribuyente, String idTipoPersona, Integer idSexo, Integer idEstadoValidado, Integer idFuenteDato, Timestamp fechaCreacion) throws CreateException, RemoteException;
  public persona create(Integer idPersona) throws RemoteException, CreateException;
  public persona findByPrimaryKey(Integer primaryKey) throws RemoteException, FinderException;
  public Collection findByRut(Integer rut) throws RemoteException, FinderException;
  /*
  public Collection findAll() throws RemoteException, FinderException;
  */
}