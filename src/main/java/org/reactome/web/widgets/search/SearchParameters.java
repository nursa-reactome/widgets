package org.reactome.web.widgets.search;

/**
 * The data query search parameters includes the following fields:
 * <p><dl>
 * <dt>term</dt><dd>the search term</dd>
 * <dt>start</dt><dd>the starting offset of the page</dd>
 * <dt>size</dt><dd>the number of page rows to return</dd>
 * <dl></p>
 * 
 * The default start is zero.
 * If the page size is null, then all rows are returned.
 * The data supplier should raise an error if a request has
 * a missing or empty term.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SearchParameters {
    private String term;
    private int start = 0;
    private Integer size;

    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public int getStart() {
        return start;
    }
    
    public void setStart(int start) {
        this.start = start;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }

}
