package me.arithesage.java.libs;

import java.util.Collection;
import java.util.Random;




public class Utils {
    /**
     * Returns an array from a list of objects.
     * So, it basically returns the same that gets.
     *
     * It seems a nonsense but i use it in case i want to pass
     * a variable list of arguments in an position other than the last one.
     *
     * @param objs
     * @return
     */
    public static <T extends Object> T[] ArrayFrom (T... objs) {
        return objs;
    }


    /**
     * Generates an ID between 0 and Integer.MAX_VALUE.
     * @return the generated ID
     */
    public static int GenerateID () {
        return new Random().nextInt(Integer.MAX_VALUE);
    }


    /**
     * Check if one or all of the given objects are in a collection
     *
     * @param collection The collection were we are searching
     * @param objs A bunch of objects
     * @param checkForAll Indicates if the collection must contain all objects
     *                    or only one.
     */
    public static <T extends Object> boolean InCollection (
            Collection<T> collection,
            T[] objs,
            boolean checkForAll)
    {
        int itemsFound = 0;

        for (T item : objs) {
            if (collection.contains (item)) {
                if (!checkForAll) {
                    return true;
                }

                itemsFound ++;
            }
        }

        return (itemsFound == objs.length);
    }


    public static String StringFrom (Object[] objs) {
        return StringFrom(objs, " ");
    }


    /**
     * Creates a String appending multiple objects
     *
     * @param objs Objects to create the String from
     * @param separator Separator used to separate items (yes, pretty obvious)
     *
     * @return A string with all objects
     */
    public static String StringFrom (Object[] objs, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        int items = objs.length;

        for (int i = 0; i < items; i ++) {
            int preAddingLength = stringBuilder.length();

            stringBuilder.append(objs[i]);

            if ((stringBuilder.length() > preAddingLength) &&
                    (i != (items - 1)))
            {
                stringBuilder.append (separator);
            }
        }

        return stringBuilder.toString();
    }
}
