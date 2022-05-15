package at.stefangaller.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*


fun Routing.apiRoute() {
    route("/api/v1") {
        books()
    }
    route("/hello", HttpMethod.Get) {
        handle {
            call.respondText("HELLO WORLD!")
        }
    }
}