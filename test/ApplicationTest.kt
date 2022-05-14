package at.stefangaller

import com.google.gson.Gson
import com.google.gson.JsonArray
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ mainWithDeps() }) {
            handleRequest(HttpMethod.Get, "/hello").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }
    @Test
    fun testPostBook() {
        withTestApplication({ mainWithDeps() }) {
            handleRequest(HttpMethod.Post, "/api/v1/book") {
                addHeader("Content-Type", "application/json")
                setBody("""{"title":"xyz","author":"abc","age":42}""")
            }.apply {
                println("response status ${response.status()}")
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun testGetBooks() {
        withTestApplication({ mainWithDeps() }) {
            handleRequest(HttpMethod.Get, "/api/v1/books").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val gson = Gson()
                val jsonArray = gson.fromJson(response.content, JsonArray::class.java)
                println("jsonArray ${response.content}")
                //assertEquals(1, jsonArray.size())
            }
        }
    }

}
