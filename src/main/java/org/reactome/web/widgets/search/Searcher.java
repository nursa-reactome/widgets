package org.reactome.web.widgets.search;

import java.util.function.Consumer;

public interface Searcher {
    /**
     * Searches for items which matches the given parameters.
     * The implementor is responsible for calling the consumer
     * accept() method with the search result when the search
     * is completed.
     */
    void search(SearchParameters parameters, Consumer<SearchResult> consumer);
}