package org.reactome.web.widgets.search;

import java.util.List;


/**
 * A search result with a total number of entries available.
 * The default number found is the number of entries.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface SearchResult {

    Integer getNumFound();

    List<Suggestion> getEntries();

}
