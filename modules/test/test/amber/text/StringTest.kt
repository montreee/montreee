package amber.text

import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec

class StringTest : AnnotationSpec() {

    @Test
    fun `String cut function`() {
        "~oifjdg|isef65$1*foo/bar?param=value&foo=bar§text/json;{key:value}".cut(
                '#', '~', '|', '%', '$', '*', '!', '§', ';'
        ).size shouldBe 7
    }
}
