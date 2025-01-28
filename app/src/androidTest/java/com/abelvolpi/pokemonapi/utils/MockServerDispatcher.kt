package com.abelvolpi.pokemonapi.utils

import com.abelvolpi.pokemonapi.utils.UiTestUtils.getJsonContent
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher {
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/pokemon?offset=0&limit=20" -> MockResponse()
                    .setResponseCode(200)
                    .setBody(getJsonContent("pokemon_list.json"))

                "/pokemon/charizard" -> MockResponse()
                    .setResponseCode(200)
                    .setBody(getJsonContent("pokemon_details.json"))

                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }
}
