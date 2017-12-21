package org.reactome.web.widgets.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;


/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class AbstractAccordionPanel extends FlowPanel implements AccordionPanel {

    @Override
    public void onPanelCollapsed(PanelCollapsedEvent event) {
        getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    @Override
    public void onPanelExpanded(PanelExpandedEvent event) {
        getElement().getStyle().setDisplay(Style.Display.INLINE);
    }
}
