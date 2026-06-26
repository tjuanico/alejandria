package es.utamed.alejandria.ejb.services;

import es.utamed.alejandria.ejb.exceptions.DocumentoNoEncontradoException;
import es.utamed.alejandria.ejb.interfaces.LibraryService;
import es.utamed.alejandria.ejb.models.DocumentoChunk;
import es.utamed.alejandria.ejb.models.Documento;
import es.utamed.alejandria.ejb.business.Chunkify;
import es.utamed.alejandria.ejb.business.Embedding;
import es.utamed.alejandria.ejb.utils.PersistFile;
import es.utamed.alejandria.persistence.entity.DocumentoChunkEntity;
import es.utamed.alejandria.persistence.entity.DocumentoEntity;
import es.utamed.alejandria.persistence.utils.VectorConverter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
   TFM, UTAMED
   Propósito: Implementación LibraryService.
   @since: 14/03/2026
   @author: Antoni Juanico
 */


@Stateless
public class LibraryServiceImpl implements LibraryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryServiceImpl.class);

    @PersistenceContext(unitName="alejandriaPU")
    EntityManager em;

    @Inject
    private ConfigurationService configService;

    @Override
    @RolesAllowed({"ALE_ADMIN"})
    public void insertarNuevoDocumento(String titulo, String nombreFichero, String idioma, LocalDate fechaDocumento, String usuario, InputStream fileContent) {

        byte[] pdfBytes;
        try {
            pdfBytes = fileContent.readAllBytes();
        } catch (IOException ex) {
            LOGGER.error("No se ha podido leer el stream del PDF: " + ex.toString());
            throw new RuntimeException(ex);
        }

        try (PDDocument doc = Loader.loadPDF(pdfBytes)){ // 2. Leer el pdf

            // 3. Preparamos el documento que guardaremos en base de datos
            DocumentoEntity documento = new DocumentoEntity();
            documento.setFechaDocumento(fechaDocumento);
            documento.setTitulo(titulo);
            documento.setIdioma(idioma);
            documento.setNombreFichero(nombreFichero);
            documento.setUsuario(usuario);

            // 4. Dividir en chunks con solapación
            Chunkify chunkify = new Chunkify(doc);
            List<String> chunks = chunkify.getChunks();

            String urlEmbeddingProvider = configService.getValor("urlEmbeddingProvider");
            String embeddingModel = configService.getValor("embeddingModel");
            String bearerToken = configService.getValor("bearerToken");

            LOGGER.info("urlEmbeddingProvider: " +urlEmbeddingProvider);
            LOGGER.info("embeddingModel: " +embeddingModel);

            Embedding embedding = new Embedding(urlEmbeddingProvider,embeddingModel,bearerToken);

            List<DocumentoChunkEntity> chunksDocumento = new ArrayList<>();

            for  (String chunk : chunks) {
                // 5. Para cada chunk calcular su embedding
                float[] emb = embedding.generateEmbedding(chunk);
                LOGGER.info("Tamaño del vector generado: {}", emb.length);

                // 6. Guardar el embedded en la base de datos vectorial
                DocumentoChunkEntity documentoChunkEntity = new DocumentoChunkEntity();
                documentoChunkEntity.setEmbedding(emb);
                documentoChunkEntity.setContentChunk(chunk);
                documentoChunkEntity.setDocumento(documento);

                chunksDocumento.add(documentoChunkEntity);
            }
            documento.setChunks(chunksDocumento);
            em.persist(documento);

            String pathPDFAlejandria = configService.getValor("pathPdf");
            LOGGER.info("path alejandria: " +pathPDFAlejandria);

            PersistFile persistFile = new PersistFile(pathPDFAlejandria, nombreFichero);
            persistFile.save(new ByteArrayInputStream(pdfBytes));

        }
        catch (IOException ex) {
            LOGGER.error("No se ha podido leer el texto del PDF o bien guardar el pdf en disco: " + ex.toString());
            throw new RuntimeException(ex);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            LOGGER.error("Interrupción al obtener embedding: " + ex.toString());
            throw new RuntimeException(ex);
        }
        catch (Exception ex) {
            LOGGER.error("Error inesperado procesando el documento: " + ex.toString());
            throw new RuntimeException(ex);
        }

    }

    @Override
    @RolesAllowed({"ALE_ADMIN"})
    public void borrarDocumento(Integer idDocumento) {
        em.remove(em.getReference(DocumentoEntity.class, idDocumento));
    }

    @Override
    @RolesAllowed({"ALE_ADMIN","ALE_USER"})
    public List<Documento> obtenerListaDocumentos() {

        List<Object[]> resultados = em.createNamedQuery("DocumentoEntity.obtenerListaDocumentos", Object[].class).getResultList();
        return resultados.stream()
                .map(Documento::desdeFilaJPA)
                .toList();
    }

    @Override
    @RolesAllowed({"ALE_ADMIN","ALE_USER"})
    public Documento obtenerDocumento(Integer idDocumento) {

        try {
            Object[] fila = (Object[]) em.createNamedQuery("DocumentoEntity.obtenerDocumento", Object[].class)
                .setParameter("idDocumento", idDocumento)
                .getSingleResult();

            return Documento.desdeFilaJPA(fila);

        }
        catch (jakarta.persistence.NoResultException e) {
            throw new DocumentoNoEncontradoException("El documento con ID " + idDocumento + " no existe.");
        }

    }

    @Override
    @RolesAllowed({"ALE_ADMIN", "ALE_API", "ALE_USER"})
    public List<DocumentoChunk> obtenerContenidoSimilar(String promptUsuario, int maxResults) {
        try {

            // Obtenemos los embbedings del promptUsuario
            VectorConverter vectorConverter = new VectorConverter();
            String urlEmbeddingProvider = configService.getValor("urlEmbeddingProvider");
            String embeddingModel = configService.getValor("embeddingModel");
            String bearerToken =  configService.getValor("bearerToken");

            LOGGER.info("urlEmbeddingProvider: " +urlEmbeddingProvider);
            LOGGER.info("embeddingModel: " +embeddingModel);

            Embedding embedding = new Embedding(urlEmbeddingProvider,embeddingModel,bearerToken);

            float[] embbedingUserText = embedding.generateEmbedding(promptUsuario);
            byte[] bytesFromEmbeddingUserText = vectorConverter.convertToDatabaseColumn(embbedingUserText);

            // Realizamos la consulta en base de datos
            Query query = em.createNamedQuery("DocumentoChunkEntity.busquedaPorSimilitud");
            query.setParameter("BytesFromEmbbedingUserText", bytesFromEmbeddingUserText);
            query.setMaxResults(maxResults);

            // Generamos resultado
            List<Object[]> chunksResultado = query.getResultList();
            List<DocumentoChunk> chunksEncontrados = new ArrayList<>();

            for (Object[] c : chunksResultado) {
                String textoChunk = (String) c[1];
                Float distancia = ((Number) c[2]).floatValue();
                Float similitud = 1-distancia; // Importante distancia = 1-similitud <--> similitud = 1 - distancia
                DocumentoChunk chunk = new DocumentoChunk(textoChunk, similitud);
                chunksEncontrados.add(chunk);
            }

            return chunksEncontrados;
        }
        catch(Exception ex) {
            LOGGER.error("Error inesperado obteniendo contenido similar: " + ex.toString());
            throw new RuntimeException(ex);
        }

    }

}

