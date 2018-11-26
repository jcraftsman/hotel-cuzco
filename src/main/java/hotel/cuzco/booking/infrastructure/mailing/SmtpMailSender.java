package hotel.cuzco.booking.infrastructure.mailing;

import com.sun.mail.smtp.SMTPTransport;
import hotel.cuzco.booking.domain.notification.MailSender;
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

import static java.lang.Integer.parseInt;

public class SmtpMailSender implements MailSender {

    private static final String MAIL_TRANSFER_PROTOCOL_ENV_VAR_NAME = "MAIL_TRANSFER_PROTOCOL";
    private static final String SMTP_ENV_VAR_NAME = "SMTP_HOST";
    private static final String SMTP_SERVER_PORT_ENV_VAR_NAME = "SMTP_PORT";
    private static final String SMTP_USER_ENV_VAR_NAME = "SMTP_USER";
    private static final String SMTP_PASSWORD_NEV_VAR_NAME = "SMTP_PASSWORD";
    private static final String SENDER_EMAIL_ENV_VAR_NAME = "SENDER_EMAIL";
    private static final Logger LOGGER = LoggerFactory.getLogger(SmtpMailSender.class);

    private final String smtpHost;
    private final String smtpUser;
    private final String smtpPassword;
    private final String senderEmail;
    private final int smtpServerPort;
    private final String mailTransferProtocol;

    private SmtpMailSender(String mailTransferProtocol,
                           String smtpHost,
                           int smtpServerPort,
                           String smtpUser,
                           String smtpPassword,
                           String senderEmail) {
        this.mailTransferProtocol = mailTransferProtocol;
        this.smtpHost = smtpHost;
        this.smtpServerPort = smtpServerPort;
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
        this.senderEmail = senderEmail;
    }

    public static SmtpMailSender build() {
        Map<String, String> environmentVariables = System.getenv();
        if (!(environmentVariables.containsKey(SMTP_USER_ENV_VAR_NAME) &&
                environmentVariables.containsKey(SMTP_PASSWORD_NEV_VAR_NAME) &&
                environmentVariables.containsKey(SENDER_EMAIL_ENV_VAR_NAME))) {
            LOGGER.error("Missing environment variables for mailing service. " +
                    "You should set the following environment variables:" +
                    " \"SMTP_USER\" \"SMTP_PASSWORD\" \"SENDER_EMAIL\"");
        }
        return new SmtpMailSender(
                environmentVariables.get(MAIL_TRANSFER_PROTOCOL_ENV_VAR_NAME),
                environmentVariables.get(SMTP_ENV_VAR_NAME),
                parseInt(environmentVariables.get(SMTP_SERVER_PORT_ENV_VAR_NAME)),
                environmentVariables.get(SMTP_USER_ENV_VAR_NAME),
                environmentVariables.get(SMTP_PASSWORD_NEV_VAR_NAME),
                environmentVariables.get(SENDER_EMAIL_ENV_VAR_NAME)
        );
    }

    @Override
    public void send(String email, String subject, String body) {
        Session session = createSmtpSession();
        try {
            var message = buildMessage(email, subject, body, session);
            var smtpTransport = (SMTPTransport) session.getTransport(mailTransferProtocol);
            smtpTransport.connect(smtpHost, smtpServerPort, smtpUser, smtpPassword);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            LOGGER.info(emailSentSuccessMessage(email, smtpTransport));
            smtpTransport.close();
        } catch (MessagingException e) {
            LOGGER.error(sendEmailFailureMessage(email), e);
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
        Properties props = new Properties(System.getProperties());
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtps.host", smtpHost);
        props.put("mail.smtps.auth", "true");
        return Session.getInstance(props, null);
    }

    private String emailSentSuccessMessage(String email, SMTPTransport smtpTransport) {
        return "Mail sent to " +
                email +
                " | SMTP server responded with: " + smtpTransport.getLastServerResponse();
    }

    private String sendEmailFailureMessage(String email) {
        return "Couldn't send the email to " + email + " :(";
    }
}
