package presentation.dto.response;

public class loginResponse {
    public String token;
    public String user;

    public loginResponse(String token, String user) {
        this.token = token;
        this. user = user;
    }
}
