package hotel.cuzco.booking.infrastructure.mailing;

import com.sun.mail.smtp.SMTPTransport;
import hotel.cuzco.booking.domain.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class MailGunSender implements MailSender {

    private static final String SMTPS_PROTOCOL = "smtps";
    private static final String SMTP_HOST = "smtp.mailgun.com";
    private static final String SMTP_USER_ENV_VAR_NAME = "SMTP_USER";
    private static final String SMTP_PASSWORD_NEV_VAR_NAME = "SMTP_PASSWORD";
    private static final String SENDER_EMAIL_ENV_VAR_NAME = "SENDER_EMAIL";

    private final String smtpUser;
    private final String smtpPassword;
    private final String senderEmail;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailGunSender.class);

    public MailGunSender(String smtpUser, String smtpPassword, String senderEmail) {
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
        this.senderEmail = senderEmail;
    }

    @Override
    public void send(String email, String subject, String body) {
        Session session = createSmtpSession();
        try {
            var message = buildMessage(email, subject, body, session);

            var smtpTransport = (SMTPTransport) session.getTransport(SMTPS_PROTOCOL);
            smtpTransport.connect(SMTP_HOST, smtpUser, smtpPassword);
            smtpTransport.sendMessage(message, message.getAllRecipients());

            LOGGER.info("Mail sent to " +
                    email +
                    " | SMTP server responded with: " + smtpTransport.getLastServerResponse());

            smtpTransport.close();
        } catch (MessagingException e) {
            LOGGER.error("Couldn't send the email :(", e);
        }
    }

    private Message buildMessage(String email, String subject, String body, Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        InternetAddress[] addresses = InternetAddress.parse(email, false);
        message.setRecipients(Message.RecipientType.TO, addresses);
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(new Date());
        return message;
    }

    private Session createSmtpSession() {
        Properties props = System.getProperties();
        props.put("mail.smtps.host", SMTP_HOST);
        props.put("mail.smtps.auth", "true");
        return Session.getInstance(props, null);
    }

    public static MailGunSender build() {
        Map<String, String> environmentVariables = System.getenv();
        if (!(environmentVariables.containsKey(SMTP_USER_ENV_VAR_NAME) &&
                environmentVariables.containsKey(SMTP_PASSWORD_NEV_VAR_NAME) &&
                environmentVariables.containsKey(SENDER_EMAIL_ENV_VAR_NAME))) {
            LOGGER.error("Missing environment variables for mailing service. " +
                    "You should set the following environment variables:" +
                    " \"SMTP_USER\" \"SMTP_PASSWORD\" \"SENDER_EMAIL\"");
        }
        return new MailGunSender(
                environmentVariables.get(SMTP_USER_ENV_VAR_NAME),
                environmentVariables.get(SMTP_PASSWORD_NEV_VAR_NAME),
                environmentVariables.get(SENDER_EMAIL_ENV_VAR_NAME));
    }
}
