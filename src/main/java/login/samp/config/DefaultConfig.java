package login.samp.config;

import lombok.Getter;
import net.gtaun.shoebill.util.config.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public class DefaultConfig {

    private static final String DEFAULT_CONFIG_PATH = "./shoebill/config.yml";

    @Getter
    private static final DefaultConfig instance = new DefaultConfig();

    public YamlConfiguration config;

    private String mysqlUser;
    private String mysqlPassword;
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;

    private String emailServer;
    private int emailPort;
    private String emailUser;
    private String emailPassword;
    private String emailEmail;
    private String emailName;

    public void load() throws IOException {
        config = new YamlConfiguration();
        config.read(Files.newInputStream(Paths.get(DEFAULT_CONFIG_PATH)));

        mysqlUser = config.getString("mysql.user");
        mysqlPassword = config.getString("mysql.password");
        mysqlHost = config.getString("mysql.host");
        mysqlPort = config.getInt("mysql.port");
        mysqlDatabase = config.getString("mysql.database");

        emailServer = config.getString("email.server");
        emailPort = config.getInt("email.port");
        emailUser = config.getString("email.user");
        emailPassword = config.getString("email.password");
        emailEmail = config.getString("email.email");
        emailName = config.getString("email.name");
    }
}
