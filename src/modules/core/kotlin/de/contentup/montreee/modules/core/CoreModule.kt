package de.contentup.montreee.modules.core

import amber.api.constructParameter
import amber.api.server.api
import amber.api.server.group
import amber.api.server.impl.bdtp.BdtpApiImpl
import amber.api.server.method
import amber.bdtp.Engine
import amber.bdtp.impl.KtorWSBdtpServer
import amber.collections.sync
import amber.coroutines.scope
import amber.logging.Logger
import amber.logging.adapter.Slf4JLogBinder
import de.contentup.montreee.Version
import de.contentup.montreee.module.MontreeeModule
import de.contentup.montreee.util.api.client
import de.contentup.montreee.util.api.connection

class CoreModule(version: Version) : MontreeeModule("Montreee Core Module", version) {

    private val renderer = mutableListOf<RendererConnection>().sync()

    private val serverBdtpEngine = Engine(context.network.scope())

    private val api = api {
        group("register") {
            method("renderer") {
                val rendererConnection = RendererConnection(client)
                connection.onClose {
                    renderer.remove(rendererConnection)
                }
                renderer.add(rendererConnection)
            }
            method("database") {

            }
            method("api") {

            }
        }
        method("renderer") {

        }
        method("database") {

        }
        group("api") {
            group("renderer") {
                method("update") {
                    if (!parameter.contains("name") || !parameter.contains("data")) return@method

                    renderer.forEach {
                        it.client.call("update", parameter)
                    }
                }
                method("render") {
                    if (!parameter.contains("name")) return@method

                    //todo pick one not multiple
                    renderer.forEach {
                        it.client.call("render", constructParameter { "name" to parameter["name"]!! })
                    }
                }
                method("result") {
                    if (!parameter.contains("name")) return@method

                    respond(renderer.random().client.call("result", parameter))
                }
            }
        }
    }

    private val serverBdtpImpl by lazy {
        KtorWSBdtpServer(
                serverBdtpEngine,
                config.core.port.toInt(),
                config.encryption,
                Logger("Core Api Server " + "Logger", logger),
                context.network.scope()
        )
    }

    private val apiImpl = BdtpApiImpl(api, serverBdtpEngine)

    override fun launch() {
        Slf4JLogBinder.register(logger)
        apiImpl.start()
        serverBdtpImpl.start()
    }

    override fun exit() {
        apiImpl.stop()
        serverBdtpImpl.stop()
    }
}
