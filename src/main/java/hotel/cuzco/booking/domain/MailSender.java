package hotel.cuzco.booking.domain;

public interface MailSender {
    void send(String email, String subject, String body);
}
