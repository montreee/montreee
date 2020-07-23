package amber.properties.binding

import amber.properties.BindablePropertyBase

open class Binding<T>(val from: BindablePropertyBase<T>, val to: BindablePropertyBase<T>) {

    var isBound = false
        protected set

    val isBidirectional: Boolean
        get() {
            if (from.isBound && to.isBound) if ((to.binding?.to === from) && (from.binding?.to === to)) return true
            return false
        }

    private val listener = BindingPropertyEventListener<T>(from, to, this)

    var value = to.value

    open fun bind() {
        value = to.value
        to.addListener(listener)
        isBound = true
    }

    open fun unbind() {
        listener.removeItself()
        isBound = false
    }
}
