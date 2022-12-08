package login.samp;

import login.samp.commands.PlayerCommands;
import login.samp.config.DefaultConfig;
import login.samp.database.SQL;
import login.samp.listeners.DialogLoginListener;
import login.samp.listeners.PlayerDisconnectListener;
import login.samp.listeners.PlayerLoginListener;
import lombok.Getter;
import net.gtaun.shoebill.ShoebillMain;
import net.gtaun.shoebill.common.command.PlayerCommandManager;
import net.gtaun.shoebill.entities.Server;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.shoebill.event.dialog.DialogShowEvent;
import net.gtaun.shoebill.event.player.PlayerDisconnectEvent;
import net.gtaun.shoebill.event.player.PlayerRequestClassEvent;
import net.gtaun.shoebill.resource.Gamemode;
import net.gtaun.util.event.EventManager;
import net.gtaun.util.event.HandlerPriority;

@ShoebillMain(name = "sarp-gamemode", version = "1.0-SNAPSHOT", author = "eupedroosouza")
public class Main extends Gamemode {

    @Getter
    private static Main instance;

    @Override
    protected void onEnable() throws Throwable {
        instance = this;

        DefaultConfig.getInstance().load();



        if(!SQL.getInstance().connect()){
            disable();
            return;
        }
        SQL.getInstance().tables();

        this.registerEvents();
        this.registerCommands();

        getLogger().info("SampLogin habilitado com sucesso.");
    }

    @Override
    protected void onDisable() throws Throwable {
        getLogger().info("SampLogin desabilitado com sucesso.");
    }

    private void registerEvents(){
        EventManager manager = getEventManager();
        manager.registerHandler(PlayerRequestClassEvent.class, new PlayerLoginListener());
        manager.registerHandler(DialogResponseEvent.class, new DialogLoginListener());
        manager.registerHandler(PlayerDisconnectEvent.class, new PlayerDisconnectListener());
    }

    private void registerCommands(){
        /*PlayerCommandManager manager = new PlayerCommandManager(getEventManager());
        manager.installCommandHandler(HandlerPriority.NORMAL);
        manager.registerCommands(new PlayerCommands());*/
    }


}
