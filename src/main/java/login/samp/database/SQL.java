package login.samp.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import login.samp.Main;
import login.samp.config.DefaultConfig;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class SQL {

    @Getter
    private static final SQL instance = new SQL();

    private HikariDataSource dataSource;

    public boolean tables() {
        try(Connection connection = dataSource.getConnection();
                PreparedStatement economy = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata " +
                        "(`id` BIGINT NOT NULL AUTO_INCREMENT, `name` VARCHAR(255), `email` TEXT, `password` LONGTEXT, " +
                        "`gender` VARCHAR(32), `lastPosition` LONGTEXT, PRIMARY KEY (`id`));");) {
            economy.execute();
            Main.getInstance().getLogger().info("A tabela 'playerda foi carregada com sucesso.");
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            Main.getInstance().getLogger().error("Houve um erro ao tentar carregar as tabelas no MySQL.");

            //Bukkit.getPluginManager().disablePlugin(Main.getInstance());
            return false;
        }
    }

    public boolean connect(){

        String type = "jdbc:mysql://";
        String url = type + DefaultConfig.getInstance().getMysqlHost() + ":" +
                DefaultConfig.getInstance().getMysqlPort() + "/" +
                DefaultConfig.getInstance().getMysqlDatabase();

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(url);
        config.setMaxLifetime(86400000L);
        config.setMaximumPoolSize(30);

        config.setUsername(DefaultConfig.getInstance().getMysqlUser());
        config.setPassword(DefaultConfig.getInstance().getMysqlPassword());

        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("charsetEncoding", "utf-8");
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "275");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        try{
            dataSource = new HikariDataSource(config);
            Main.getInstance().getLogger().info("Conexão com o MySQL estabelecida com sucesso.");
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            Main.getInstance().getLogger().error("Houve um erro ao tentar se conectar ao MySQL no host: '"
                    + DefaultConfig.getInstance().getMysqlHost() + ":" + DefaultConfig.getInstance().getMysqlPort() + "'.");
            //Bukkit.getPluginManager().disablePlugin(Main.getInstance());
            return false;
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection createConnection() throws SQLException {
        return instance.getConnection();
    }

}
