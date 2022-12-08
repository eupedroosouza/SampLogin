package login.samp.listeners;
;
import login.samp.controller.ConfirmationController;
import login.samp.controller.PlayerController;
import login.samp.model.data.LastPosition;
import login.samp.model.PlayerData;
import login.samp.repository.PlayerRepository;
import net.gtaun.shoebill.entities.Player;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.util.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class PlayerDisconnectListener implements EventHandler<PlayerDisconnectEvent> {


    @Override
    public void handleEvent(@NotNull PlayerDisconnectEvent event) throws Throwable {
        Player player = event.getPlayer();

        PlayerData pd = PlayerController.getController().get(player.getName());
        if(pd != null){
            pd.setLastPosition(LastPosition.getPlayer(player));
            PlayerRepository.getRepository().save(pd);
            PlayerController.getController().removePlayer(pd.getName());
        }

        ConfirmationController.getController().removeConfirmation(player.getName());
    }
}
