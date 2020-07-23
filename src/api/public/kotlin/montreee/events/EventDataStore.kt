package montreee.events

import amber.event.Event
import montreee.data.DataRepository
import montreee.data.DataStore

class EventDataStore internal constructor(impl: DataRepository<Event<*>>) : DataStore<Event<*>>(impl)
