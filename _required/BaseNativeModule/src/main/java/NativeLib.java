package me.android.modules;

public class NativeLib {

    // Used to load the 'modules' library on application startup.
    static {
        System.loadLibrary("nativelib.cpp");
    }

    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}