package es.utamed.alejandria.api;

import es.utamed.alejandria.ejb.models.DocumentoChunk;

import java.io.Serializable;

public class RAGItem implements Serializable {
    private String texto;
    private Float score;

    // Constructor
    public RAGItem() {}

    public RAGItem(String texto, Float score) {
        this.texto = texto;
        this.score = score;
    }

    public RAGItem(DocumentoChunk chunk) {
        this.texto = chunk.getChunk();
        this.score = chunk.getSimilitud();
    }

    // Getter & Setters
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }
}
