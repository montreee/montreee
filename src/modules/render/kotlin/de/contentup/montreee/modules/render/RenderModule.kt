package de.contentup.montreee.modules.render

import amber.api.client.KtorWSBdtpApiClient
import amber.api.response.TextResponse
import amber.api.server.api
import amber.api.server.impl.bdtp.BdtpApiImpl
import amber.api.server.method
import amber.coroutines.scope
import amber.logging.Logger
import amber.logging.adapter.Slf4JLogBinder
import amber.logging.info
import amber.result.Result
import amber.result.onSuccess
import amber.serialization.parseJson
import amber.sync.Synchronized
import de.contentup.montreee.Version
import de.contentup.montreee.module.MontreeeModule
import de.contentup.montreee.module.asyncAutoRetry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import montreee.Engine
import montreee.features.standard.installStandardFeatures
import montreee.result.ResultElement

class RenderModule(version: Version) : MontreeeModule("Montreee Render Module", version) {

    val engine by lazy {
        runBlocking(context.startup) {
            Engine().apply {
                installStandardFeatures()
                install(plugins)
            }
        }
    }

    val synchronized = Synchronized()

    val renderResults = mutableMapOf<String, Result<ResultElement, Throwable>>()

    val pages = mutableMapOf<String, String>()

    val api by lazy {
        api {
            method("update") {
                synchronized.asyncSynchronized {
                    if (!parameter.contains("name") || !parameter.contains("data")) return@asyncSynchronized

                    pages[parameter["name"]!!] = parameter["data"]!!
                }
            }
            method("render") {
                synchronized.asyncSynchronized {
                    if (!parameter.contains("name")) return@asyncSynchronized

                    val renderer = engine.newRenderer()
                    renderer.createPage(
                            parameter["name"]!!,
                            parseJson(pages[parameter["name"]!!]!!)
                    )
                    renderer.invoke().forEach {
                        renderResults[it.key] = it.value
                    }
                }
            }
            method("result") {
                synchronized.asyncSynchronized {
                    if (!parameter.contains("name")) return@asyncSynchronized

                    renderResults[parameter["name"]!!]!!.onSuccess {
                        respond(TextResponse(value.read()))
                    }
                }
            }
        }
    }

    private lateinit var apiClient: KtorWSBdtpApiClient
    private lateinit var bdtpApiImpl: BdtpApiImpl

    override fun launch() {
        Slf4JLogBinder.register(logger)

        apiClient = KtorWSBdtpApiClient(
                config.render.host,
                config.render.port.toInt(),
                config.encryption,
                Logger("Render Module KtorWSBdtpApiClient Logger", logger),
                -1,
                context.network.scope()
        )
        bdtpApiImpl = BdtpApiImpl(api, apiClient.engine)
        bdtpApiImpl.start()

        context.network.scope {
            launch {
                asyncAutoRetry {
                    apiClient.connect()
                    apiClient.call("register/renderer")
                }
                logger.info { +"connection to core module established" }
            }
        }
    }

    override fun exit() {
        bdtpApiImpl.stop()
        apiClient.disconnect()
    }
}
