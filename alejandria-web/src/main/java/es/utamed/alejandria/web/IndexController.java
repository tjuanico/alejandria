package es.utamed.alejandria.web;

import es.utamed.alejandria.ejb.interfaces.LibraryService;
import es.utamed.alejandria.ejb.models.Documento;
import es.utamed.alejandria.ejb.models.DocumentoChunk;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class IndexController implements Serializable {

    private List<Documento> documentos;

    // Funcionalidad de prueba de búsqueda
    private List<DocumentoChunk> fragmentos;
    private String promptUsuario;
    private Integer maxResults;

    @Inject
    private LibraryService libraryServiceBean;

    // Callbacks
    @PostConstruct
    public void init() {
        documentos = this.libraryServiceBean.obtenerListaDocumentos();
        maxResults = 3;
    }

    // Getter & Setter
    public List<Documento> getDocumentos() { return documentos; }

    public String getPromptUsuario() { return promptUsuario; }
    public void setPromptUsuario(String promptUsuario) { this.promptUsuario = promptUsuario; }

    public List<DocumentoChunk> getFragmentos() { return fragmentos; }

    public Integer getMaxResults() { return maxResults; }
    public void setMaxResults(Integer maxResults) { this.maxResults = maxResults; }

    // Métodos públicos
    public void openDocumentoDialog() {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .modal(true)
                .width("950")
                .height("500")
                .contentHeight("100%")
                .contentWidth("100%")
                .headerElement("customheader")
                .build();

        PrimeFaces.current().dialog().openDynamic("documentoModal", options, null);
    }

    public void openDocumentoDialog(Long idDocumento) {
        DialogFrameworkOptions options = DialogFrameworkOptions.builder()
                .modal(true)
                .width("950")
                .height("500")
                .contentHeight("100%")
                .contentWidth("100%")
                .headerElement("customheader")
                .styleClass("dialogo-solido")
                .build();

        Map<String, List<String>> params = Map.of("idDocumento", List.of(idDocumento.toString()));
        PrimeFaces.current().dialog().openDynamic("documentoModal", options, params);
    }

    public void updateDocumentosFromModal(SelectEvent<Boolean> event) {
        if (event.getObject() != null) {
            documentos = this.libraryServiceBean.obtenerListaDocumentos();
            FacesMessage message = new FacesMessage("Documento indexado correctamente", "Documento guardado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void buscar() {
        this.fragmentos = this.libraryServiceBean.obtenerContenidoSimilar(this.promptUsuario,this.maxResults);
    }

    public void deleteDocumento(Integer idDocumento) {
        try {
            this.libraryServiceBean.borrarDocumento(idDocumento);
            documentos = this.libraryServiceBean.obtenerListaDocumentos();
            FacesMessage message = new FacesMessage("Documento borrado correctamente", "Documento " + idDocumento + " borrado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        catch (Exception ex) {
            FacesMessage message = new FacesMessage("Error borrando documento", "Error: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

}
