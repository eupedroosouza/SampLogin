package login.samp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@" +
            "[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email){
        return email.matches(regex);
    }


}
