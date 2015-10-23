package com.wisecityllc.cookedapp.parseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by dexterlohnes on 10/2/15.
 */
@ParseClassName("WebAddress")
public class WebAddress extends ParseObject {

    final public static String _TITLE = "title";
    final public static String _URL = "url";

    final public static String _FAQ_TITLE = "faq";
    final public static String _ABOUT_TITLE = "about";

    public WebAddress() {
        // A default constructor is required.
    }

    public String getTitle() { return getString(_TITLE); }

    public String getURL() { return getString(_URL); }
}
