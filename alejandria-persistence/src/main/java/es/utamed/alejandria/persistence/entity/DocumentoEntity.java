package es.utamed.alejandria.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


/*
   TFM, UTAMED
   Propósito: Mapear la tabla RAG_DOCUMENTO de la base de datos.
   @since: 14/03/2026
   @author: Antoni Juanico
 */

@Entity
@Table(name="RAG_DOCUMENTO")
@NamedQueries({
@NamedQuery(
        name = "DocumentoEntity.obtenerListaDocumentos",
        query = "SELECT d.idDocumento, d.titulo, d.nombreFichero, d.idioma, " +
                "       d.fechaDocumento, d.usuario, COUNT(c.idChunk) " +
                "FROM DocumentoEntity d " +
                "LEFT JOIN d.chunks c " +
                "GROUP BY d.idDocumento, d.titulo, d.nombreFichero, d.idioma, d.fechaDocumento, d.usuario"),
@NamedQuery(
        name = "DocumentoEntity.obtenerDocumento",
        query = "SELECT d.idDocumento, d.titulo, d.nombreFichero, d.idioma, " +
                "       d.fechaDocumento, d.usuario, COUNT(c.idChunk) " +
                "FROM DocumentoEntity d " +
                "LEFT JOIN d.chunks c " +
                "WHERE d.idDocumento = :idDocumento " +
                "GROUP BY d.idDocumento, d.titulo, d.nombreFichero, d.idioma, d.fechaDocumento, d.usuario")
})
public class DocumentoEntity {

    @Id
    @Column(name="id_documento")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idDocumento;

    @Column(name="titulo", nullable = false)
    private String titulo;

    @Column(name="fichero", nullable = false)
    private String nombreFichero;

    @Column(name="idioma", nullable = false)
    private String idioma;

    @Column(name="fecha_documento")
    private LocalDate fechaDocumento;

    @Column(name="usuario", nullable = false)
    private String usuario;

    @OneToMany(mappedBy = "documento", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DocumentoChunkEntity> chunks = new java.util.ArrayList<>();

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
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public java.util.List<DocumentoChunkEntity> getChunks() { return chunks; }
    public void setChunks(java.util.List<DocumentoChunkEntity> chunks) { this.chunks = chunks; }
}
