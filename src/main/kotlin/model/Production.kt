package model

data class Production(
    val left: Value,
    val right: List<Value>,
) {

    constructor(left: String, right: List<String>): this(Value(left, true), right.map { Value(it, true) })

    override fun toString(): String {
        return """
            $left -> ${right.joinToString(" ", transform = { value -> value.toString() })}
        """.trimIndent()
    }

}
