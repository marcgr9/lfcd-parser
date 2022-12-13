package model

data class Production(
    val left: Value,
    val right: List<Value>,
    val name: String,
) {

    override fun toString(): String {
        return """
            $left -> ${right.joinToString(" ", transform = { value -> value.toString() })} ($name)
        """.trimIndent()
    }

}
