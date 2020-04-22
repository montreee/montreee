package amber.mapquery

open class MapQueryException(message: String = "") : Exception(message)

class MapQueryExceptionValueCantBeCastedTo(
        castFrom: String, castTo: String
) : MapQueryException("Can't cast $castFrom to $castTo")

class MapQueryExceptionUseNumericKeyForList : MapQueryException("You should use a numeric key on a list")
class MapQueryExceptionIllegalGet : MapQueryException("Get function can only be used on maps and lists")
class MapQueryExceptionOutOfListRangeException :
        MapQueryException("index/key has to be lager/equal to 0 and smaller size (of the list)")
