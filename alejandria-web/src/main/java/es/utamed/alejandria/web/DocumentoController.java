package es.utamed.alejandria.web;

import es.utamed.alejandria.ejb.interfaces.LibraryService;
import es.utamed.alejandria.ejb.models.Documento;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Named
@ViewScoped
public class DocumentoController implements Serializable {

    private UploadedFile file;
    private String nombreFichero;
    private String idioma;
    private String titulo;
    private LocalDate fechaDocumento;

    @Inject
    private LibraryService libraryServiceBean;

    // Getters & Setters
    public String getNombreFichero() { return nombreFichero; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getFechaDocumento() { return fechaDocumento; }
    public void setFechaDocumento(LocalDate fechaDocumento) { this.fechaDocumento = fechaDocumento; }

    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }

    // Métodos públicos
    public void addDocumento(){

        try {

            final String usuarioActual = Optional.ofNullable(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal())
                    .map(Principal::getName)
                    .orElse("anonymous");

            if (file != null) {
                this.nombreFichero = file.getFileName();
                this.libraryServiceBean.insertarNuevoDocumento(this.titulo,
                        this.nombreFichero,
                        this.idioma,
                        this.fechaDocumento,
                        usuarioActual,
                        file.getInputStream());
                PrimeFaces.current().dialog().closeDynamic(Boolean.TRUE);
            }
            else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Guardar documento", "Debe adjuntar un fichero en formato pdf"));
                PrimeFaces.current().dialog().closeDynamic(null);
            }
        }
        catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Guardar documento", "Error: " + ex.toString()));
            PrimeFaces.current().dialog().closeDynamic(null);
        }

    }

    public void closeDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }
}
