package hotel.cuzco.booking.domain;

import lombok.Data;

@Data
public class MainContact {
    private final String fullName;
    private final String email;

    public MainContact(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
}
