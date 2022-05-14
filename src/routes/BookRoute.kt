package at.stefangaller.routes

import at.stefangaller.data.Book
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
import org.kodein.di.ktor.closestDI

fun Route.books() {

    val bookService by closestDI().instance<BookService>()

    get("books") {
        val allBooks = bookService.getAllBooks()
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
