package amber.properties

import amber.properties.binding.Binding
import amber.properties.exceptions.AlreadyBoundPropertyException
import amber.properties.exceptions.IllegalBindPropertyException
import amber.properties.exceptions.IllegalSetPropertyException
import amber.properties.value.PropertyValue
import kotlinx.coroutines.sync.Mutex

abstract class BindablePropertyBase<T>(initValue: PropertyValue<T>) : ListenablePropertyBase<T>(initValue) {

    protected val mutex = Mutex()

    var binding: Binding<T>? = null
        set(value) {
            synchronized {
                field = if (field == null) {
                    if (value?.from === this) {
                        bindInternal(value)
                        value
                    } else throw IllegalBindPropertyException()
                } else if (value == null) {
                    unbindInternal(field!!)
                    null
                } else throw AlreadyBoundPropertyException()
            }
        }
        get() = synchronized {
            field
        }

    val isBound: Boolean
        get() {
            return binding != null
        }

    private fun bindInternal(binding: Binding<T>) {
        value = binding.value
        if (!binding.isBound) {
            binding.bind()
        }
    }

    private fun unbindInternal(binding: Binding<T>) {
        binding.unbind()
    }

    infix fun bind(to: BindablePropertyBase<T>) {
        binding = Binding<T>(this, to)
    }

    fun unbind() {
        this.binding = null
    }

    override fun checkSet(newValue: PropertyValue<T>) {
        super.checkSet(newValue)
        if (isBound) if (!(binding!!.value === newValue.value || binding?.isBidirectional!!)) throw IllegalSetPropertyException()
    }
}
