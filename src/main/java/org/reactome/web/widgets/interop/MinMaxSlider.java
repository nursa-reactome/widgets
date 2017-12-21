package org.reactome.web.widgets.interop;

import com.google.gwt.dom.client.Element;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;


@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class MinMaxSlider {
    public static native Element create(Element sliderElt, Element[] inputElts, int[] bounds, int[] start);
    
    public static native int[] getValues(Element sliderElt);
}
