import amber.serialization.parseYaml

fun main() {
    val yaml = """
        test:
            test2:
                test3: "hello"
                test5: 120
            test7: "test"
    """.trimIndent()
    val test = parseYaml(yaml)
    print(test)
}
