package amber.text

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class `string cut - function` : AnnotationSpec() {

    @Test
    fun `String cut - function`() {
        "~oifjdg|isef65$1*foo/bar?param=value&foo=bar§text/json;{key:value}".cut(
                '#', '~', '|', '%', '$', '*', '!', '§', ';'
        ).size shouldBe 7
    }
}
