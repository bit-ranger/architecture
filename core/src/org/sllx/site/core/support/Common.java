package org.sllx.site.core.support;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sllx on 14-10-15.
 */
public class Common {

    public static String toString(Object object){
        return object == null ? "" : object.toString();
    }

    public static Map<String,String> convert(Object object){
        return Arrays.asList(Reflection.getFields(object)).parallelStream().collect(
                Collectors.toMap(
                        Field::getName,
                        p -> toString(Reflection.getValue(p, object))
                )
        );
    }
}
