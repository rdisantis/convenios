package cl.decalink.tgr.convenios.deudas;

/*
 * Title:        DeudasWeb.java
 * Description:  Caracteriza las deudas que se presentan en las interfaces JSP
 * Copyright:    Copyright (c) 2003
 * Company:      Decalink Ltda.
 * @author Gonzalo Ariel Aro D�az
 * @version 1.0
 */
import java.io.Serializable;
import java.sql.Date;

public class DeudaWeb extends DeudaConvenio implements Serializable{
  private String grupo;
  private boolean seleccionado;
  private boolean habilitado;
  private String rutRolFormateado;
  private String descripcionTipoDeuda;
  private boolean exclusionPorRol;/*06-06-2003*/
  private Long cod92Liq;/*21-03-2012  ccc*/
  private DeudaExclusion deudaExclusion;

  public DeudaWeb(int tipoContribuyente, int tipoFormulario, long rutRol, long folio, Date fechaVencimiento) {
    super(tipoContribuyente,tipoFormulario,rutRol, folio, fechaVencimiento);
    this.seleccionado = false;
    this.habilitado = false;
    this.exclusionPorRol = false;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public String getGrupo() {
    return this.grupo;
  }

  public void seleccionar() {
    this.seleccionado = true;
  }

  public void desseleccionar(){
    this.seleccionado = false;
  }

  public boolean estaSeleccionado() {
    return this.seleccionado;
  }

  public void habilitar() {
    this.habilitado = true;
  }

  public void deshabilitar() {
    this.habilitado = false;
  }

  public boolean estaHabilitado() {
    return this.habilitado;
  }

  public String getRutRolFormateado(){
    return this.rutRolFormateado;
  }

  public void setRutRolFormateado(String rutRolFormateado) {
    this.rutRolFormateado = rutRolFormateado;
  }

  public String getDescripcionTipoDeuda(){
    return this.descripcionTipoDeuda;
  }

  public void setDescripcionTipoDeuda(String descripcionTipoDeuda){
    this.descripcionTipoDeuda = descripcionTipoDeuda;
  }

  public void setPosibilidadConvenio(String posibilidadConvenio){
    super.setPosibilidadConvenio(posibilidadConvenio);

    if (("N".equals(this.getPosibilidadCondonacion())) && ("N".equals(this.getPosibilidadConvenio()))){
      this.deshabilitar();
    }
  }

  public void setPosibilidadCondonacion(String posibilidadCondonacion){
    super.setPosibilidadCondonacion(posibilidadCondonacion);
    if (("N".equals(this.getPosibilidadCondonacion())) && ("N".equals(this.getPosibilidadConvenio()))){
      this.deshabilitar();
    }
  }

  public void setDerechoConvenio(String posibilidadConvenio){
	    super.setDerechoConvenio(posibilidadConvenio);

	    if (("N".equals(this.getDerechoCondonacion())) && ("N".equals(this.getDerechoConvenio()))){
	      this.deshabilitar();
	    }
	  }

	  public void setDerechoCondonacion(String posibilidadCondonacion){
	    super.setDerechoCondonacion(posibilidadCondonacion);
	    if (("N".equals(this.getDerechoCondonacion())) && ("N".equals(this.getDerechoConvenio()))){
	      this.deshabilitar();
	    }
	  }  
  
  
  /*06-06-2003*/
  public void verificarExclusionPorRol(){
     this.exclusionPorRol = true;
  }
  /*06-06-2003*/
  public boolean exclusionPorRol(){
     return this.exclusionPorRol;
  }

  public void setCod92Liq(Long cod92Liq) {
  this.cod92Liq = cod92Liq;
}

 public Long getCod92Liq() {
  return this.cod92Liq;
 }

 public String getIsliquidable(){
   String liquidable=null;
   if (this.getCod92Liq().longValue() >= 0){
      liquidable="SI";
   }else{
      liquidable="NO";
   }
    return liquidable;
 }

public void setDeudaExclusion(DeudaExclusion deudaExclusion) {
	this.deudaExclusion = deudaExclusion;
}

public DeudaExclusion getDeudaExclusion() {
	return deudaExclusion;
}

}