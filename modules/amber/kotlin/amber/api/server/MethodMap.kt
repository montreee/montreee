package amber.api.server

import amber.api.MethodPath

open class MethodMap(val impl: MutableMap<MethodPath, MethodMap> = mutableMapOf()) : MutableMap<MethodPath, MethodMap> by impl
