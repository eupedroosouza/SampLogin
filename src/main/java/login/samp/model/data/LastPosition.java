package login.samp.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.entities.Player;

@Getter
@AllArgsConstructor
public class LastPosition  {

    private final float x;
    private final float y;
    private final float z;
    private final float angle;

    public LastPosition(Player player){
        this.x = player.getLocation().x;
        this.y = player.getLocation().y;
        this.z = player.getLocation().z;
        this.angle = player.getAngle();
    }

    public String serialize(){
        return x + ";" + y + ";" + z + ";" + angle;
    }

    public AngledLocation getLocation(){
        return new AngledLocation(x, y, z, angle);
    }

    public static String serialize(Player player){
        return new LastPosition(player).serialize();
    }

    public static LastPosition getPlayer(Player player){
        return new LastPosition(player.getLocation().x, player.getLocation().y, player.getLocation().z,
                player.getLocation().angle);
    }

    public static LastPosition unserialize(String string){
        if(string == null)
            return null;

        String[] split = string.split(";");
        if(split.length != 4)
            return null;

        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        float z = Float.parseFloat(split[2]);
        float angle = Float.parseFloat(split[3]);

        return new LastPosition(x, y, z, angle);
    }

}
