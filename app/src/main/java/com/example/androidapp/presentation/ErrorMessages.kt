package com.example.androidapp.presentation

import java.io.IOException

// Turns an exception into a short text for the user.
// IOException = no internet / server not reachable (for example Wi-Fi is off).
fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "No internet connection. Check Wi-Fi and try again."
    else -> "Could not load decks (${message ?: "unknown error"})."
}
