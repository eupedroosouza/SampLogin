package login.samp.controller;

import login.samp.config.DefaultConfig;
import login.samp.model.ConfirmationCode;
import lombok.Getter;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

@Getter
public class ConfirmationController {

    @Getter
    private static final ConfirmationController controller = new ConfirmationController();

    private final HashMap<String, ConfirmationCode> confirmations = new HashMap<>();

    public void addConfirmation(ConfirmationCode confirmation){
        confirmations.put(confirmation.getName().toLowerCase(), confirmation);
    }

    public void removeConfirmation(String name){
        confirmations.remove(name.toLowerCase());
    }

    public ConfirmationCode get(String name){
        return confirmations.get(name.toLowerCase());
    }

    public void sendEmail(ConfirmationCode confirmation)  {

                Properties props = new Properties();
        //props.put("mail.debug", "true");

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", DefaultConfig.getInstance().getEmailServer());
        props.put("mail.smtp.port", String.valueOf(DefaultConfig.getInstance().getEmailPort()));

        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(DefaultConfig.getInstance().getEmailUser(), DefaultConfig.getInstance().getEmailPassword());
            }
        });


        //session.setDebug(false);

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(DefaultConfig.getInstance().getEmailEmail(), DefaultConfig.getInstance().getEmailName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(confirmation.getEmail()));
            message.setSubject("Confirme seu e-mail no SampLogin.");
            message.setText("O seu código de confirmação é: \n\n" + confirmation.getCode() +
                    ".\n\n* Se você não se cadastrou em SampLogin, ignore este e-mail.");

            Transport.send(message);
            //System.out.println(message + ": enviado.");
        }catch(MessagingException | UnsupportedEncodingException ex){
            ex.printStackTrace();
        }

    }

}
