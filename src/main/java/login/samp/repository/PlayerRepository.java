package login.samp.repository;

import login.samp.database.SQL;
import login.samp.model.data.Gender;
import login.samp.model.data.LastPosition;
import login.samp.model.PlayerData;
import lombok.Getter;

import java.sql.*;

public class PlayerRepository {

    @Getter
    private static final PlayerRepository repository = new PlayerRepository();

    public PlayerData load(Connection connection, String name){
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM `playerdata` WHERE LOWER(`name`) = '" + name.toLowerCase() + "';");
            ResultSet result = statement.executeQuery()){

            if(!result.next())
                return null;

            long id = result.getLong("id");
            String email = result.getString("email");
            String password = result.getString("password");

            Gender gender = Gender.get(result.getString("gender"));
            LastPosition lastPosition = LastPosition.unserialize(result.getString("lastPosition"));

            return new PlayerData(id, name, email, password, gender, lastPosition, false);
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public PlayerData load(String name){
        try(Connection connection = SQL.getInstance().getConnection()){
            return load(connection, name);
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public PlayerData insert(Connection connection, PlayerData playerData){
        if(playerData == null)
            return null;
        try(PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO `playerdata`(`name`, `email`, `password`, `gender`, `lastPosition`) " +
                        "VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, playerData.getName());
            statement.setString(2, playerData.getEmail());
            statement.setString(3, playerData.getPassword());
            statement.setString(4, playerData.getGender() != null ?
                    playerData.getGender().name() :  null);
            statement.setString(5, playerData.getLastPosition() != null ?
                    playerData.getLastPosition().serialize() : null);


            int success = statement.executeUpdate();
            if(success == 0)
                return null;

            try(ResultSet idResult = statement.getGeneratedKeys()){
                if(idResult.next())
                    playerData.setId(idResult.getLong(1));
                return playerData;
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public PlayerData insert(PlayerData playerData){
        try(Connection connection = SQL.getInstance().getConnection()){
            return insert(connection, playerData);
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public PlayerData save(Connection connection, PlayerData playerData){
        try(PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET `email` = ?, " +
                "`password` = ?, `gender` = ?, `lastPosition` = ? WHERE LOWER(`name`) = '"
                + playerData.getName().toLowerCase() + "';")){
            statement.setString(1, playerData.getEmail());
            statement.setString(2, playerData.getPassword());
            statement.setString(3, playerData.getGender() != null ?
                    playerData.getGender().name() :  null);
            statement.setString(4, playerData.getLastPosition() != null ?
                    playerData.getLastPosition().serialize() : null);

            statement.execute();
            return playerData;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public PlayerData save(PlayerData playerData){
        try(Connection connection = SQL.getInstance().getConnection()){
            return save(connection, playerData);
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public <T> boolean update(Connection connection, String name, String column, T value){
        try(PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET `" + column + "` = ? " +
                "WHERE LOWER(`name`) = '" + name.toLowerCase() + "';")){
            statement.setObject(1, value);
            return statement.executeUpdate() >= 1;
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

    }

    public <T> boolean update(String name, String column, T value){
        try(Connection connection = SQL.getInstance().getConnection()){
            return update(connection, name, column, value);
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }
}
