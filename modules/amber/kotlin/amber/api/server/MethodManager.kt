package amber.api.server

import amber.api.MethodPath
import amber.api.normalize

class MethodManager(val mainMethodMap: MethodMap) {

    fun create(methodPath: MethodPath, method: Method) {
        val normalizedMethodPath = methodPath.normalize()
        val splintedMethodPath = normalizedMethodPath.split("/")
        val methodName = splintedMethodPath.last()
        val methodPath_ = splintedMethodPath.filter { it !== methodName }
        var currentMethodMap = mainMethodMap
        methodPath_.forEach {
            if (!currentMethodMap.contains(it)) {
                val newMap = MethodMap()
                currentMethodMap[it] = newMap
                currentMethodMap = newMap
            } else {
                currentMethodMap = currentMethodMap[it] as MethodMap
            }
        }
        if (currentMethodMap.contains(methodName)) {
            val value = currentMethodMap[methodName]
            if (value !is Method) {
                currentMethodMap.remove(methodName)
                currentMethodMap[methodName] = method
                if (method.isEmpty() || value!!.isEmpty()) {
                    value!!.entries.forEach {
                        method[it.key] = it.value
                    }
                } else throw Exception("Only methods with no submethods can replace middle method methodmaps.")
            } else throw Exception("Method already exists")
        } else {
            currentMethodMap[methodName] = method
        }
    }

    operator fun invoke(methodPath: MethodPath) = resolve(methodPath)

    fun resolve(methodPath: MethodPath): Method? {
        return mainMethodMap.resolve(methodPath)
    }

    private fun MethodMap.resolve(methodPath: MethodPath): Method? {
        val normalizedMethodPath = methodPath.normalize()
        val splintedMethodPath = normalizedMethodPath.split("/")
        return resolve(splintedMethodPath)
    }

    private fun MethodMap.resolve(splintedMethodPath: List<MethodPath>): Method? {
        var currentMethodMap = mainMethodMap
        val methodName = splintedMethodPath.last()
        val methodPath_ = splintedMethodPath.filter { it !== methodName }
        methodPath_.forEach {
            val item = currentMethodMap.get(it)
            when (item) {
                is MethodMap -> currentMethodMap = item
                else         -> return null
            }
        }
        if (currentMethodMap.contains(methodName)) {
            val item = currentMethodMap.get(methodName)
            when (item) {
                is Method -> return item
                else      -> {
                }
            }
        }
        return null
    }
}
