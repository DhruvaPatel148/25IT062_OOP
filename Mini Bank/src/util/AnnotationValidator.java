package util;

import model.annotation.MaxLength;
import model.annotation.Positive;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnnotationValidator {
    /** Reads @Positive and @MaxLength by reflection and returns the error messages. */
    public static String[] validate(Object obj) {
        ArrayList<String> errors = new ArrayList<>();
        try {
            // walk up the class chain so inherited fields (e.g. Account's) are checked too
            for (Class<?> c = obj.getClass(); c != null; c = c.getSuperclass()) {
                for (Field field : c.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (field.isAnnotationPresent(Positive.class) && value instanceof Number n && n.longValue() <= 0) {
                        errors.add(field.getAnnotation(Positive.class).message());
                    }
                    if (field.isAnnotationPresent(MaxLength.class) && value instanceof String s
                            && s.length() > field.getAnnotation(MaxLength.class).value()) {
                        errors.add(field.getName() + " is longer than " + field.getAnnotation(MaxLength.class).value());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            errors.add("Could not read a field: " + e.getMessage());
        }
        return errors.toArray(new String[0]);
    }
}
