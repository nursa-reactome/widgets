package org.reactome.web.widgets.panels;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class PanelExpandedEvent extends GwtEvent<PanelExpandedEvent.Handler> {
    public static Type<Handler> TYPE = new Type<>();
 
    public interface Handler extends EventHandler {
        void onPanelExpanded(PanelExpandedEvent event);
    }

    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onPanelExpanded(this);
    }

}
