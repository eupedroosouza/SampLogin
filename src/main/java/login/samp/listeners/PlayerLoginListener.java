package login.samp.listeners;

import login.samp.controller.PlayerController;
import login.samp.model.Dialog;
import login.samp.model.PlayerData;
import login.samp.repository.PlayerRepository;
import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.entities.Player;
import net.gtaun.shoebill.event.player.PlayerRequestClassEvent;
import net.gtaun.util.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PlayerLoginListener implements EventHandler<PlayerRequestClassEvent> {
    @Override
    public void handleEvent(@NotNull PlayerRequestClassEvent event) throws Throwable {
        Player player = event.getPlayer();
        for(int i = 0; i < 20; i++)
            player.sendMessage(Color.WHITE, "");

        //TODO: login location/organize
        player.setLocation(375.8277F, -2057.7202F, 10.7F);
        player.setAngle(172.0632F);

        player.setCameraPosition(376.8F, -2060.4F, 10.9F);
        player.setCameraLookAt(376.8F, -2057.7202F, 10.9F);


        //TODO: show dialogs
        CompletableFuture<PlayerData> future = CompletableFuture.supplyAsync(() ->
                PlayerRepository.getRepository().load(player.getName()));
        future.thenAccept(pd -> {
            if(pd == null){
                PlayerController.getController().addPlayer(new PlayerData(player.getName()));
                Dialog.REGISTER_DIALOG.show(player, player.getName());
                return;
            }


            PlayerController.getController().addPlayer(pd);
            Dialog.LOGIN_DIALOG.show(player, player.getName());
            DialogLoginListener.attempts.put(player.getName().toLowerCase(), DialogLoginListener.max_attempts);
        });

    }
}
