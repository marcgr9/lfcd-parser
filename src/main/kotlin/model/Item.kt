package model

class Item(production: Production) : Cloneable {

    val left: String
    val right: List<String>
    var dot: Int
    val id: Int

    init {
        left = production.left.value
        right = production.right.map { it.value }
        dot = 0
        id = production.id
    }

    constructor(item: Item) : this(Production(item.left, item.right, item.id)){
        dot = item.dot
    }

    fun goTo(): Boolean {
        if (dot >= right.size) return false
        dot++
        return true
    }

    fun currentSymbol(): String {
        return if (dot >= right.size) ""
        else right[dot]
    }

    override fun toString(): String {
        var out = "$left ->"
        right.forEachIndexed { index, element ->
            out += if (index == dot) " .$element"
            else " $element"
        }
        if (dot == right.size)
            out += "."
        return out
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Item) return false
        return left == other.left && right == other.right && dot == other.dot
    }

    public override fun clone(): Item = Item(this)

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + dot
        return result
    }

}