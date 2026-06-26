package es.utamed.alejandria.ejb.models;


/*
   TFM, UTAMED
   Clase DocumentoChunk embeddings, fragmentos de un documento
   @since: 15/03/2026
   @author: Antoni Juanico
 */
public class DocumentoChunk {
    private String chunk;
    private Float similitud;

    // Constructores
    public DocumentoChunk() {}
    public DocumentoChunk(String chunk, Float similitud) {
        this.chunk = chunk;
        this.similitud = similitud;
    }

    // Getters & Setters
    public String getChunk() { return chunk; }
    public void setChunk(String chunk) { this.chunk = chunk; }

    public Float getSimilitud() { return similitud; }
    public void setSimilitud(Float similitud) { this.similitud = similitud; }
}
