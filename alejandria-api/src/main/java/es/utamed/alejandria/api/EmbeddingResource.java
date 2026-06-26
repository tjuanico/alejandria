package es.utamed.alejandria.api;

import es.utamed.alejandria.ejb.interfaces.LibraryService;
import es.utamed.alejandria.ejb.models.DocumentoChunk;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Path("/api")
public class EmbeddingResource {

    private static final Integer DEFAULT_MAX_RAG_RESULT = 3;

    @Inject
    LibraryService libraryServiceBean;

    @POST
    @Path("/similitud")
    @RolesAllowed("ALE_API")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFragmentosSimilares(RAGRequest peticion) {
        try {
            Integer maxItems = peticion.getMaxItems() != null ?  peticion.getMaxItems() : DEFAULT_MAX_RAG_RESULT;

            List<DocumentoChunk> respuestaAlejandria = libraryServiceBean.obtenerContenidoSimilar( peticion.getUserPrompt(), maxItems);
            List<RAGItem> fragmentos = respuestaAlejandria.stream().map(RAGItem::new).toList();

            RAGResponse respuesta = new RAGResponse(fragmentos, "ok");

            return Response.ok(respuesta).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new RAGResponse(new ArrayList<>(), "ko"))
                    .build();
        }
    }
}
