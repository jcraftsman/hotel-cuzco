package integration.mailing;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import hotel.cuzco.booking.infrastructure.mailing.SmtpMailSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;

import static org.assertj.core.api.Assertions.assertThat;

class SmtpMailSenderTest {
    private static final String SENDER_EMAIL = "booking@hotel-cuzco.fr";
    private static final String MAIL_TRANSFER_PROTOCOL = "smtps";
    private static final String SMTP_HOST = "localhost";
    private static final int SMTPS_PORT = ServerSetupTest.SMTPS.getPort();
    private static final String SMTP_USER = "cooladmin";
    private static final String SMTP_PASSWORD = "abcdef123";
    private static final String EMAIL_USER_ADDRESS = "cooladmin@localhost";
    private static final String EMAIL_TO = "someone@somewhere.com";
    private static final String EMAIL_SUBJECT = "Test E-Mail";
    private static final String EMAIL_TEXT = "This is a test e-mail.";

    private GreenMail mailServer;
    private TestableSmtpMailSender smtpMailSender;

    @BeforeEach
    void setUp() {
        Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
        mailServer = new GreenMail(ServerSetupTest.SMTPS);
        mailServer.start();
        smtpMailSender = new TestableSmtpMailSender(MAIL_TRANSFER_PROTOCOL, SMTP_HOST, SMTPS_PORT, SMTP_USER, SMTP_PASSWORD, SENDER_EMAIL);
    }

    @AfterEach
    void tearDown() {
        mailServer.stop();
    }

    @Test
    void it_should_send_the_email() throws IOException, MessagingException {
        // Given
        mailServer.setUser(EMAIL_USER_ADDRESS, SMTP_USER, SMTP_PASSWORD);

        // When
        smtpMailSender.send(EMAIL_TO, EMAIL_SUBJECT, EMAIL_TEXT);

        // Then
        var messages = mailServer.getReceivedMessages();
        assertThat(messages).hasSize(1);
        var sentMessage = messages[0];
        assertThat(sentMessage.getSubject()).isEqualTo(EMAIL_SUBJECT);
        assertThat(contentOf(sentMessage)).isEqualToIgnoringNewLines(EMAIL_TEXT);
        assertThat(sentMessage.getFrom()[0].toString()).isEqualTo(SENDER_EMAIL);

    }

    private String contentOf(MimeMessage sentMessage) throws IOException, MessagingException {
        return String.valueOf(sentMessage.getContent());
    }

    class TestableSmtpMailSender extends SmtpMailSender {

        TestableSmtpMailSender(String mailTransferProtocol,
                               String smtpHost,
                               int smtpServerPort,
                               String smtpUser,
                               String smtpPassword,
                               String senderEmail) {
            super(mailTransferProtocol, smtpHost, smtpServerPort, smtpUser, smtpPassword, senderEmail);
        }
    }

}