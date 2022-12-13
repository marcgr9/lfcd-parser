package model

data class Value(
    val value: String,
    val isTerminal: Boolean,
) {

    override fun toString(): String = value

}
