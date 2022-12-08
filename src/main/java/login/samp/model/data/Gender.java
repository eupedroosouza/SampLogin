package login.samp.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    FEMALE(226), MALE(306);

    final int defaultSkinId;

    public static Gender get(String string){
        if(string == null)
            return null;
        return Gender.valueOf(string);
    }

}
