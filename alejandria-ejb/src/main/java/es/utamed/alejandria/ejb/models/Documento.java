package es.utamed.alejandria.ejb.models;

import es.utamed.alejandria.persistence.entity.DocumentoEntity;

import java.time.LocalDate;

/*
   TFM, UTAMED
   Clase Documento para representar los metadatos de un documento
   @since: 15/03/2026
   @author: Antoni Juanico
 */
public class Documento {

    private Integer idDocumento;
    private String titulo;
    private String nombreFichero;
    private String idioma;
    private LocalDate fechaDocumento;
    private String usuario;
    private Long numChunks;

    public Documento() {}

    public Documento(Integer idDocumento, String titulo, String nombreFichero,
                      String idioma, LocalDate fechaDocumento, String usuario, Long numChunks) {
        this.idDocumento = idDocumento;
        this.titulo = titulo;
        this.nombreFichero = nombreFichero;
        this.idioma = idioma;
        this.fechaDocumento = fechaDocumento;
        this.usuario = usuario;
        this.numChunks = (numChunks != null) ? numChunks : 0L;
    }

    // Getters & Setters

    public Integer getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Integer idDocumento) { this.idDocumento = idDocumento; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getNombreFichero() { return nombreFichero; }
    public void setNombreFichero(String nombreFichero) { this.nombreFichero = nombreFichero; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public LocalDate getFechaDocumento() { return fechaDocumento; }
    public void setFechaDocumento(LocalDate fechaDocumento) { this.fechaDocumento = fechaDocumento; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario;}

    public Long getNumChunks() { return numChunks; }
    public void setNumChunks(Long numChunks) { this.numChunks = numChunks; }

    // Método de factoria
    public static Documento desdeFilaJPA(Object[] fila) {
        if (fila == null || fila.length < 7) {
            throw new IllegalArgumentException("La fila de base de datos no tiene el formato esperado");
        }

        return new Documento(
                (Integer) fila[0],         // idDocumento
                (String) fila[1],          // titulo
                (String) fila[2],          // nombreFichero
                (String) fila[3],          // idioma
                (LocalDate) fila[4],       // fechaDocumento
                (String) fila[5],          // usuario
                (Long) fila[6]             // numChunks
        );
    }
}
