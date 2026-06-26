package es.utamed.alejandria.ejb.business;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.List;

public record OllamaEmbeddingResponse(
        @JsonbProperty("model")            String model,
        @JsonbProperty("embeddings")       float[][] embeddings,
        @JsonbProperty("total_duration")   Long totalDuration,
        @JsonbProperty("load_duration")    Long loadDuration,
        @JsonbProperty("prompt_eval_count") Integer promptEvalCount
) {}