package at.stefangaller

import at.stefangaller.data.Book
import com.google.gson.GsonBuilder
import io.cucumber.java8.En
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals


class  BooksSteps: En {
    private val log = LoggerFactory.getLogger(javaClass)

    private lateinit var expectedBook: Book
    private lateinit var gotBooks: List<Book>

    init {
        Given("empty books database") {
            // https://stackoverflow.com/questions/40981804/is-there-a-way-to-run-raw-sql-with-kotlins-exposed-library
            Database.connect("jdbc:postgresql://localhost:5432/bookdb", user = "jr")
            transaction {
                TransactionManager.current().exec("TRUNCATE TABLE books;")
            }
        }

        When("I create the following book") { book: String ->
            withTestApplication({ mainWithDeps() }) {
                handleRequest(HttpMethod.Post, "/api/v1/book") {
                    addHeader("Content-Type", "application/json")
                    setBody(book)
                }.apply {
                    println("response status ${response.status()}")
                    assertEquals(HttpStatusCode.Accepted, response.status())
                }
            }
            expectedBook = GsonBuilder().create().fromJson(book, Book::class.java)
        }

        When("I request all books") {
            withTestApplication({ mainWithDeps() }) {
                handleRequest(HttpMethod.Get, "/api/v1/books").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    log.info("jsonArray {}",response.content)
                    gotBooks = GsonBuilder().create().fromJson(response.content, Array<Book>::class.java).toList()
                    log.info("books {}",gotBooks)
                }
            }
        }

        Then("my book is returned") {
            assertEquals(1, gotBooks.size)
            assertEquals(expectedBook, gotBooks[0].copy(id=0))
        }

        When("I delete the book titled {string}") { title: String ->
            val id = gotBooks.firstOrNull() { it.title == title } ?.id
                ?: throw IllegalArgumentException("no book titled $title found")
            withTestApplication({ mainWithDeps() }) {
                handleRequest(HttpMethod.Delete, "/api/v1/book/$id").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
            }
        }

        Then("no book is returned") {
            assertEquals(0, gotBooks.size)
        }
    }
}