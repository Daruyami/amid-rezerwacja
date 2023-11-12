package uk.adamcz.patryk.rezerwacja;

public class StringInputUtils {
    public static String Escape(String login){
        String preparedLogin = login.replaceAll("[\\r\\n]", "");
        if (preparedLogin.length() > 256){
            preparedLogin = preparedLogin.substring(0, 255);
        }
        return preparedLogin;
    }
}
