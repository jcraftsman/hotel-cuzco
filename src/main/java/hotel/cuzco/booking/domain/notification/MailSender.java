package hotel.cuzco.booking.domain.notification;

public interface MailSender {
    void send(String email, String subject, String body);
}
