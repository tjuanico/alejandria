package es.utamed.alejandria.persistence.entity;

import es.utamed.alejandria.persistence.utils.VectorConverter;
import jakarta.persistence.*;
import jdk.jfr.Name;

/*
   TFM, UTAMED
   Propósito: Mapear la tabla RAG_DOCUMENTO_CHUNK de la base de datos.
   @since: 15/03/2026
   @author: Antoni Juanico
 */

@Entity
@Table(name="RAG_DOCUMENTO_CHUNK")
@NamedNativeQuery(
        name = "DocumentoChunkEntity.busquedaPorSimilitud",
        query = "SELECT chunk.id_chunk, chunk.content_chunk, " +
                "       VEC_DISTANCE_COSINE(chunk.embedding, :BytesFromEmbbedingUserText) AS distance " + // Recurda distancia = 0.0 (son conceptos iguales) por tanto similitud = 1.0 (son conceptos iguales)
                "       FROM RAG_DOCUMENTO_CHUNK chunk " +
                "       ORDER BY distance ASC " // al ser distancia ordenamos de manera ascendente
)
public class DocumentoChunkEntity {

    @Id
    @Column(name="id_chunk")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idChunk;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_documento", nullable = false)
    private DocumentoEntity documento;

    @Lob
    @Column(name = "content_chunk", nullable = false)
    private String contentChunk;

    @Column(name = "embedding", columnDefinition = "VECTOR(768)", nullable = false)
    @Convert(converter = VectorConverter.class)
    private float[] embedding;


    // Getters & Setters
    public Integer getIdChunk() { return idChunk; }
    public void setIdChunk(Integer idChunk) { this.idChunk = idChunk; }

    public DocumentoEntity getDocumento() { return documento; }
    public void setDocumento(DocumentoEntity documento) { this.documento = documento; }

    public String getContentChunk() { return contentChunk; }
    public void setContentChunk(String contentChunk) { this.contentChunk = contentChunk; }

    public float[] getEmbedding() { return embedding; }
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }
}
