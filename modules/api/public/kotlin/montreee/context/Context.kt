package montreee.context

import kotlinx.coroutines.CoroutineScope
import montreee.data.DataRepository
import montreee.data.DataStore
import montreee.events.EventDataStore
import montreee.module.ModuleFactoryDataStore
import montreee.parameter.ParameterFactoryDataStore

class Context(
        val scope: CoroutineScope,
        val data: ContextData = DataStore(DataRepository()),
        val events: ContextEvents = EventDataStore(DataRepository()),
        val modules: ModuleFactoryDataStore = ModuleFactoryDataStore(DataRepository()),
        val parameter: ParameterFactoryDataStore = ParameterFactoryDataStore(DataRepository())
)
