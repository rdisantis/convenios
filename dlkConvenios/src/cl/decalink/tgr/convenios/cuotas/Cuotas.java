package cl.decalink.tgr.convenios.cuotas;

/*
 * Title: Cuotas.java
 * Description:  Controla el conjunto de cuotas generado y/o a generar
 * Copyright:    Copyright (c) 2003
 * Company:      Decalink Ltda.
 * @author Gonzalo Ariel Aro D�az
 * @version 1.0
 */

import java.io.Serializable;

import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;

import java.sql.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;

import cl.decalink.tgr.convenios.BooleanString;
import cl.decalink.tgr.convenios.DateExt;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Cuotas implements Serializable {
  /* Representa la cantidad de meses a adicionar al calculo de la primera cuota
   *  de un convenio
   */
  public static final int PRIMERA_CUOTA_CONVENIO = 0;

  /* Representa la cantidad de meses a adicionar al calculo del primer pago
   *  de una condonaci�n
   */
  public static final int PRIMERA_CUOTA_CONDONACION = 0;

  private long codigoPropuesta;
  private int tiposPagoCuotas;
  private int cantidadCuotas;
  private long porcentajeCuotaContado;
  private long montoCuotaContado;
  private long montoCuotaFija;
  private long montoCuotaAjuste;
  private long total;
  private long totalProyectado;
  private Date fechaPrimeraCuota;
  private Date fechaUltimaCuota;
  private Vector cuotas;

  private long rut;

  public Cuotas(int cantidadCuotas, long porcentajeCuotaContado, long total, long totalProyectado, Date fechaInicio) {
   /* System.out.println("--- Constructor Cuotas");
    System.out.println("Cantidad Cuotas " + cantidadCuotas);
    System.out.println("Porcentaje Cuota Contado " + porcentajeCuotaContado);
    System.out.println("Total " + total);
    System.out.println("Total Proyectado " + totalProyectado);
    System.out.println("Fecha Inicio " + fechaInicio);*/

    Cuota cuota = null;

    this.cuotas = new Vector();
    this.cantidadCuotas = cantidadCuotas;
    this.porcentajeCuotaContado = porcentajeCuotaContado;
    this.montoCuotaAjuste = 0;
    this.total = total;
    this.totalProyectado = totalProyectado;
    this.fechaPrimeraCuota = fechaInicio;

    double fMontoCuotaContado = 0;
    double fSaldo = 0;

    double fTotal = (double) this.total;
    double fPorcentajeCuotaContado = (double) this.porcentajeCuotaContado;

    if (this.cantidadCuotas == 0){
      this.porcentajeCuotaContado = 100;
      this.montoCuotaContado = this.total;
      this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONDONACION)).getTime());
      this.fechaUltimaCuota = this.fechaPrimeraCuota;
    } else if (this.cantidadCuotas == 1) {
      this.porcentajeCuotaContado = 100;
      this.montoCuotaContado = this.total;
      this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONVENIO)).getTime());
      cuota = this.addCuota(1, fechaPrimeraCuota, (long) this.montoCuotaContado, 0);
    } else {

      this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONVENIO)).getTime());

      fMontoCuotaContado = porcentajeCuotaContado;
      this.porcentajeCuotaContado = Math.round(fMontoCuotaContado/fTotal);
      fSaldo = (double) this.totalProyectado - fMontoCuotaContado;
      this.montoCuotaContado = (long) Math.round (fMontoCuotaContado);
      this.montoCuotaFija = (long) Math.round (fSaldo / (this.cantidadCuotas - 1));

      this.addCuota(1, fechaPrimeraCuota, this.montoCuotaContado, 0);
      if (cantidadCuotas >= 2){
        int i = 1;

        while(this.cantidadCuotas > ++i){

          this.addCuota(i, fechaPrimeraCuota, this.montoCuotaFija, i - 1);
        }
        cuota = this.addCuota(i, fechaPrimeraCuota, 0, i);
      }
    }

    this.fechaUltimaCuota = cuota.getFechaVencimiento();
  }

