package at.stefangaller

import io.cucumber.java8.En
import io.ktor.http.*
import io.ktor.server.testing.*
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class WebServerSteps: En {
    lateinit var gotResponse: String
    init {
        When("I request {string}") { uri: String ->
            withTestApplication({ mainWithDeps() }) {
                handleRequest(HttpMethod.Get, uri).apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    gotResponse = response.content
                        ?: throw IllegalArgumentException("response to $uri does not contain a body")
                }
            }
        }
        Then("the server responds {string}") { expResponse: String ->
            assertEquals(expResponse, gotResponse)
        }
    }
}