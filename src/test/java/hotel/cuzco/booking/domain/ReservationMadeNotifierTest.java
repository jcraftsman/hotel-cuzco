package hotel.cuzco.booking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ReservationMadeNotifierTest {


    private static final LocalDate JAN_01_19 = LocalDate.parse("2019-01-01");
    private static final LocalDate JAN_02_19 = LocalDate.parse("2019-01-02");
    private MailSender mailSender;
    private ReservationMadeNotifier reservationMadeNotifier;

    @BeforeEach
    void setUp() {
        mailSender = mock(MailSender.class);
        reservationMadeNotifier = new ReservationMadeNotifier(mailSender);
    }

    @Test
    void it_should_send_a_mail_to_main_contact_with_the_made_reservation_information() {
        // Given
        ReservationId reservationId = new ReservationId();
        String email = "valentina.xue@walmail.com";
        MainContact valentina_xue = new MainContact("Valentina Xue", email);
        ReservationMade reservationMade = ReservationMade.builder()
                .reservationId(reservationId)
                .checkIn(JAN_01_19)
                .checkOut(JAN_02_19)
                .mainContact(valentina_xue)
                .build();

        // When
        reservationMadeNotifier.handle(reservationMade);

        // Then
        then(mailSender).should().send(
                email,
                "Welcome to Hotel Cuzco",
                "Dear Valentina Xue, \n" +
                        "Thank you very much for choosing Hotel Cuzco for your stay in Cusco city (from 2019-01-01 to 2019-01-02).\n" +
                        "Your reservation number is " + reservationId + " .\n" +
                        "Hotel Cuzco staff wish you a wonderful stay in our hotel.\n" +
                        "Best regards!\n" +
                        "Jose\n" +
                        "Recepcion");
    }
}