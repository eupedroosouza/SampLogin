package login.samp.model;

import login.samp.model.data.Gender;
import login.samp.model.data.LastPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PlayerData {

    private long id;
    private String name;
    private String email;
    private String password;

    private Gender gender;
    private LastPosition lastPosition;

    private boolean logged = false;

    public void login() {
        logged = true;
    }

    public PlayerData(String name){
        this.name = name;
    }

    public boolean hasEmail() {
        return email != null;
    }

    public boolean hasGender(){
        return gender != null;
    }
}
