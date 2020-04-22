package amber.properties.groups

import amber.properties.BindablePropertyBase
import amber.properties.binding.Binding

class PropertyGroupBinding<T>(
        private val propertyGroup: PropertyGroup<T>, from: BindablePropertyBase<T>, to: BindablePropertyBase<T>
) : Binding<T>(from, to) {

    override fun unbind() {
        if (isBound) {
            isBound = false
            propertyGroup.remove(from)
            super.unbind()
        }
    }
}
