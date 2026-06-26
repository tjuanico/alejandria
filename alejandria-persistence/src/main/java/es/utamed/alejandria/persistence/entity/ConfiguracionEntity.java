package es.utamed.alejandria.persistence.entity;


/*
   TFM, UTAMED
   Propósito: Mapear la tabla RAG_CONFIGURACION dónde almacenamos parámetros de la aplicación
   @since: 19/04/2026
   @author: Antoni Juanico
 */

import jakarta.persistence.*;

@Entity
@Table(name="RAG_CONFIGURACION")
@NamedQuery(
        name = "ConfiguracionEntity.obtenerParClaveValor",
        query = "SELECT c.valor FROM ConfiguracionEntity c WHERE c.clave = :clave AND c.entorno = :entorno"
)
public class ConfiguracionEntity {
    @Id
    @Column(name="id_config")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idConfig;

    @Column(name="clave", nullable = false)
    private String clave;

    @Column(name="valor", nullable = false)
    private String valor;

    @Column(name="entorno", nullable = false)
    private String entorno;

    // Getters & Setters

    public Integer getIdConfig() { return idConfig; }
    public void setIdConfig(Integer idConfig) { this.idConfig = idConfig;}

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getEntorno() { return entorno; }
    public void setEntorno(String entorno) { this.entorno = entorno; }
}
