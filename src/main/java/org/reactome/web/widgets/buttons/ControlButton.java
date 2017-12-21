package org.reactome.web.widgets.buttons;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class ControlButton extends Button {

    public ControlButton(String title, String style, ClickHandler handler) {
        setStyleName(style);
        addClickHandler(handler);
        setTitle(title);
    }
}
