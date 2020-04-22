package montreee.module

import montreee.descriptor.ModuleDescriptor
import montreee.module.phases.InfoContext

data class ModuleInfo(val descriptor: ModuleDescriptor) {
    constructor(context: InfoContext) : this(context.moduleDescriptor)
}