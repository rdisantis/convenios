package cl.decalink.tgr.convenios.propuestas;

/*
 * Title: ConvenioInternet.java
 * Description: Representa las prouestas de convenio usados en internet
 * Copyright:    Copyright (c) 2003
 * Company: Decalink Ltda.
 * @author M�nica Quezada
 * @version 1.0
 */
import java.util.Vector;
import java.util.Collection;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Iterator;
import cl.decalink.tgr.convenios.deudas.DeudaWeb;
import cl.decalink.tgr.convenios.cuotas.Cuotas;
import cl.decalink.tgr.convenios.cuotas.Cuota;
import cl.decalink.tgr.convenios.propuestas.ConvenioMasivo;


public class ConvenioInternet extends Convenio implements Serializable{
  private int codTipoPago;
  private int codTipoConvenio;
  private int tipoImpuesto;

  private Long porcentajeCuotaContado;
  private Long numeroCuotas;
  private Long numeroMaximoCuotas;
  private int numeroDeudas;
  private long saldoNetoPropuesta;

  private Cuotas cuotas;
  private Vector deudas;
  private ArrayList deudasPropuesta;
  private String nombrePropuesta;
  private ConvenioMasivo convenioMasivo;

  public ConvenioInternet(String nombrePropuesta) {
    super();
    this.deudas = new Vector();
    this.nombrePropuesta = nombrePropuesta;
    this.convenioMasivo = null;
    }

   public String getNombrePropuesta(){
    return this.nombrePropuesta;
   }

   public void setCodTipoPago(int codTipoPago){
    this.codTipoPago = codTipoPago;
   }

   public int getCodTipoPago(){
    return this.codTipoPago;
   }

   public void setCodTipoConvenio(int codTipoConvenio){
    this.codTipoConvenio=codTipoConvenio;
   }

   public int getCodTipoConvenio(){
    return this.codTipoConvenio;
   }

   public void setTipoImpuesto(int tipoImpuesto){
    this.tipoImpuesto=tipoImpuesto;
   }

   public int getTipoImpuesto(){
    return this.tipoImpuesto;
   }


   public void setPorcentajeCuotaContado(Long porcentajeCuotaContado){
    this.porcentajeCuotaContado = porcentajeCuotaContado;
   }

   public Long getPorcentajeCuotaContado(){
    return this.porcentajeCuotaContado;
   }

   public void setNumeroMaximoCuotas(Long numeroMaximoCuotas){
       this.numeroMaximoCuotas = numeroMaximoCuotas;
   }

   public Long getNumeroMaximoCuotas(){
       return  this.numeroMaximoCuotas;
   }

   public void setNumeroCuotas(Long numeroCuotas){
    this.numeroCuotas = numeroCuotas;
   }

   public Long getNumeroCuotas(){
    return this.numeroCuotas;
   }

   public void setCuotas(Cuotas cuotas){
   this.cuotas = cuotas;
   }

   public Cuotas getCuotas(){
   return this.cuotas;
   }

   public Collection getColeccionCuotas(){
   return this.cuotas.getCuotas();
   }

   public void setDeudas(Vector deudas){
   this.deudas = deudas;
   }

   public Collection getDeudas(){
   return this.deudas;
   }

   public void setDeudasPropuesta(ArrayList deudasProp){
   this.deudasPropuesta = deudasProp;
   }

   public ArrayList getDeudasPropuesta(){
   return this.deudasPropuesta;
   }



   public ConvenioMasivo getConvenioMasivo(){
     return  this.convenioMasivo;
   }
   public void setConvenioMasivo(ConvenioMasivo convenioMasivo){
   this.convenioMasivo = convenioMasivo;
   }

   public int getNumeroDeudas(){
   //this.numeroDeudas = this.getDeudas().size();
   return this.numeroDeudas;

   }

   public void setNumeroDeudas(int numeroDeudas){

     this.numeroDeudas = numeroDeudas;


   }


   public long getMontoCuotaContado(){
     long monto = this.cuotas.getMontoCuotaContado();
     return monto;
   }

   public long getMontoCuotaFija(){
     long monto = this.cuotas.getMontoCuotaFija();
     return monto;
   }



   public long getSaldoNetoPropuesta(){
     return this.saldoNetoPropuesta;
   /*long suma=0;
   if (!this.deudas.isEmpty()){
       Iterator it = this.deudas.iterator();
       while (it.hasNext()){
           DeudaWeb deuda = (DeudaWeb) it.next();
           Long Saldo = (Long) new Long (deuda.getMonto());
           suma = suma + Saldo.longValue();
       }
     }
   return suma;*/
   }

   public void setSaldoNetoPropuesta(long saldoNetoPropuesta)
   {
     this.saldoNetoPropuesta=saldoNetoPropuesta;
   }

}