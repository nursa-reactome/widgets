package org.reactome.web.widgets.search;

import java.util.function.Consumer;

import org.reactome.web.widgets.paging.Pager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The suggestion combo box widget builder. The widget instance
 * is accessed by the {@link #asWidget()} method.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SuggestionComboBox<C extends Suggestion> implements IsWidget,
        SelectionChangeEvent.Handler {
    
    /**
     * The UiBinder interface.
     */
    interface Binder extends UiBinder<DockLayoutPanel, SuggestionComboBox<?>> {
    }
    static final Binder uiBinder = GWT.create(Binder.class);

    private static final int DEF_PAGE_SIZE = 4;

    /**
     * The search key accessor for fetching the item.
     */
    private static final ProvidesKey<Suggestion> KEY_PROVIDER =
            item -> item == null ? null : item.getKey();
    
    private Consumer<C> consumer;

    private SingleSelectionModel<Suggestion> selectionModel;
    
    /**
     * The input text box.
     */
    @UiField(provided = true)
    TextBox input;
    
    /**
     * The suggestions list.
     */
    @UiField(provided = true)
    CellList<Suggestion> suggestions;
 
    /**
     * The suggestions pager.
     */
    @UiField(provided = true)
    SimplePager pager;

    private DockLayoutPanel widget;

    /**
     * Creates the combo box with page size 4.
     * 
     * @param searcher the data supplier  
     * @param consumer the suggestion selection consumer
     * @return the combo box widget
     */
    public SuggestionComboBox(Searcher searcher, Consumer<C> consumer) {
        this(searcher, consumer, DEF_PAGE_SIZE);
    }

    /**
     * Creates the combo box.
     * 
     * @param searcher the data supplier  
     * @param consumer the suggestion selection consumer
     * @param pageSize the maximum number of suggestion
     */
    public SuggestionComboBox(Searcher searcher, Consumer<C> consumer, int pageSize) {
        this.consumer = consumer;
        // The suggestions list.
        SuggestionCell<Suggestion> suggestionCell = new SuggestionCell<Suggestion>();
        suggestions = new CellList<Suggestion>(suggestionCell, KEY_PROVIDER);
        suggestions.sinkEvents(Event.FOCUSEVENTS);
        suggestions.addStyleName(RESOURCES.getCSS().list());
        suggestions.setKeyboardPagingPolicy(HasKeyboardPagingPolicy.KeyboardPagingPolicy.INCREASE_RANGE);
        selectionModel = new SingleSelectionModel<Suggestion>(KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(this);
        suggestions.setSelectionModel(selectionModel);
        suggestions.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);

        // The auto-complete search input text box.
        final SearchDataProvider dataProvider = new SearchDataProvider(searcher, pageSize);
        dataProvider.addDataDisplay(suggestions);
        Consumer<String> termConsumer = new Consumer<String>() {

            @Override
            public void accept(String term) {
                dataProvider.setTerm(term);
            }
 
        };
        input = new SearchBox(termConsumer);
        input.addStyleName(RESOURCES.getCSS().input());
        
        // The suggestions pager.
        // Oddly, the pager default is to show a fast forward
        // button but hide the last page button. This is contrary
        // to conventional pager behaviour. The boolean arguments
        // in the following constructor restore this convention.
        // The location argument centers the pager text under the
        // buttons.
        pager = new Pager(pageSize);
        pager.addStyleName(RESOURCES.getCSS().pager());
        pager.setDisplay(suggestions);
        widget = uiBinder.createAndBindUi(this);
    }
    
    @Override
    public Widget asWidget() {
        return this.widget;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        Suggestion selection = selectionModel.getSelectedObject();
        this.consumer.accept((C) selection);
    }

    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }
 
    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
 
        /**
         * The styles used in this widget.
         */
        @Source(Css.CSS)
        Css getCSS();

    }

    /**
     * Styles used by this widget.
     */
    public interface Css extends CssResource {
 
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "SuggestionComboBox.css";

        String main();

        String input();

        String list();

        String pager();

    }
}
