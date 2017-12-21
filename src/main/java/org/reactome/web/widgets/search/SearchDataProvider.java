package org.reactome.web.widgets.search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class SearchDataProvider extends AsyncDataProvider<Suggestion> {

    private Searcher searcher;
    private SearchParameters searchParameters;
    private Map<Integer, List<Suggestion>> cache =
            new HashMap<Integer, List<Suggestion>>();

    public SearchDataProvider(Searcher searcher, int pageSize) {
        this.searcher = searcher;
        this.searchParameters = new SearchParameters();
        this.searchParameters.setSize(new Integer(pageSize));
    }

    @Override
    protected void onRangeChanged(HasData<Suggestion> display) {
        // Initialization calls this method before a search term
        // is entered or there are any results to display. We
        // only want to fire off a search if there is a search
        // term.
        if (this.searchParameters.getTerm() != null) {
            // Get the new range.
            Range range = display.getVisibleRange();
            int start = range.getStart();
            this.searchParameters.setStart(start);
            this.searchParameters.setSize(new Integer(range.getLength()));
            // Changing the page deselects a selected row.
            // If this is not done, then the corresponding
            // row is selected on the new page.
            List<Suggestion> cached = this.cache.get(start);
            if (cached == null) {
                search();
            } else {
                updateRowData(start, cached);
            }
        }
    }

    public void setTerm(String term) {
        this.searchParameters.setTerm(term);
        this.searchParameters.setStart(0);
        this.cache.clear();
        
        if (term == null) {
            updateRowCount(0, false);
            updateRowData(0, Collections.emptyList());
        } else {
            search();
        }
    }

    private void search() {
        final int start = this.searchParameters.getStart();
        Consumer<SearchResult> searchConsumer = new Consumer<SearchResult>() {

            @Override
            public void accept(SearchResult searchResult) {
                // Per the SearchResult interface contract, the default
                // number found is the entries count.
               Integer numFound = searchResult.getNumFound();
               List<Suggestion> entries = searchResult.getEntries();
               int rowCnt = numFound == null ? entries.size() : numFound.intValue();
               updateRowCount(rowCnt, true);
               updateRowData(start, entries);
            }
            
        };
        this.searcher.search(searchParameters, searchConsumer);
    }

    @Override
    public void updateRowData(int start, List<Suggestion> values) {
        this.cache.put(start, values);
        super.updateRowData(start, values);
    }
    
    

}
