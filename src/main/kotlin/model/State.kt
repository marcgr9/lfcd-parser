package model

import Grammar

class State(
    private val grammar: Grammar,
    val core: MutableSet<Item>,
    val id: Int
) : Cloneable {

    val symbol: String
    val nextStates = mutableListOf<State>()
    val items: MutableSet<Item> = mutableSetOf()

    init {
        items.addAll(core)
        symbol = if (id > 0) {
            items.first().let {
                it.right[it.dot - 1]
            }
        } else ""
    }

    fun closure() {
        val temp = mutableSetOf<Item>()

        var done = false
        while (!done) {
            done = true

            items.filter {
                it.currentSymbol().isNotEmpty() && grammar.isNonTerminal(it.currentSymbol())
            }.forEach {
                val productions = grammar.getProductionsForNonterminal(it.currentSymbol())
                temp.addAll(productions.map { prod -> Item(prod) })
            }
            if (!items.containsAll(temp)) {
                items.addAll(temp)
                done = false
            }
            temp.clear()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is State)
            return false
        return this.items == other.items
    }

    public override fun clone(): State {
        val itemsCopy = mutableSetOf<Item>()
        items.forEach { itemsCopy.add(it.clone()) }
        return State(grammar, itemsCopy, id)
    }

    override fun toString(): String {
        return "$id $items $symbol"
    }

    override fun hashCode(): Int {
        var result = grammar.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }

}