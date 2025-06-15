package userPCG;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    //Prywatny konstruktor tzw. singleton
    private UserSession() {}

    //Tworzenie instancji UserSession, sprawdza czy instancja nie istnieje
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    //Logowanie jako gość
    public void loginAsGuest() {
        User guest = new User();
        guest.setUsername("guest");
        guest.setRoleId(3); // 3 = rola gościa
        loginUser(guest);
    }

    //Logowanie i Wylogowywanie
    public void loginUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    //Pobieranie obecnie zalogowanego użytkownika
    public User getCurrentUser() {
        return currentUser;
    }
}
