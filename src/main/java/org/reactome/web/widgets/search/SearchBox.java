package org.reactome.web.widgets.search;

import java.util.function.Consumer;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A common TextBox that provides changed text value to a consumer.
 * It waits for the specified time without changes to the value
 * before invoking the consumer to avoid unnecessary searches while
 * the user is still typing. The consumer is invoked if the following
 * conditions are met:
 * <ul>
 * <li>the specified delay has elapsed since the input</li>
 * <li>the input is at least three characters long</li>
 * </ul>
 *
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class SearchBox extends TextBox {

    private static String OPENING_TEXT = "Enter search term ...";
    private static final int DEF_DELAY = 250;
    private static final int MIN_INPUT_LEN = 3;
    
    private class ValueChecker {
        private int delay;
        private Timer timer;
        private String value;
        private boolean start = true;

        public ValueChecker(int delay) {
            this.delay = delay;
            this.timer = new Timer() {
                
                @Override
                public void run() {
                    // Recheck.
                    SearchBox.this.checkContent();
                }
                
            };
        }
        
        public boolean check(String value) {
            if (this.start) {
                // Start the timer.
                this.start = false;
                this.timer.schedule(this.delay);
                return false;
            }
            boolean isChanged = isChanged(value);
            if (this.timer.isRunning()) {
                // Need to wait.
                if (isChanged) {
                    this.timer.schedule(this.delay);
                }
                return false;
            } else if (isActionable(this.value) || isActionable(value)) {
                // The input is or was at least 3 characters.
                if (isChanged) {
                    // We have a winner; reset the value and start over.
                    this.start = true;
                    this.value = value;
                }
                return isChanged;
            } else {
                // Not enough characters; restart the timer.
                this.timer.schedule(this.delay);
                return false;
            }
        }
        
        private boolean isChanged(String value) {
            return value == null ? this.value != null : !value.equals(this.value);
        }
        
     }

    /**
     * This InputEvent class is used internally only be the search box to
     * capture user text input actions.
     *
     */
    private static class InputEvent extends DomEvent<InputEvent.InputHandler> {

        private static final Type<InputHandler> TYPE =
                new Type<InputHandler>("input", new InputEvent());
  
        interface InputHandler extends EventHandler {

            void onInput(InputEvent event);

        }

        public static Type<InputHandler> getType() {
            return TYPE;
        }

        @Override
        public final Type<InputHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(InputHandler handler) {
            handler.onInput(this);
        }

    }    
 
    private ValueChecker checker;
    private Consumer<String> consumer;

    public SearchBox(Consumer<String> consumer) {
        this(consumer, DEF_DELAY);
    }

    public SearchBox(Consumer<String> consumer, int searchDelay) {
        this.consumer = consumer;
        this.checker = new ValueChecker(searchDelay);
        getElement().setPropertyString("placeholder", OPENING_TEXT);
        // Plug into user text entry to detect when to recognize when
        // the entry constitutes an actionable search term.
        // cf. https://stackoverflow.com/questions/3184648/instant-value-change-handler-on-a-gwt-textbox.
        InputEvent.InputHandler handler = new InputEvent.InputHandler() {

            @Override
            public void onInput(InputEvent event) {
                checkContent();
            }

        };
        addDomHandler(handler, InputEvent.getType());
    }
 
    private void checkContent() {
        String value = getText();
        if (this.checker.check(value)) {
            // If there aren't enough characters, then clear the search
            // by returning a null.
            if (!isActionable(value)) {
                value = null;
            }
            this.consumer.accept(value);
        }
    }

    private boolean isActionable(String value) {
        return !(value == null || value.length() < MIN_INPUT_LEN);
    }

}
