package com.example.infs_project.model;

public class SearchResponse {
    public Recipe[] getResults() {
        return results;
    }

    public void setResults(Recipe[] results) {
        this.results = results;
    }

    private Recipe[] results;
}
