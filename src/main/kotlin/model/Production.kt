package model

data class Production(
    val left: Value,
    val right: List<Value>,
    val id: Int,
) {

    constructor(left: String, right: List<String>, id: Int): this(Value(left, true), right.map { Value(it, true) }, id)

    override fun toString(): String {
        return """
            $left -> ${right.joinToString(" ", transform = { value -> value.toString() })}
        """.trimIndent()
    }

}
