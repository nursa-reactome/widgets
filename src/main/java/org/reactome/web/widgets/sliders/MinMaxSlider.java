package org.reactome.web.widgets.sliders;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

public class MinMaxSlider {
    interface ConfigUiBinder extends UiBinder<TableElement, MinMaxSlider> {}
    private static final ConfigUiBinder uiBinder = GWT.create(ConfigUiBinder.class);

    @UiField TableCellElement slider;

    @UiField InputElement min;

    @UiField InputElement max;

    private Element root;
    
    public MinMaxSlider(int[] bounds, int[] start) {
        root = uiBinder.createAndBindUi(this);
        InputElement[] inputs = {min, max};
        // Delegate to the jsinterop wrapper.
        org.reactome.web.widgets.interop.MinMaxSlider.create(slider, inputs, bounds, start);
    }

    public Element getElement() {
        return root;
    }
    
    public int[] getValues() {
        return org.reactome.web.widgets.interop.MinMaxSlider.getValues(slider);
    }
}
