package at.stefangaller.routes

import at.stefangaller.data.models.Book
import at.stefangaller.services.BookService
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.slf4j.LoggerFactory

fun Route.books() {

    val bookService by di().instance<BookService>()
    val logger = LoggerFactory.getLogger("Route.books")

    get("books") {
        val allBooks = bookService.getAllBooks()
        logger.info(allBooks.toString())
        call.respond(allBooks)
    }

    post("book") {
        val bookRequest = call.receive<Book>()
        bookService.addBook(bookRequest)
        call.respond(HttpStatusCode.Accepted)
    }

    delete("book/{id}") {
        val bookId = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
        bookService.deleteBook(bookId)
        call.respond(HttpStatusCode.OK)
    }
}
