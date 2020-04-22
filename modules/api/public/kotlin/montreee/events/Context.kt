package montreee.events

import amber.event.Event
import montreee.context.Context
import montreee.context.ContextEvents


typealias ContextCreateEvent = Event<Context>
typealias ContextDestroyEvent = Event<Context>

const val CONTEXT_CREATE_EVENT_ID = "context.create"
const val CONTEXT_DESTROY_EVENT_ID = "context.destroy"

class ContextEventScope(private val events: ContextEvents) {
    val create get() = events.getOrInsert(CONTEXT_CREATE_EVENT_ID) { ContextCreateEvent() }
    val destroy get() = events.getOrInsert(CONTEXT_DESTROY_EVENT_ID) { ContextDestroyEvent() }
}

val ContextEvents.context get() = ContextEventScope(this)
