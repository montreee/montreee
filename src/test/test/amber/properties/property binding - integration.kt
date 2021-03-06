package amber.properties

import amber.properties.exceptions.IllegalSetPropertyException
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class `property binding - integration` : AnnotationSpec() {

    @Test
    fun `basic binding - integration`() {

        val property1 = StringProperty("1")
        val property2 = StringProperty("2")

        property2 bind property1

        property2.value shouldBe "1"

        property1.value = "3"

        property2.value shouldBe "3"

        shouldThrowExactlyUnit<IllegalSetPropertyException> {
            property2.value = "4"
        }
    }

    @Test
    fun `bidirectional binding - integration`() {

        val property1 = StringProperty("1")
        val property2 = StringProperty("2")

        property2 bind property1
        property1 bind property2

        property2.value shouldBe "1"

        property1.value = "3"

        property2.value shouldBe "3"

        property2.value = "4"

        property1.value shouldBe "4"
    }
}
