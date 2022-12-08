package login.samp.utils;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateUtils {

    private static final String[] CONTENT = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6"};

    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    public static String generate(int length){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++)
            sb.append(CONTENT[rand.nextInt(CONTENT.length)]);
        return sb.toString();
    }

}
