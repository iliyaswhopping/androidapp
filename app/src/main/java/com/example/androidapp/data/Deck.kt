package com.example.androidapp.data

data class Flashcard(
    val front: String,
    val back: String
)

data class Deck(
    val id: Int,
    val title: String,
    val category: String,
    val emoji: String,
    val description: String,
    val cards: List<Flashcard>
)

val categories = listOf("All", "Languages", "Science", "Programming", "History", "Music")

val decks = listOf(
    Deck(
        id = 1,
        title = "German A1: Verbs",
        category = "Languages",
        emoji = "🇩🇪",
        description = "Basic German verbs you need every day. Great for the first months of learning.",
        cards = listOf(
            Flashcard("gehen", "to go"),
            Flashcard("kommen", "to come"),
            Flashcard("machen", "to do / to make"),
            Flashcard("sprechen", "to speak"),
            Flashcard("ankommen", "to arrive")
        )
    ),
    Deck(
        id = 2,
        title = "German A1: Seasons and Weather",
        category = "Languages",
        emoji = "☀️",
        description = "Words for the four seasons and the weather.",
        cards = listOf(
            Flashcard("der Frühling", "spring"),
            Flashcard("der Sommer", "summer"),
            Flashcard("der Herbst", "autumn"),
            Flashcard("der Winter", "winter"),
            Flashcard("der Schnee", "snow")
        )
    ),
    Deck(
        id = 3,
        title = "Kazakh Basics",
        category = "Languages",
        emoji = "🇰🇿",
        description = "Simple everyday Kazakh words and phrases.",
        cards = listOf(
            Flashcard("Сәлеметсіз бе", "Hello (polite)"),
            Flashcard("Рахмет", "Thank you"),
            Flashcard("Иә / Жоқ", "Yes / No"),
            Flashcard("Су", "Water")
        )
    ),
    Deck(
        id = 4,
        title = "English Phrasal Verbs That Every Student Should Know Before the IELTS Exam",
        category = "Languages",
        emoji = "🇬🇧",
        description = "A deck with a very long title to check that the layout does not break.",
        cards = listOf(
            Flashcard("give up", "to stop trying"),
            Flashcard("look after", "to take care of"),
            Flashcard("put off", "to postpone"),
            Flashcard("figure out", "to understand / to solve")
        )
    ),
    Deck(
        id = 5,
        title = "Chemistry: Elements",
        category = "Science",
        emoji = "🧪",
        description = "Chemical symbols of the most common elements.",
        cards = listOf(
            Flashcard("H", "Hydrogen"),
            Flashcard("O", "Oxygen"),
            Flashcard("Na", "Sodium"),
            Flashcard("Fe", "Iron"),
            Flashcard("Au", "Gold")
        )
    ),
    Deck(
        id = 6,
        title = "Physics Formulas",
        category = "Science",
        emoji = "⚛️",
        description = "Key formulas from school and first-year physics.",
        cards = listOf(
            Flashcard("Newton's second law", "F = m · a"),
            Flashcard("Kinetic energy", "E = m · v² / 2"),
            Flashcard("Ohm's law", "U = I · R"),
            Flashcard("Density", "ρ = m / V")
        )
    ),
    Deck(
        id = 7,
        title = "Biology: The Cell",
        category = "Science",
        emoji = "🧬",
        description = "Parts of the cell and what they do.",
        cards = listOf(
            Flashcard("Mitochondria", "Produces energy (ATP)"),
            Flashcard("Nucleus", "Stores DNA"),
            Flashcard("Ribosome", "Makes proteins")
        )
    ),
    Deck(
        id = 8,
        title = "Kotlin Basics",
        category = "Programming",
        emoji = "💻",
        description = "Core Kotlin keywords and ideas.",
        cards = listOf(
            Flashcard("val vs var", "val cannot be reassigned, var can"),
            Flashcard("data class", "A class that holds data, gets equals/toString/copy for free"),
            Flashcard("?.", "Safe call: runs only if the value is not null"),
            Flashcard("?:", "Elvis operator: default value when the left side is null")
        )
    ),
    Deck(
        id = 9,
        title = "Jetpack Compose",
        category = "Programming",
        emoji = "📱",
        description = "Layout building blocks of Jetpack Compose.",
        cards = listOf(
            Flashcard("Column", "Places children vertically"),
            Flashcard("Row", "Places children horizontally"),
            Flashcard("LazyColumn", "A scrolling vertical list that only draws visible items"),
            Flashcard("Scaffold", "Screen skeleton with slots for top bar, content and more")
        )
    ),
    Deck(
        id = 10,
        title = "Git Commands",
        category = "Programming",
        emoji = "🌿",
        description = "The Git commands you use every day.",
        cards = listOf(
            Flashcard("git add", "Stage changes for the next commit"),
            Flashcard("git commit -m", "Save staged changes with a message"),
            Flashcard("git push", "Upload commits to the remote repository"),
            Flashcard("git pull", "Download and merge remote changes")
        )
    ),
    Deck(
        id = 11,
        title = "History of Kazakhstan",
        category = "History",
        emoji = "🏛️",
        description = "Important dates from the history of Kazakhstan.",
        cards = listOf(
            Flashcard("1465", "Kazakh Khanate founded"),
            Flashcard("16 December 1991", "Independence of Kazakhstan"),
            Flashcard("1997", "Capital moved to Astana")
        )
    ),
    Deck(
        id = 12,
        title = "World War II",
        category = "History",
        emoji = "📜",
        description = "Key events and dates of World War II.",
        cards = listOf(
            Flashcard("1 September 1939", "Germany invades Poland, the war begins"),
            Flashcard("22 June 1941", "Germany attacks the USSR"),
            Flashcard("8–9 May 1945", "Germany surrenders")
        )
    ),
    Deck(
        id = 13,
        title = "My New Deck",
        category = "Programming",
        emoji = "🆕",
        description = "A deck without cards yet, it shows the empty state on the detail screen.",
        cards = emptyList()
    )
)
