package montreee.module

import amber.factory.Factory

abstract class ModuleFactory(val name: String) : Factory<ModuleLogic>
