package es.utamed.alejandria.ejb.business;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


/*
   TFM, UTAMED
   Propósito: Obtenier el embedding de un chunk
   @since: 18/03/2026
   @author: Antoni Juanico
 */
public class Embedding {


    private String urlEmbeddingProvider;
    private String embeddingModel;
    private String bearerToken;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    // Constructor
    public Embedding(String urlEmbeddingProvider, String embeddingModel, String bearerToken) {
        this.urlEmbeddingProvider = urlEmbeddingProvider;
        this.embeddingModel = embeddingModel;
        this.bearerToken = bearerToken;
    }

    // Obtención del emmbedding
    public float[] generateEmbedding(String chunk) throws Exception {

        try (Jsonb jsonb = JsonbBuilder.create()) {
            String jsonOllamaEmbeddingRequest = jsonb.toJson(new OllamaEmbeddingRequest(this.embeddingModel, chunk));


            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(this.urlEmbeddingProvider))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonOllamaEmbeddingRequest));

            if (this.bearerToken != null && !this.bearerToken.isBlank()) {
                requestBuilder.header("Authorization", "Bearer " + this.bearerToken);
            }

            HttpRequest request = requestBuilder.build();

            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

            try (InputStream is = response.body()) {
                OllamaEmbeddingResponse embeddingResponse = jsonb.fromJson(is, OllamaEmbeddingResponse.class);

                return embeddingResponse.embeddings()[0];
            }

        }


    }

}
