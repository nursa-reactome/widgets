package org.reactome.web.widgets.paging;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * A slight improvement over the GWT {@link SimplePager} with the
 * following differences:
 * <ul>
 * <li>the last page button is shown by default</li>
 * <li>the fast forward button is hidden by default</li>
 * <li>paging aligns on the specified page size boundary<li>
 * </ul>
 * 
 * This pager respects the page size value set in the constructor
 * by ensuring that every page starts at the first row of the
 * page number modulo the page size. Thus, e.g., the last page
 * starts at the row following the penultimate page. The
 * GWT pager fills the last page visible display, including rows
 * from the penultimate page if necessary. Respecting the specified
 * page size arguably results in more intuitive and conventional
 * paging behaviour.
 * 
 * This implementation adapts the "last page" work-around of the
 * PathwayBrowser
 * <code>org.reactome.web.pwp.client.details.tabs.analysis.widgets.common.CustomPager</code>
 * implementation.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class Pager extends SimplePager {

    private int pageSize;

    /**
     * Creates the pager. Unlike the superclass no-arugment constructor,
     * this implementation shows the last page button and hides the fast
     * forward button.
     * 
     * @param pageSize the required page size
     */
    public Pager(int pageSize) {
        super(TextLocation.CENTER, false, true);
        this.setPageSize(pageSize);
    }

    /**
     * Get the page size.
     *
     * @return the page size
     * @see #setPageSize(int)
     */
    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Overrides {@link SimplePager#setPageSize(int)} to capture the
     * specified page size and use that in preference to the inferring
     * the page size from the display visible range.
     *
     * @param pageSize the page size
     */
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        super.setPageSize(pageSize);
    }

    /**
     * Overrides {@link SimplePager#setPageStart(int)} to set the page
     * start to the specified index. The superclass implementation
     * adjusts the last page start to fill the page. By contrast, this
     * pager adjusts the last page visible range length to show only the
     * items from the given index up to the last available row.
     *
     * @param index the index
     */
    @Override
    public void setPageStart(int index) {
        HasRows display = getDisplay();
        if (display != null) {
            Range range = display.getVisibleRange();
            if (index != range.getStart()) {
                int len;
                if (isRangeLimited() && display.isRowCountExact()) {
                    len = Math.min(pageSize, display.getRowCount() - index);
                } else {
                    len = pageSize;
                }
                display.setVisibleRange(index, len);
            }
        }
    }

    /**
     * Set the page start to the total row count minus the remainder of
     * the total row count divided by the page size.
     */
    @Override
    public void lastPageStart() {
        HasRows display = getDisplay();
        if (display != null) {
            setPageStart(display.getRowCount() % getPageSize());
        }
    }

    /**
     * Get the current page index.
     * 
     * @return the page index, or -1 if the display is not set
     * @see #setPage(int)
     */
    @Override
    public int getPage() {
        HasRows display = getDisplay();
        if (display == null) {
            return -1;
        }
        Range range = display.getVisibleRange();
        int pageSize = getPageSize();
        return (range.getStart() + pageSize - 1) / pageSize;
    }

    /**
     * Advance the starting row by {@link #getPageSize()} rows.
     */
    @Override
    public void nextPage() {
        HasRows display = getDisplay();
        if (display != null) {
            Range range = display.getVisibleRange();
            setPageStart(range.getStart() + getPageSize());
        }
    }

    /**
     * Move the starting row back by {@link #getPageSize()} rows.
     */
    @Override
    public void previousPage() {
        HasRows display = getDisplay();
        if (display != null) {
            Range range = display.getVisibleRange();
            setPageStart(range.getStart() - getPageSize());
        }
    }

    /**
     * Returns true if there is enough data such that a call to
     * {@link #nextPage()} will succeed in moving the starting point of
     * the table forward.
     *
     * @return true if there is a next page
     */
    @Override
    public boolean hasNextPage() {
        HasRows display = getDisplay();
        if (display == null || display.getRowCount() == 0) {
            return false;
        } else if (!display.isRowCountExact()) {
            return true;
        }
        return getPageStart() + getPageSize() < display.getRowCount();
    }

    /**
     * Returns true if there is enough data to display a given number of
     * additional pages.
     *
     * @param pages the number of pages to query
     * @return true if there are {@code pages} next pages
     */
    @Override
    public boolean hasNextPages(int pages) {
        HasRows display = getDisplay();
        if (display == null) {
            return false;
        }
        return getPageStart() + pages * getPageSize() < display.getRowCount();
    }

    /**
     * Returns true if there is enough data to display a given number of
     * previous pages.
     *
     * @param pages the number of previous pages to query
     * @return true if there are {@code pages} previous pages
     */
    @Override
    public boolean hasPreviousPages(int pages) {
        HasRows display = getDisplay();
        if (display == null) {
            return false;
        }
        return (pages - 1) * getPageSize() < getPageStart();
    }

    /**
     * Overrides {@link SimplePager} as follows:
     * <ul>
     * <li>Display "0 of 0" when there are no records (otherwise
     *     you get "1-1 of 0")</li>
     * <li>Display "1 of 1" when there is only one record
     *     (otherwise you get "1-1 of 1").</li>
     * <li>Display the correct number of items on the first page.</li>
     * 
     * This implementation extends the PathwayBrowser CustomPager
     * implementation to display the correct number of items on
     * the first page.
     */
    @Override
    protected String createText() {
        NumberFormat formatter = NumberFormat.getFormat("#,###");
        HasRows display = getDisplay();
        Range range = display.getVisibleRange();
        int pageStart = range.getStart() + 1;
        // Fix the following superclass bug:
        // * The page size is intended to be the number of items
        //   displayed. The range length is the number of rows
        //   which could be filled by items in the display. The
        //   intended page size is captured in the pageSize field.
        //int pageSize = range.getLength();
        int dataSize = display.getRowCount();
        int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
        endIndex = Math.max(pageStart, endIndex);
        boolean exact = display.isRowCountExact();
        if (dataSize == 0) {
            return "0 of 0";
        }
        String startStr = formatter.format(pageStart);
        String totalStr = formatter.format(dataSize);
        if ( pageStart == endIndex) {
            return startStr + " of " + totalStr;
        }
        String rangeStr = startStr + "-" + formatter.format(endIndex);
        return rangeStr + (exact ? " of " : " of over ") + totalStr;
    }

}
