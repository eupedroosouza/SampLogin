package login.samp.model;

import login.samp.utils.GenerateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ConfirmationCode {

    private final String name;
    private final String email;
    @Setter private String code;

    public ConfirmationCode(String name, String email){
        this.name = name;
        this.email = email;

        this.code = GenerateUtils.generate(6);
    }

}
