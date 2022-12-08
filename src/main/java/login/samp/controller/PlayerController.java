package login.samp.controller;

import login.samp.model.PlayerData;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class PlayerController {

    @Getter
    private static final PlayerController controller = new PlayerController();

    public final HashMap<String, PlayerData> players = new HashMap<>();

    public void addPlayer(PlayerData playerData){
        players.put(playerData.getName().toLowerCase(), playerData);
    }

    public void removePlayer(String name){
        players.remove(name.toLowerCase());
    }

    public PlayerData get(String name){
        return players.get(name.toLowerCase());
    }
}
