package es.utamed.alejandria.ejb.interfaces;


import es.utamed.alejandria.ejb.models.DocumentoChunk;
import es.utamed.alejandria.ejb.models.Documento;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/*
   TFM, UTAMED
   Propósito: Interfaz del bean de negocio que permitirá indexar archivos y almacenar sus embeddings
              en la base de datos vectorial. También recuperar la información asociada.
   @since: 14/03/2026
   @author: Antoni Juanico
 */
public interface LibraryService {

    // Generar embeddings nuevo documento
    void insertarNuevoDocumento(String titulo, String nombreFichero, String idioma, LocalDate fechaDocumento, String usuario, InputStream fileContent);

    // Eliminar documento
    void borrarDocumento(Integer idDocumento);

    // Obtener lista de documentos
    List<Documento> obtenerListaDocumentos();

    // Obtener detalles documento
    Documento obtenerDocumento(Integer idDocumento);

    // Obtener los chunks de contenido similar
    List<DocumentoChunk> obtenerContenidoSimilar(String promptUsuario, int maxResults);

}
