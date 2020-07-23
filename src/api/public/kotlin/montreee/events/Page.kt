package montreee.events

import amber.event.Event
import montreee.context.ContextEvents
import montreee.page.Page


typealias PageCreateEvent = Event<Page>
typealias PageBeforeEvent = Event<Page>
typealias PageAfterEvent = Event<Page>

const val PAGE_CREATE_EVENT_ID = "page.create"
const val PAGE_BEFORE_EVENT_ID = "page.before"
const val PAGE_AFTER_EVENT_ID = "page.after"

class PageEventScope(private val events: ContextEvents) {
    val create get() = events.getOrInsert(PAGE_CREATE_EVENT_ID) { PageCreateEvent() }
    val before get() = events.getOrInsert(PAGE_BEFORE_EVENT_ID) { PageBeforeEvent() }
    val after get() = events.getOrInsert(PAGE_AFTER_EVENT_ID) { PageAfterEvent() }
}

val ContextEvents.page get() = PageEventScope(this)
