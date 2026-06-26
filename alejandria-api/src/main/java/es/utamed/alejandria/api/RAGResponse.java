package es.utamed.alejandria.api;

import java.io.Serializable;
import java.util.List;

public class RAGResponse implements Serializable {

    private List<RAGItem> fragmentos;
    private String resultado;

    // Constructores
    public RAGResponse() {}

    public RAGResponse(List<RAGItem> fragmentos, String resultado) {
        this.fragmentos = fragmentos;
        this.resultado = resultado;

    }

    // Getter & Setter
    public List<RAGItem> getFragmentos() { return fragmentos; }
    public void setFragmentos(List<RAGItem> fragmentos) { this.fragmentos = fragmentos; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}
