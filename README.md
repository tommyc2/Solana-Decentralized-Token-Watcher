# Solana Decentralized Token Watcher
A Kotlin based mobile app for analysing solana DEX tokens on the solana blockchain

<img width="353" height="758" alt="Screenshot from 2025-10-22 11-58-05" src="https://github.com/user-attachments/assets/d532730e-01f3-4fbb-a72a-f9e4bb65e4ef" />

# Features
- Monitor Solana DEX tokens in real-time
- Analyze token performance
- User-friendly interface for easy navigation
- Add/Delete tokens to and from watchlist

# Installation
1. Install any necessary dependencies
2. Ensure you have Android Studio installed
3. Clone the repository
4. Open the project in Android Studio
5. Build and run the app on your Android device or emulator

# AI-usage for assignment
This project was created with the assistance of AI tools like CoPilot to help generate code snippets and provide suggestions. All code was reviewed and modified by myself.

AI was used for the following tasks:

1. Coroutine implementation assistance (lifecycleScope + Dispatcher.IO skeleton code)
2. Implementing internal storage persistence (using openFileInput and openFileOutput)
3. General code optimization and best practices (lambdas, try/catch blocks, etc.)

I used lifecycle coroutines as the web API call was blocking the UI, so it made sense to move it to a background thread.
I also wanted to implement internal storage persistence instead of external persistence as it made more sense for my app.
