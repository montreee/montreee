package de.contentup.montreee.cli.cmd.module

import amber.factory.DynamicFactory
import amber.factory.DynamicLambdaFactory
import de.contentup.montreee.Version
import de.contentup.montreee.module.MontreeeModule

object ModuleLoader {

    private val modules = mutableMapOf<String, DynamicFactory<Version, MontreeeModule>>()

    private val _moduleNames = mutableListOf<List<String>>()
    internal val moduleNames get() = _moduleNames.toList()

    init {
        defineModules()
    }

    fun module(vararg name: String, block: (Version) -> MontreeeModule) {
        val factory = DynamicLambdaFactory(block)
        _moduleNames.add(listOf(*name))
        name.forEach {
            modules[it] = factory
        }
    }

    fun load(name: String): DynamicFactory<Version, MontreeeModule> {
        val lowerCaseName = name.toLowerCase()
        val matchingModules = modules.filter { it.key.startsWith(lowerCaseName) }
        when (matchingModules.size) {
            1    -> return matchingModules.entries.first().value
            else -> notFoundError(name, matchingModules.size)
        }
    }

    private fun notFoundError(name: String, numberOfMatchingModules: Int): Nothing {
        if (numberOfMatchingModules == 0) throw NoModuleFoundException(name)
        else throw MultipleModulesFoundException(name)
    }

    class NoModuleFoundException(val name: String) : Exception()
    class MultipleModulesFoundException(val name: String) : Exception()
}