//metodo para nuevo convenio ley 20027

  public Cuotas(int cantidadCuotas, long porcentajeCuotaContado, long total, long totalProyectado, Date fechaInicio, boolean sinAjuste) {
 /* System.out.println("--- Constructor Cuotas");
  System.out.println("Cantidad Cuotas " + cantidadCuotas);
  System.out.println("Porcentaje Cuota Contado " + porcentajeCuotaContado);
  System.out.println("Total " + total);
  System.out.println("Total Proyectado " + totalProyectado);
  System.out.println("Fecha Inicio " + fechaInicio);*/

  Cuota cuota = null;

  this.cuotas = new Vector();
  this.cantidadCuotas = cantidadCuotas;
  this.porcentajeCuotaContado = porcentajeCuotaContado;
  this.montoCuotaAjuste = 0;
  this.total = total;
  this.totalProyectado = totalProyectado;
  this.fechaPrimeraCuota = fechaInicio;

  double fMontoCuotaContado = 0;
  double fSaldo = 0;

  double fTotal = (double) this.total;
  double fPorcentajeCuotaContado = (double) this.porcentajeCuotaContado;

  if (this.cantidadCuotas == 0){
    this.porcentajeCuotaContado = 100;
    this.montoCuotaContado = this.total;
    this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONDONACION)).getTime());
    this.fechaUltimaCuota = this.fechaPrimeraCuota;
  } else if (this.cantidadCuotas == 1) {
    this.porcentajeCuotaContado = 100;
    this.montoCuotaContado = this.total;
    this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONVENIO)).getTime());
    cuota = this.addCuota(1, fechaPrimeraCuota, (long) this.montoCuotaContado, 0);
  } else {

    this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONVENIO)).getTime());

    fMontoCuotaContado = porcentajeCuotaContado;
    this.porcentajeCuotaContado = Math.round(fMontoCuotaContado/fTotal);
    fSaldo = (double) this.totalProyectado - fMontoCuotaContado;
    this.montoCuotaContado = (long) Math.round (fMontoCuotaContado);
    this.montoCuotaFija = (long) Math.round (fSaldo / (this.cantidadCuotas - 1));

    this.addCuota(1, fechaPrimeraCuota, this.montoCuotaContado, 0);
    if (cantidadCuotas >= 2){
      int i = 1;

      while(this.cantidadCuotas > ++i){

        this.addCuota(i, fechaPrimeraCuota, this.montoCuotaFija, i - 1);
      }
        if (sinAjuste){
         cuota = this.addCuota(i, fechaPrimeraCuota, this.montoCuotaFija, i - 1);
        }else{
          cuota = this.addCuota(i, fechaPrimeraCuota, 0, i - 1);
        }
     }

  }

  this.fechaUltimaCuota = cuota.getFechaVencimiento();
}
////fin ley


  public Cuotas(int cantidadCuotas, long total, Date fechaInicio) {
    this.cuotas = new Vector();
    this.cantidadCuotas = cantidadCuotas;
    this.porcentajeCuotaContado = 0;
    this.montoCuotaAjuste = 0;
    this.total = total;
    Cuota cuota = null;
    this.montoCuotaContado = 0;
    this.montoCuotaAjuste = 0;

    fechaPrimeraCuota = fechaInicio;

    double fSaldo = 0;

    if (this.cantidadCuotas == 0){
      this.porcentajeCuotaContado = 100;
      this.montoCuotaContado = this.total;
      this.fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONDONACION)).getTime());
      this.fechaUltimaCuota = this.fechaPrimeraCuota;

    }
    //System.out.println("CUOTAS " + this.cantidadCuotas);
    //System.out.println("CUOTA FECHA ULTIMA CUOTA " + this.fechaUltimaCuota);
    if (cantidadCuotas >= 1){
      this.montoCuotaFija = (long) Math.round(this.total / (this.cantidadCuotas));
      fechaPrimeraCuota = (java.sql.Date) new java.sql.Date(((java.util.Date) DateExt.addMes(fechaPrimeraCuota, this.PRIMERA_CUOTA_CONVENIO)).getTime());
      int i = 0;
      if (this.cantidadCuotas > 1){
        while(this.cantidadCuotas > i + 1){
         // System.out.println(i);
          cuota = this.addCuota(++i, fechaPrimeraCuota, this.montoCuotaFija, i - 1);
        }
      }
      /* Inserta cuota de ajuste */
      cuota = this.addCuota(++i, fechaPrimeraCuota, montoCuotaAjuste, i - 1);
      this.fechaUltimaCuota = cuota.getFechaVencimiento();
    }

  }

  private Cuota addCuota(long numeroCuota, Date fechaCuota, long monto, int meses){
    Date fechaVencimiento = fechaCuota;

    //Verifica si es la primera cuota y si el a�o es bisiesto
    GregorianCalendar nuevaFecha = new GregorianCalendar();

     nuevaFecha.setTime(fechaCuota);

     int iAno = nuevaFecha.get(nuevaFecha.YEAR);
     int iMes = nuevaFecha.get(nuevaFecha.MONTH);
     int iDia = nuevaFecha.get(nuevaFecha.DATE);

    if (meses > 0 )
    {
      //Verifica si es febrero
     if (iMes == 1)
     {
         iDia=28;

     }
     fechaVencimiento.setDate(iDia);
    }
  //  nuevaFecha.set(nuevaFecha.DATE,iDia);

//    fechaVencimiento=(java.sql.Date)nuevaFecha.getTime();

    fechaVencimiento = new java.sql.Date(((java.util.Date) DateExt.getUltimoDiaHabilMes(DateExt.addMes((java.util.Date) fechaVencimiento, meses))).getTime());

    Cuota cuota = new Cuota(numeroCuota, fechaVencimiento, monto);
    this.cuotas.addElement(cuota);

    return cuota;
  }

  public long getCodigoPropuesta(){
    return this.codigoPropuesta;
  }

  public int getTiposPagoCuotas(){
    return this.tiposPagoCuotas;
  }

  public int getCantidadCuotas (){
    return this.cantidadCuotas;
  }

  public long getPorcentajeCuotaContado(){
    return this.porcentajeCuotaContado;
  }

  public long getMontoCuotaContado(){
    return this.montoCuotaContado;
  }

  public long getMontoCuotaFija(){
    return this.montoCuotaFija;
  }

  public long getMontoCuotaAjuste(){
    return this.montoCuotaAjuste;
  }

  public long getTotal(){
    return this.total;
  }

  public Date getFechaPrimeraCuota(){
    return this.fechaPrimeraCuota;
  }

  public Vector getCuotas(){
    return this.cuotas;
  }

