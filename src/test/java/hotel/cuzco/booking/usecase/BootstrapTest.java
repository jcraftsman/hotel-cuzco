package hotel.cuzco.booking.usecase;

import hotel.cuzco.booking.domain.reservation.Hotel;
import hotel.cuzco.booking.domain.reservation.RoomRepository;
import hotel.cuzco.booking.infrastructure.database.inmemory.RoomInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class BootstrapTest {

    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        roomRepository = RoomInMemoryRepository.build();
    }

    @Test
    void it_should_setup_all_rooms_for_hotel_cuzco() {
        // When
        Bootstrap.setupCuzcoRooms(roomRepository);

        // Then
        assertThat(roomRepository.all()).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(Hotel.CUZCO().allRooms());
    }

    @Test
    void it_should_not_save_any_room_when_repository_is_not_empty() {
        // Given
        RoomRepository roomRepository = mock(RoomRepository.class);
        given(roomRepository.isEmpty()).willReturn(false);

        // When
        Bootstrap.setupCuzcoRooms(roomRepository);

        // Then
        then(roomRepository).should(never()).save(any());
    }
}