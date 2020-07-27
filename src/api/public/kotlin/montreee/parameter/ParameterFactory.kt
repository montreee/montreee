package montreee.parameter

import amber.factory.Factory

abstract class ParameterFactory<T>(val name: String) : Factory<Parameter<T>>
