package Helpers;

import com.owlike.genson.Genson;

/**
 * Created by Mindaugas on 23/04/16.
 */
public class GensonHelpers {

    private static Genson instance = null;
    private GensonHelpers() {
        // Exists only to defeat instantiation.
    }
    public static Genson getInstance() {
        if(instance == null) {
            instance = new Genson();
        }
        return instance;
    }

}
