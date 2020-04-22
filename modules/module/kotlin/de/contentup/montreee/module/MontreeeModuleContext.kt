package de.contentup.montreee.module

import amber.coroutines.OptimalDispatcher
import de.contentup.montreee.Context
import kotlin.coroutines.CoroutineContext

class MontreeeModuleContext(private val module: MontreeeModule) : Context {
    override val startup: CoroutineContext by lazy { dispatcherWithPurpose("Startup") }
    override val shutdown: CoroutineContext by lazy { dispatcherWithPurpose("Shutdown") }
    override val main: CoroutineContext by lazy { dispatcherWithPurpose("Main") }
    override val application: CoroutineContext by lazy { dispatcherWithPurpose("Application") }
    override val background: CoroutineContext by lazy { dispatcherWithPurpose("Background") }
    override val network: CoroutineContext by lazy { dispatcherWithPurpose("Network") }
    override val io: CoroutineContext by lazy { dispatcherWithPurpose("IO") }
    override val log: CoroutineContext by lazy { dispatcherWithPurpose("Log") }

    private fun dispatcherWithPurpose(purpose: String) =
            OptimalDispatcher("${module.name} $purpose")
}
