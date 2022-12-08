package login.samp.model;

import lombok.Getter;
import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.entities.DialogId;
import net.gtaun.shoebill.entities.Player;

@Getter
public enum Dialog {

    REGISTER_DIALOG("Samp Login - Registro",  DialogStyle.PASSWORD,"{FFFFFF}Seja bem-vindo ao servidor {FF6600}%s{FFFFFF}.\n" +
            "\n{FFFFFF}Registrado: {FF0000}não\n" +
            "\n{FFFFFF}Digite uma senha para se registrar:", "Registrar", "Sair"),
    LOGIN_DIALOG("Samp Login - Autenticação", DialogStyle.PASSWORD, "{FFFFFF}Seja bem-vindo ao servidor {FF6600}%s{FFFFFF}.\n" +
            "\n{FFFFFF}Registrado: {40FF00}sim\n" +
            "\n{FFFFFF}Digite uma senha para se autenticar:", "Entrar", "Sair"),
    EMAIL_DIALOG("Samp Login - Email", DialogStyle.INPUT, "{FFFFFF}Digite seu melhor e-mail:", "Continuar", "Sair"),
    CODE_DIALOG("Samp Login - Confirmação", DialogStyle.INPUT,
            "{FFFFFF}Digite o código de confirmação enviado para o e-mail {FF6600}%s{FFFFFF}:",
            "Continuar", "Sair"),
    GENDER_DIALOG("Samp Login - Sexo", DialogStyle.MSGBOX, "{FFFFFF}Escolha o sexo do seu personagem:", "Masculino", "Feminino");

    private final DialogId dialogId;
    private final DialogStyle style;
    private final String title;
    private final String text;
    private final String button1;
    private final String button2;

    Dialog(String title, DialogStyle style, String text, String button1, String button2){
        this.dialogId = DialogId.create();
        this.style = style;

        this.title = title;
        this.text = text;
        this.button1 = button1;
        this.button2 = button2;
    }

    public void show(Player player){
        show(player, (Object) null);
    }

    public void show(Player player, Object... format){
        show(null, player, format);
    }

    public void show(String errorMessage, Player player, Object... format){
        String newText = text + (errorMessage != null ? ("\n" + errorMessage) : "");
        player.showDialog(dialogId, style, title, format != null ? String.format(newText, format) : newText, button1, button2);
    }

}
