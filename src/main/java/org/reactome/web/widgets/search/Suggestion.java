package org.reactome.web.widgets.search;

import java.util.Collections;
import java.util.List;

/**
 * The suggestion displayed in the search result.
 */
public interface Suggestion {

    /**
     * @return the value returned if the suggestion is selected
     */
    Object getKey();

    /**
     * @return the required primary display text
     */
    String getTitle();
    
    /**
     * @return the optional secondary display text items
     */
    default List<String> getSecondary() {
        return Collections.emptyList();
    }

}