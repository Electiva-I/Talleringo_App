
package com.islam.talleringo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserEntity {

    @SerializedName("id_usuario")
    @Expose
    private int idUsuario;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("estado")
    @Expose
    private int estado;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
