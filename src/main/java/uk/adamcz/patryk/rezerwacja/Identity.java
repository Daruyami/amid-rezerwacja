package uk.adamcz.patryk.rezerwacja;

public class Identity {
    public boolean LoggedIn = false;

    public UserRole Role;

    public String Username;

    Identity(){}

    Identity(boolean loggedIn, String role, String username){
        if (loggedIn) {
            LoggedIn = loggedIn;
            Role = UserRole.valueOf(role);
            Username = username;
        }
    }
}
