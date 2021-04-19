
package com.islam.talleringo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehicleEntity {

    @SerializedName("id_vehiculo_usuario")
    @Expose
    private int idVehiculoUsuario;
    @SerializedName("id_modelo")
    @Expose
    private int idModelo;
    @SerializedName("id_usuario")
    @Expose
    private int idUsuario;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("AddMaintenanceDialog\u00f1o")
    @Expose
    private String aO;
    @SerializedName("estado")
    @Expose
    private int estado;

    public int getIdVehiculoUsuario() {
        return idVehiculoUsuario;
    }

    public void setIdVehiculoUsuario(int idVehiculoUsuario) {
        this.idVehiculoUsuario = idVehiculoUsuario;
    }

    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getaO() {
        return aO;
    }

    public void setaO(String aO) {
        this.aO = aO;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
