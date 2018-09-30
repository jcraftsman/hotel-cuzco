# Hotel Cuzco

"Hotel Cuzco" is a charming hotel in Cusco city, Peru.
It has been successful in the last years, so the administration decided to build a simple booking system and get rid of the excel sheet they were maintaining for reservations.

There are 12 rooms in the hotel:

| Floor | Room | Description                                                                | Capacity |
|:-----:|:----:|----------------------------------------------------------------------------|:--------:|
|   1   | 101  | 1 king size bed - A/C - Wi-Fi - private bathroom - wheelchair accessible   | 2 guests |
|   1   | 102  | 2 queen size beds - A/C - Wi-Fi - private bathroom - wheelchair accessible | 4 guests |
|   1   | 103  | 3 single beds - A/C - Wi-Fi - private bathroom - wheelchair accessible     | 3 guests |
|   2   | 201  | 1 king size bed - A/C - Wi-Fi - private bathroom                           | 2 guests |
|   2   | 202  | 1 queen size bed - Wi-Fi - private bathroom                                | 2 guests |
|   2   | 203  | 1 king size bed + 3 single beds - A/C - Wi-Fi - private bathroom           | 5 guests |
|   2   | 204  | 1 single bed - Wi-Fi - shared bathroom                                     | 1 guest  |
|   2   | 205  | 2 single beds - A/C - Wi-Fi - shared bathroom                              | 2 guests |
|   3   | 301  | 1 queen size bed - A/C - private bathroom                                  | 2 guests |
|   3   | 302  | 2 single beds - A/C - private bathroom                                     | 2 guests |
|   3   | 303  | 3 single beds - A/C - shared bathroom                                      | 3 guests |
|   3   | 304  | 2 single beds - shared bathroom                                            | 2 guests |

## Booking

The booking system should offer 2 main features:

    1. Find available rooms:
        - The user enters the check-in / check-out dates and the number of guests
        - The system displays all the available rooms that match the user's request 
        - The minimum stay is 1 night
    2. Make a reservation: 
        - The user enters the check-in / check-out dates, the number of guests and the room number
        - The system saves the reservation
        - The room is now considered as unavailable for booking during the period of reservation  

## Annual closing periods and planned interventions

The hotel closes 2 weeks per year for annual holidays but also for renovation.

It is usually around the Pachacamilla holidays (October, 18th to 28th).

Sometimes the hotel plans an intervention to fix something broken in a room. The room cannot be booked at this date.

## Pricing

The rate depends on the type of room and the season.

### Discounts

Discounts can apply to:

- reservations made by returning customers
- reservations made by some third party partners

## Billing

During his stay, a customer can ask for some services (laundry, extra meal, breakfast, tour... etc.) that will be added to his bill.

It is not possible to list all the extras that is possible to charge. The system needs to be flexible about this.
