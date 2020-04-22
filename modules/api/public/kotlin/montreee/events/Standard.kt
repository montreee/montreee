package montreee.events

import amber.event.Event
import montreee.MontreeeApiException
import montreee.context.ContextEvents

@Suppress("UNCHECKED_CAST")
fun <T : Event<*>> ContextEvents.getOrInsert(name: String, factoryBlock: () -> T): T {
    return amber.trial.trial {
        this[name] as T
    } alternate {
        val event = factoryBlock()
        if (contains(name)) throw MontreeeApiException("Not allowed to insert to events with the same id.")
        this[name] = event
        event
    }
}
