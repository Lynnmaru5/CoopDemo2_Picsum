package com.example.customtoastdemo

// Provides a random quote from a built-in list
class QuoteRepository {

    // Local list of quotes (no API needed)
    private val quotes = listOf(
        QuoteResponse(
            quote = "If you don’t take risks, you can’t create a future.",
            character = "Monkey D. Luffy",
            anime = "One Piece"
        ),
        QuoteResponse(
            quote = "A dropout will beat a genius through hard work.",
            character = "Rock Lee",
            anime = "Naruto"
        ),
        QuoteResponse(
            quote = "Sometimes, we have to look beyond what we want and do what’s best.",
            character = "Piccolo",
            anime = "Dragon Ball Z"
        ),
        QuoteResponse(
            quote = "Whatever you lose, you’ll find it again. But what you throw away, you’ll never get back.",
            character = "Kenshin Himura",
            anime = "Rurouni Kenshin"
        )
    )

    // Returns one random quote every time
    fun getRandomQuote(): QuoteResponse {
        return quotes.random()
    }
}
