package login.samp.listeners;

import login.samp.controller.ConfirmationController;
import login.samp.controller.PlayerController;
import login.samp.model.ConfirmationCode;
import login.samp.model.Dialog;
import login.samp.model.data.Gender;
import login.samp.model.PlayerData;
import login.samp.repository.PlayerRepository;
import login.samp.utils.CriptyUtils;
import net.gtaun.shoebill.constant.DialogStyle;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.entities.Player;
import net.gtaun.shoebill.event.dialog.DialogResponseEvent;
import net.gtaun.util.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DialogLoginListener implements EventHandler<DialogResponseEvent> {

    public static final int max_attempts = 3;

    public static HashMap<String, Integer> attempts = new HashMap<>();

    @Override
    public void handleEvent(@NotNull DialogResponseEvent event) throws Throwable {
        Player player = event.getPlayer();
        if(event.getDialog() == Dialog.REGISTER_DIALOG.getDialogId()){
            if(event.getDialogResponse() == 0) {
                player.kick();
                return;
            }
            String password = event.getInputText();
            if(password.length() < 5){
                Dialog.REGISTER_DIALOG.show("{FF0000}* Sua senha deve ter mais de 5 caracteres.", player, player.getName());
                return;
            }

            if(password.length() > 16){
                Dialog.REGISTER_DIALOG.show("{FF0000}* Sua senha não deve ultrapassar os 16 caracteres.", player, player.getName());
                return;
            }

            PlayerData pd = PlayerController.getController().get(player.getName());
            if(pd == null){
                player.kick();
                return;
            }
            pd.setPassword(CriptyUtils.hashSha256(password));
            CompletableFuture.runAsync(() -> PlayerRepository.getRepository().insert(pd));

            Dialog.EMAIL_DIALOG.show(player);
        }else if(event.getDialog() == Dialog.LOGIN_DIALOG.getDialogId()){
            if(event.getDialogResponse() == 0) {
                player.kick();
                return;
            }
            String password = event.getInputText();

            PlayerData pd = PlayerController.getController().get(player.getName());
            if(pd == null){
                player.kick();
                return;
            }

            if(pd.getPassword().equals(CriptyUtils.hashSha256(password))){
                player.sendMessage(Color.GREEN, "[AUTENTICAÇÃO]: Você foi autenticado com sucesso.");
                if(!pd.hasEmail()){
                    Dialog.EMAIL_DIALOG.show(player);
                    return;
                }

                pd.login();

                if(pd.getLastPosition() != null)
                    player.setSpawnInfo(pd.getLastPosition().getLocation());

                player.spawn();
            }else {
                int nowAttempts = attempts.get(player.getName().toLowerCase()) - 1;
                if(nowAttempts == 0){
                    player.kick();
                    return;
                }
                attempts.put(player.getName().toLowerCase(), nowAttempts);
                Dialog.LOGIN_DIALOG.show("{FF0000}Senha incorreta, você tem mais "
                        + (nowAttempts == 1 ? "1 tentativa" : nowAttempts + " tentativas") + ".", player, player.getName());
            }
        }else if(event.getDialog() == Dialog.EMAIL_DIALOG.getDialogId()){
            if(event.getDialogResponse() == 0) {
                player.kick();
                return;
            }

            String email = event.getInputText();
            /*if(!EmailUtils.isValidEmail(email)){
                player.showDialog(Dialog.EMAIL_DIALOG.getDialogId(), DialogStyle.INPUT, Dialog.EMAIL_DIALOG.getTitle(),
                        String.format(Dialog.EMAIL_DIALOG.getText(), player.getName()) + "\n{FF0000}E-mail inválido, tente novamente.",
                        Dialog.EMAIL_DIALOG.getButton1(), Dialog.EMAIL_DIALOG.getButton2());
                return;
            }*/

            ConfirmationCode confirmationCode = new ConfirmationCode(player.getName(), email);
            ConfirmationController.getController().addConfirmation(confirmationCode);
            CompletableFuture.runAsync(() -> ConfirmationController.getController().sendEmail(confirmationCode));

            Dialog.CODE_DIALOG.show(player, email);
        }else if(event.getDialog() == Dialog.CODE_DIALOG.getDialogId()){
            if(event.getDialogResponse() == 0) {
                player.kick();
                return;
            }

            String code = event.getInputText();
            ConfirmationCode confirmationCode = ConfirmationController.getController().get(player.getName());
            if(confirmationCode == null){
                player.kick();
                return;
            }

            PlayerData pd = PlayerController.getController().get(player.getName());
            if(pd == null){
                player.kick();
                return;
            }

            if(confirmationCode.getCode().equals(code)){
                pd.setEmail(confirmationCode.getEmail());
                CompletableFuture.runAsync(() -> PlayerRepository.getRepository().update(player.getName(),
                        "email", confirmationCode.getEmail()));

                if(!pd.hasGender()){
                    Dialog.GENDER_DIALOG.show(player);
                    return;
                }

                //player.sendMessage(Color.GREEN, "[AUTENTICAÇÃO]: Você foi autenticado com sucesso.");
                pd.login();

                if(pd.getLastPosition() != null)
                    player.setSpawnInfo(pd.getLastPosition().getLocation());

                player.spawn();

            }else Dialog.CODE_DIALOG.show("{FF0000}* Código incorreto, tente novamente.", player, pd.getEmail());
        }else if(event.getDialog() == Dialog.GENDER_DIALOG.getDialogId()){
            PlayerData pd = PlayerController.getController().get(player.getName());
            if(pd == null){
                player.kick();
                return;
            }

            if(event.getDialogResponse() == 0)
                pd.setGender(Gender.FEMALE);
            else pd.setGender(Gender.MALE);

            CompletableFuture.runAsync(() -> PlayerRepository.getRepository().update(player.getName(),
                    "gender", pd.getGender().name()));

            pd.login();

            if(pd.getLastPosition() != null)
                player.setSpawnInfo(pd.getLastPosition().getLocation());

            player.spawn();

            player.setSkin(pd.getGender().getDefaultSkinId());
        }
    }

}
