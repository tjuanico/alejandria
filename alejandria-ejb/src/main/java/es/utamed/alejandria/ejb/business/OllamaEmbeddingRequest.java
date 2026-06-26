package es.utamed.alejandria.ejb.business;

import jakarta.json.bind.annotation.JsonbProperty;

public record OllamaEmbeddingRequest( @JsonbProperty("model") String model,
                                      @JsonbProperty("input") String input ) {
}

