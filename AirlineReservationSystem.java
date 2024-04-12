import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AirlineReservationSystem {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Airline Reservation System");

        while (true) {
            System.out.println("1. Book a Flight");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    bookFlight();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void bookFlight() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Fetch available flights
            String query = "SELECT * FROM flights WHERE available_seats > 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Available Flights:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". " + resultSet.getString("flight_number")
                        + " (" + resultSet.getString("source") + " to " + resultSet.getString("destination") + ")"
                        + " - Departure Date: " + resultSet.getString("departure_date")
                        + " - Available Seats: " + resultSet.getInt("available_seats"));
            }

            System.out.print("Enter Flight ID to book: ");
            int flightId = scanner.nextInt();

            // Check if flight is available
            query = "SELECT available_seats FROM flights WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, flightId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int availableSeats = resultSet.getInt("available_seats");
                if (availableSeats > 0) {
                    // Book the flight
                    query = "UPDATE flights SET available_seats = ? WHERE id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, availableSeats - 1);
                    preparedStatement.setInt(2, flightId);
                    preparedStatement.executeUpdate();

                    System.out.println("Flight booked successfully!");
                } else {
                    System.out.println("No available seats for the selected flight.");
                }
            } else {
                System.out.println("Invalid Flight ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cancelReservation() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Fetch reservations
            String query = "SELECT * FROM reservations";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Your Reservations:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + ". Customer ID: " + resultSet.getInt("customer_id")
                        + ", Flight ID: " + resultSet.getInt("flight_id"));
            }

            System.out.print("Enter Reservation ID to cancel: ");
            int reservationId = scanner.nextInt();

            // Delete the reservation
            query = "DELETE FROM reservations WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reservationId);
            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows > 0) {
                // Update available seats for the cancelled flight
                query = "UPDATE flights SET available_seats = available_seats + 1 WHERE id = (SELECT flight_id FROM reservations WHERE id = ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, reservationId);
                preparedStatement.executeUpdate();

                System.out.println("Reservation cancelled successfully!");
            } else {
                System.out.println("Invalid Reservation ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
