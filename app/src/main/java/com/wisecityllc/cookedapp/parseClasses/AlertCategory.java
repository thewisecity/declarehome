package com.wisecityllc.cookedapp.parseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by dexterlohnes on 10/2/15.
 */
@ParseClassName("AlertCategory")
public class AlertCategory extends ParseObject {

    final static String _TITLE = "title";
    final static String _TEXT_COLOR_R = "textR";
    final static String _TEXT_COLOR_G = "textG";
    final static String _TEXT_COLOR_B = "textB";

    final static String _BG_COLOR_R = "backgroundR";
    final static String _BG_COLOR_G = "backgroundG";
    final static String _BG_COLOR_B = "backgroundB";

    public AlertCategory() {
        // A default constructor is required.
    }

    public String getTitle() { return getString(_TITLE); }

    public Number getTextColorR() { return getNumber(_TEXT_COLOR_R); }

    public Number getTextColorG() { return getNumber(_TEXT_COLOR_G); }

    public Number getTextColorB() {
        return getNumber(_TEXT_COLOR_B);
    }

    public Number getBGColorR() { return getNumber(_BG_COLOR_R); }

    public Number getBGColorG() {
        return getNumber(_BG_COLOR_G);
    }

    public Number getBGColorB() {
        return getNumber(_BG_COLOR_B);
    }
}
