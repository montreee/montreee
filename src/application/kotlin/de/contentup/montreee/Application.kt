package de.contentup.montreee

import amber.config.Config
import amber.event.Event
import amber.logging.Logger

object Application : ApplicationInterface {

    override val version: Version get() = delegate.version

    override val name: String get() = delegate.name

    override val logger: Logger get() = delegate.logger

    override val config: Config get() = delegate.config

    override val context: Context get() = delegate.context

    val events = Events()

    class Events {
        val onBeforeLaunch = Event<ApplicationInterface>()
        val onLaunch = Event<ApplicationInterface>()
        val onBeforeExit = Event<ApplicationInterface>()
        val onExit = Event<ApplicationInterface>()
    }

    private var _delegate: ApplicationInterface? = null
    val delegate: ApplicationInterface
        get() = _delegate ?: throw Exception("You have to launch an application before you can use it.")

    fun launch(app: ApplicationInterface) {
        if (app === this || app === _delegate) throw Exception("You can't launch this.")

        if (_delegate != null) exit()

        _delegate = app
        events.onBeforeLaunch.fire(delegate)
        delegate.launch()
        events.onLaunch.fire(delegate)
    }

    override fun exit() {
        events.onBeforeExit.fire(delegate)
        delegate.exit()
        val old = delegate
        _delegate = null
        events.onExit.fire(old)
    }

    override fun launch() {
        throw Exception("Not Allowed.")
    }
}
