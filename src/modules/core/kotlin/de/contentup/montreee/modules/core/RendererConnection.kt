package de.contentup.montreee.modules.core

import amber.api.client.BdtpApiClient
import amber.api.constructParameter

class RendererConnection(val client: BdtpApiClient) {

    suspend fun update(pageName: String, pageData: String) {
        client.call("update", constructParameter {
            "name" to pageName
            "data" to pageData
        })
    }

    suspend fun render(pageName: String) {
        client.call("render", constructParameter {
            "name" to pageName
        })
    }

    suspend fun render() {
        client.call("render")
    }
}