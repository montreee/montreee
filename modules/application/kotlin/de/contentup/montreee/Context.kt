package de.contentup.montreee

import kotlin.coroutines.CoroutineContext

interface Context {
    val startup: CoroutineContext
    val shutdown: CoroutineContext
    val main: CoroutineContext
    val application: CoroutineContext
    val background: CoroutineContext
    val network: CoroutineContext
    val io: CoroutineContext
    val log: CoroutineContext
}