public void setCuotas(Vector cuotas)
  {
    this.cuotas=cuotas;
  }

  public void setRut(long rut){
    this.rut = rut;

  }

  public void setInserta(Connection conn, long codigoPropuesta, int tiposPagoCuotas) throws SQLException{
    this.codigoPropuesta = codigoPropuesta;
    this.tiposPagoCuotas = tiposPagoCuotas;

    CallableStatement  callBorra = conn.prepareCall("{? = call InsertaPropuesta.BorraCuotas(?)}");
    callBorra.registerOutParameter(1, java.sql.Types.VARCHAR);
    callBorra.setLong(2, codigoPropuesta);/* codigo propuesta */
    callBorra.execute();

    Iterator itx = this.cuotas.iterator();
    while (itx.hasNext()) {
      Cuota cuota = (Cuota) itx.next();
      CallableStatement  call = conn.prepareCall("{? = call InsertaPropuesta.InsertarCuotasConvenio(?,?,?,?,?,?,?,?,?)}");

      call.registerOutParameter(1, java.sql.Types.VARCHAR);

      call.setLong(2, tiposPagoCuotas);/* codigo tipo pago cuota */
      call.setLong(3, codigoPropuesta);/* codigo propuesta */
      call.setLong(4, cuota.getNumeroCuota());/* numero de cuota */
      call.setLong(5, cuota.getMonto()); /* monto*/
      call.setDate(6, (java.sql.Date) cuota.getFechaVencimiento()); /* fecha vencimiento*/
      call.setString(7, cuota.getPagada()); /* pagada */
      //call.setString(7, BooleanString.NO); /* pagada */
      call.setNull(8, java.sql.Types.DATE); /* fecha pago */
      call.setString(9, BooleanString.NO); /* anulado cheque */
      call.setLong(10, this.rut); /* rutRol*/
      /*call.setLong(11, 0);  dv */

      call.execute();

      String strCodigoCuota = call.getString(1);
      try {
        Long codigoCuota = new Long(strCodigoCuota);
      } catch(NumberFormatException e) {
        throw new SQLException ("Inserta cuotas " + strCodigoCuota + e.toString());
      }
      strCodigoCuota = null;
      call.close();
      call = null;
    }
  }

  public Date getFechaUltimaCuota(){
    return this.fechaUltimaCuota;
  }
}