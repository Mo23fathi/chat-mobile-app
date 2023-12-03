# chat-mobile-app
it uses firebase services (cloud messaging , mobile authentication , firestore , storage  )

Introduction
ChatYY2 allows users to send and receive text messages and images in real-time. The application is built on the Android platform, utilizing Firebase services for user authentication, real-time data synchronization, and cloud storage. The architecture follows a modular structure, with each component serving a specific purpose.

Key Features
1. ChatActivity
Manages the chat interface for sending and receiving messages.
Displays messages in a RecyclerView.
Supports sending text messages and images.
2. ChatFragment
Displays recent chatrooms using FirestoreRecyclerAdapter.
Orders chatrooms based on the timestamp of the last message.
Handles lifecycle methods for efficient updates.
3. loading
Serves as a loading screen, directing users based on their authentication status.
Redirects to the main screen if logged in; otherwise, redirects to the login screen.
Handles incoming notifications to open specific chatrooms.
4. loginphonenum
Allows users to input their phone number for authentication.
Uses Firebase authentication to verify the user's phone number.
Sends an OTP to the user's phone for verification.
5. loginusername
Enables users to set their username and password after phone number verification.
Manages the process of setting a username and password.
Integrates with Firebase Authentication and Firestore for user data management.
6. MainActivity
Serves as the main activity managing bottom navigation and FCM token retrieval.
Integrates with Firebase Cloud Messaging (FCM) for efficient push notifications.
Uses BottomNavigationView for navigation between Chat and Profile fragments.
7. otp
Manages OTP verification after users input their phone number.
Handles OTP verification using Firebase Authentication.
Navigates to the username setting screen upon successful OTP verification.
8. ProfileFragment
Represents the user's profile settings and information.
Allows users to update their profile picture, username, and password.
Provides an option to log out from the application.
9. SearchUserActivity
Allows users to search for other users by their usernames.
Utilizes FirestoreRecyclerAdapter for dynamic display of search results.
Enables users to click on a result to view and start a chat with the selected user.
10. SearchUserRecyclerAdapter
Manages the RecyclerView for displaying search results in SearchUserActivity.
Uses FirestoreRecyclerAdapter for dynamic updates to search results.
Enables users to click on a result to initiate a chat.
11. UserModel
Represents the data model for a user, including phone number, username, password, creation timestamp, user ID, and FCM token.
Provides a structured way to store and retrieve user-related information.
12. RecentChatRecyclerAdapter
Manages the RecyclerView for displaying recent chatrooms in ChatFragment.
Uses FirestoreRecyclerAdapter for dynamic updates to recent chatrooms.
Provides a clickable interface to navigate to the corresponding chat.
13. ChatMessageModel
Represents the data model for a chat message, including message content, sender ID, and timestamp.
Provides a structured way to store and retrieve chat message-related information.


Technologies Used
Android (Java)
Firebase (Authentication, Firestore, Cloud Storage, Cloud Messaging)
Glide (Image Loading Library)
Installation
Clone the repository to your local machine.
Open the project in Android Studio.
Configure Firebase settings and add your google-services.json file.
Run the application on an Android emulator or physical device.
Usage
Open the application on your Android device.
Log in using your phone number and complete the verification process.
Set up your profile, including a profile picture, username, and password.
Explore recent chatrooms, search for users, and start conversations.
Enjoy real-time messaging with other users.
