import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    String name = "";

    @MaxLength(10)
    String username = "DhruvaPatel123";
}

public class FormValidator {
    public static List<String> check(Object obj) throws Exception {
        List<String> errors = new ArrayList<>();

        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String value = (String) f.get(obj);

            if (f.isAnnotationPresent(NotBlank.class)
                    && value.trim().isEmpty())
                errors.add(f.getName() + " cannot be blank");

            if (f.isAnnotationPresent(MaxLength.class)
                    && value.length() > f.getAnnotation(MaxLength.class).value())
                errors.add(f.getName() + " is too long");
        }

        return errors;
    }

    public static void main(String[] args) throws Exception {
        SignupForm form = new SignupForm();

        for (String error : check(form))
            System.out.println(error);
    }
}