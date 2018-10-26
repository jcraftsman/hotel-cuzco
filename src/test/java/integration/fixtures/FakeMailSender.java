package integration.fixtures;

import hotel.cuzco.booking.domain.MailSender;

public class FakeMailSender implements MailSender {
    @Override
    public void send(String email, String subject, String body) {

    }
}
