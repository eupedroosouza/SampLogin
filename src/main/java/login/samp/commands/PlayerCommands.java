package login.samp.commands;

import net.gtaun.shoebill.Shoebill;
import net.gtaun.shoebill.common.command.Command;
import net.gtaun.shoebill.constant.SpecialAction;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.entities.Player;
import net.gtaun.shoebill.entities.Vehicle;

/**
 * Used on tests.
 */
public class PlayerCommands {

    @Command(name = "moto")
    public boolean moto(Player player){
        AngledLocation location = new AngledLocation(player.getLocation().x, player.getLocation().y,
                player.getLocation().z, player.getAngle());
        Vehicle vehicle = Shoebill.get().getSampObjectManager().createVehicle(522, location,
                1, 0, -1, false);
        player.setVehicle(vehicle);
        player.sendMessage(Color.GREEN, "[MOTO] Sua moto foi spawnada.");
        return true;
    }

    @Command(name = "jetpack")
    public boolean jetpack(Player player){
        player.setSpecialAction(SpecialAction.USE_JETPACK);
        player.sendMessage(Color.GREEN, "[JETPACK] Seu jetpack foi spawnado.");
        return true;
    }
}
