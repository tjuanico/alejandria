package es.utamed.alejandria.api;

import java.io.Serializable;

public class RAGRequest implements Serializable {
    private String userPrompt;
    private Integer maxItems;

    // Constructores
    public RAGRequest() {}

    public RAGRequest(String userPrompt, Integer maxItems) {
        this.userPrompt = userPrompt;
        this.maxItems = maxItems;
    }

    // Getters 6 Setters
    public String getUserPrompt() { return userPrompt; }
    public void setUserPrompt(String userPrompt) { this.userPrompt = userPrompt;}

    public Integer getMaxItems() { return maxItems; }
    public void setMaxItems(Integer maxItems) { this.maxItems = maxItems;}
}
