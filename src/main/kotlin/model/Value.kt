package model

data class Value(
    val value: String,
    val isTerminal: Boolean? = null,
) {

    override fun toString(): String = value

}
