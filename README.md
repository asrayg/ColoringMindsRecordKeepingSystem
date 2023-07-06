# Coloring Minds Record Keeping System

**Author**: Asray Gopa  
**Date Started**: June 10th, 2023  
**Version**: 1.00  

## Description
The Coloring Minds Record Keeping System is a Java-based application designed to manage the operations of an after school art class. This system helps in keeping track of records, attendance, fees, and payments for Coloring Minds. It provides features such as generating emails to students, managing balances, and providing a user-friendly interface for easy interaction. The system utilizes Java Swing for the UI, Maven for the mail system, and various data structures for efficient record management. My mother runs Coloring Minds and uses this system extensively. We are looking at adding extra features into the system in the future: recording attendance from an in-class picture using facial recognition, auto-adding payment information into the system directly using Indian based UPI servers, adding a WhatsApp messenger update feature, and more.

## Files
- `records.txt`: Stores the record details of the art class participants.
- `fees.txt`: Manages the fee-related information, including balances.
- `attendance.txt`: Maintains the attendance records of the students.
- `payment.txt`: Tracks the payment details of the students.
- `password.txt`: Checks if the password is right

## Usage
To start the system, you have two options:

1. UI Mode:
   - Run the `StartupPageUI.java` file located in the `/ColoringMindsRecordKeepingSystem/UI/mainUI/` directory.
   - This will launch the graphical user interface (GUI) for easy navigation.

2. Console Mode:
   - Execute the corresponding `.java` files located in the `/ColoringMindsRecordKeepingSystem/Console/` directory.
   - This allows you to use the system via the command-line console.

## Login System
The system includes a login page to ensure secure access. Here are some details regarding the login system:
- **Password**: A password is required to log in to the system.
- **Password Reset**: You can reset the password if needed.
- **Failed Attempts**: If the password is incorrectly typed five times, the system will be locked for security reasons.

## UI Package Structure
The following folders and files are present in the `/ColoringMindsRecordKeepingSystem/UI` directory:

## - `attendanceUI`:
  - `AttendanceRecordsUI.java`: Displays the attendance records of the art class participants.
  - `AttendanceUI.java`: Provides the main interface for managing attendance.
  - `DeleteAttendanceUI.java`: Allows deletion of attendance records.
  - `ViewAttendanceUI.java`: Enables viewing of attendance records.

## - `emailUI`:
  - `EmailUI.java`: The main interface for managing email functionalities.
  - `GenerateBalanceEmailUI.java`: Generates emails to students regarding their balances.
  - `GenerateEmailUI.java`: Provides options to generate emails.
  - `GenerateSpecificDateEmailUI.java`: Allows generating emails for a specific date.
  - `GenerateSpecificEmailUI.java`: Generates emails to specific students.

## - `feesUI`:
  - `DeleteFeesUI.java`: Enables deletion of fee-related information.
  - `FeesUI.java`: Provides the main interface for managing fees.
  - `GenerateFeesUI.java`: Generates fees-related information.
  - `ViewFeesUI.java`: Allows viewing of fees information.
  - `ViewStudentsThatHaveABalanceUI.java`: Displays students who have a balance.

## - `mainUI`:
  - `CommandUI.java`: The main interface for interacting with the system via UI.
  - `ResetPasswordUI.java`: Provides functionality for resetting the password.
  - `StartupPageUI.java`: The initial page of the UI with login functionality.

## - `paymentUI`:
  - `DeletePaymentUI.java`: Enables deletion of payment details.
  - `NewPaymentUI.java`: Allows adding new payment information.
  - `PaymentUI.java`: The main interface for managing payments.
  - `ViewPaymentUI.java`: Displays payment details.

## - `recordsUI`:

-  `DeleteRecordsUI.java`: Enables deletion of record entries.
  - `EditRecordsUI.java`: Provides options for editing record details.
  - `ReadRecordsUI.java`: Displays the records of the art class participants.
  - `RecordsUI.java`: The main interface for managing records.
  - `RegisterRecordUI.java`: Allows registration of new art class participants.
  - `ViewBatchesUI.java`: Displays batches of students.

## Console Package Structure
The following folders and files are present in the `/ColoringMindsRecordKeepingSystem/Console` directory:

## - `attendance`:
  - `AttendanceMain.java`: The main class for managing attendance via the console.
  - `AttendanceRecords.java`: Provides functions for managing attendance records.
  - `DeleteAttendance.java`: Allows deletion of attendance records.
  - `ViewAttendance.java`: Enables viewing of attendance records.

## - `email`:
  - `EmailMain.java`: The main class for managing email functionalities via the console.
  - `GenerateBalanceEmail.java`: Generates emails to students regarding their balances.
  - `GenerateEmail.java`: Provides options to generate emails.
  - `GenerateSpecificDateEmail.java`: Allows generating emails for a specific date.
  - `GenerateSpecificEmail.java`: Generates emails to specific students.

## - `fees`:
  - `DeleteFees.java`: Enables deletion of fee-related information via the console.
  - `FeesMain.java`: The main class for managing fees via the console.
  - `GenerateFees.java`: Generates fees-related information via the console.
  - `ViewFees.java`: Allows viewing of fees information via the console.
  - `ViewStudentsThatHaveABalance.java`: Displays students who have a balance via the console.

## - `main`:
  - `CommandMain.java`: The main class for interacting with the system via the console.
  - `ResetPassword.java`: Provides functionality for resetting the password via the console.
  - `StartupPage.java`: The initial page of the console interface with login functionality.

## - `payment`:
  - `DeletePayment.java`: Enables deletion of payment details via the console.
  - `NewPayment.java`: Allows adding new payment information via the console.
  - `PaymentMain.java`: The main class for managing payments via the console.
  - `ViewPayment.java`: Displays payment details via the console.

## - `records`:
  - `DeleteRecords.java`: Enables deletion of record entries via the console.
  - `EditRecords.java`: Provides options for editing record details via the console.
  - `ReadRecords.java`: Displays the records of the art class participants via the console.
  - `RecordsMain.java`: The main class for managing records via the console.
  - `RegisterRecord.java`: Allows registration of new art class participants via the console.
  - `ViewBatches.java`: Displays batches of students via the console.

## Image
A custom-made image is located at the top of the menu interfaces, providing a visually appealing experience.

## Additional Information
For any further assistance or inquiries, please contact Asray Gopa, the author of this code repository.

Thank you for using the Coloring Minds Record Keeping System!
