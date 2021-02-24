package cl.decalink.tgr.convenios.cuotas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Title:        NumeroCuotas.java
 * Description:  Calcula el Numero m�ximo de cuotas que se pueden otorgar
 * Copyright:    Copyright (c) 2003
 * Company:      Decalink
 * @author       Luis Vald�s
 * @version 1.0
 */



public class NumeroCuotas {
private int MaximoCuotas=12;
private int numeroCuotas;

private static final int ART192=1;
private static final int RMH=3;
private static final int CONVSINCOND=2;
private static final int CONVCONCOND=1;
private static final int CONVENIOMATIC=0;
private static final int INTERNET=0;

  public NumeroCuotas(int perfil,int tipoConvenio, int tipoPago,long idPersona, long monto,String tipoImpuesto,Date fechaEmision,Connection connection)throws SQLException
  {
  int cuotas=this.MaximoCuotas;

  if (
       (perfil==CONVENIOMATIC || perfil==INTERNET) //Si el Usuario es Internet o Conveniomatico

     )
  {
        //Calculo segun tabla

        try{
            int impuesto=2;
            if (tipoImpuesto.compareTo("F")==0){
              impuesto=1;
            }

            CallableStatement  call = connection.prepareCall("{? = call CalculoNumeroCuotas.Nro_Cuotas192_RMH(?,?,?)}");
            call.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);

            call.setLong(2, idPersona);
            call.setLong(3,monto);
            call.setInt(4, impuesto);
            call.execute();
            cuotas= call.getInt(1);
            call.close();
        }
        catch(SQLException e) {
            throw new SQLException("Error ejecutando el SQL de recuperacion numero Cuotas :" + e.toString() );
        }
        finally{
          try {
            connection.close();
          }
          catch (SQLException ex) {
          }
        }

  }//Fin If Si es Convenio Art 192 con o sin condonacion  o RMH Sin Condonacion
  else // Cualquier Otro Perfil RMH Fiscal con Condonacion
  {
          try{
              int impuesto=2;
              if (tipoImpuesto.compareTo("F")==0){
                impuesto=1;
              }
              CallableStatement  call = connection.prepareCall("{? = call CalculoNumeroCuotas.Num_cuotas_Perfil(?)}");
              call.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
              call.setLong(2, perfil);
              call.execute();
              cuotas= call.getInt(1);
              call.close();
          }
          catch(SQLException e) {
              throw new SQLException("Error ejecutando el SQL de recuperacion numero de cuotas perfil:" + e.toString() );
          }

  }


  this.numeroCuotas=cuotas;
  }//Fin Constructor

  public int getNumeroCuotas(){
    return this.numeroCuotas;
  }


}//Fin Clase