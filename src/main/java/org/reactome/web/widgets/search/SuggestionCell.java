package org.reactome.web.widgets.search;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
/**
 * The cell datum includes a required key and title. The title is
 * the primary text displayed in the suggestion cell. The title is
 * elided if it exceeds the cell length, but the full title is
 * displayed in a tooltip. The cell datum can also include
 * one or more secondary text items. These secondary items are
 * displayed under the title in a smaller font size.
 *
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SuggestionCell<C extends Suggestion> extends AbstractCell<C> {
    
    /**
     * The maximum suggestion item length before it is elided in the display.
     *
     * Note - this value must be no greater than the CSS max-width cut-off
     * in small em units = 125% of normal em units.
     */
    private static final int ELISION_LEN = 35;

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
        static final String SANS_TOOLTIP = "<div class=\"{0}\">{1}</div>";
        static final String WITH_TOOLTIP = "<div class=\"{0}\" title=\"{1}\">{2}</div>";
  
        /**
         * The cell item template is a div whose title and content are the value.
         * Since the html parameter type is {@link SafeHtml}, it will not be escaped
         * before including it in the template.
         * 
         * @return a {@link SafeHtml} instance
         */
        @Template(SANS_TOOLTIP)
        SafeHtml sansTooltip(String style, SafeHtml html);

        @Template(WITH_TOOLTIP)
        SafeHtml withTooltip(String style, String toolTip, SafeHtml html);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    @Override
    public void render(Context context, C value, SafeHtmlBuilder sb) {
        // Always do a null check on the value. Cell widgets can pass null to
        // cells if the underlying data contains a null, or if the data arrives
        // out of order.
        if (value == null) {
            return;
        }
        String title = value.getTitle();
        if (title == null || title.isEmpty()) {
            String msg = "The suggestion cell is missing a title";
            throw new IllegalArgumentException(msg);
        }
        SafeHtml html = format(title);
        sb.append(html);
        for (String secondary : value.getSecondary()) {
            html = format(secondary);
            sb.append(html);
        }
    }

    private SafeHtml format(String text) {
        String style = RESOURCES.getCSS().field();
        SafeHtml html = SafeHtmlUtils.fromTrustedString(text);
        if (text.length() < ELISION_LEN) {
            return templates.sansTooltip(style, html);
        } else {
            return templates.withTooltip(style, text, html);
        }
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
        String CSS = "SuggestionCell.css";

        String main();

        String field();

    }

}
