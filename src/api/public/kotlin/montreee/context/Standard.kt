package montreee.context

import amber.event.Event
import montreee.data.DataStore

typealias ContextData = DataStore<Any>
typealias ContextEvents = DataStore<Event<*>>
