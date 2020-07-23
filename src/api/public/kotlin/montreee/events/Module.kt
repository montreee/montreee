package montreee.events

import amber.event.Event
import montreee.context.ContextEvents
import montreee.module.ModuleLogic
import montreee.module.phases.*


typealias ModuleCreateEvent = Event<ModuleLogic>
typealias ModuleBeforeEvent = Event<ModuleLogic>
typealias ModuleInfoEvent = Event<InfoContext>
typealias ModuleSetupEvent = Event<SetupContext>
typealias ModuleFeatureEvent = Event<FeatureContext>
typealias ModuleRenderEvent = Event<RenderContext>
typealias ModuleFinalEvent = Event<FinalContext>
typealias ModuleAfterEvent = Event<ModuleLogic>

const val MODULE_CREATE_EVENT_ID = "module.create"
const val MODULE_BEFORE_EVENT_ID = "module.before"
const val MODULE_INFO_EVENT_ID = "module.info"
const val MODULE_SETUP_EVENT_ID = "module.setup"
const val MODULE_FEATURE_EVENT_ID = "module.feature"
const val MODULE_RENDER_EVENT_ID = "module.render"
const val MODULE_FINAL_EVENT_ID = "module.final"
const val MODULE_AFTER_EVENT_ID = "module.after"

class ModuleEventScope(private val events: ContextEvents) {
    val create get() = events.getOrInsert(MODULE_CREATE_EVENT_ID) { ModuleCreateEvent() }
    val before get() = events.getOrInsert(MODULE_BEFORE_EVENT_ID) { ModuleBeforeEvent() }
    val info get() = events.getOrInsert(MODULE_INFO_EVENT_ID) { ModuleInfoEvent() }
    val setup get() = events.getOrInsert(MODULE_SETUP_EVENT_ID) { ModuleSetupEvent() }
    val feature get() = events.getOrInsert(MODULE_FEATURE_EVENT_ID) { ModuleFeatureEvent() }
    val render get() = events.getOrInsert(MODULE_RENDER_EVENT_ID) { ModuleRenderEvent() }
    val final get() = events.getOrInsert(MODULE_FINAL_EVENT_ID) { ModuleFinalEvent() }
    val after get() = events.getOrInsert(MODULE_AFTER_EVENT_ID) { ModuleAfterEvent() }
}

val ContextEvents.module get() = ModuleEventScope(this)
