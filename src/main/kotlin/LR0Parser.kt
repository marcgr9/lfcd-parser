import model.*
import java.util.Stack
import java.util.stream.IntStream

class LR0Parser(
    private val grammar: Grammar
) {

    val canonicalCollection: MutableList<State> = mutableListOf()
    val goToTable: MutableMap<Int, MutableMap<String, Int>> = mutableMapOf()
    val actionTable: MutableMap<Int, MutableMap<String, Action>> = mutableMapOf()
    var result = mutableListOf<Production>()
    var nrStates = 0

    fun parse(input: String) {
        val stack = Stack<String>().apply {
            push("0")
        }

        var index = 0
        var accepted = false
        while (!accepted) {
            val currentSymbol = input[index].toString()
            val top = stack.peek().toInt()

            when (val action = actionTable[top]?.get(currentSymbol)) {
                is Reduce -> {
                    val production = grammar.productions.find { it.id == action.prodID }!!
                    val toPush = production.left.value

                    IntStream.range(0, production.right.size * 2).forEach {
                        stack.pop()
                    }
                    result.add(0, production)

                    val lastToPush = goToTable[stack.peek().toInt()]!![toPush].toString()
                    stack.apply {
                        push(toPush)
                        push(lastToPush)
                    }
                }
                is Shift -> {
                    stack.push(currentSymbol)
                    stack.push(action.stateID.toString())
                    index++
                }
                else -> {
                    accepted = true
                }
            }
        }
    }

    fun createTables() {
        canonicalCollection.forEach {
            goToTable[it.id] = mutableMapOf()
            actionTable[it.id] = mutableMapOf()
        }
        canonicalCollection.forEach { state ->
            state.nextStates.forEach {
                if (grammar.isNonTerminal(it.symbol))
                    goToTable[state.id]?.put(it.symbol, it.id)
                else {
                    actionTable[state.id]?.put(it.symbol, Shift(it.id))
                }
            }
        }
        canonicalCollection.filter {
            actionTable[it.id]!!.isEmpty()
        }.forEach {
            val productionId = it.items.first().id

            if (productionId == 0) {
                actionTable[it.id]?.put("$", Accept())
                return@forEach
            }

            grammar.terminals.forEach { symbol ->
                if (actionTable[it.id]!![symbol] != null) {
                    println("conflict: S${it.id} symbol $symbol")
                } else {
                    actionTable[it.id]?.put(symbol, Reduce(productionId))
                }
            }
        }
    }

    fun createCanonicalCollection() {
        val start = mutableSetOf(Item(grammar.productions.first()))
        val startState = State(grammar, start, nrStates)
        val temp: MutableList<State> = mutableListOf<State>().apply {
            add(startState)
        }

        while (temp.size > 0) {
            val current = temp.first()
            temp.remove(current)

            val initialItem = current.items.first().clone()
            current.closure()

            if (current in canonicalCollection) continue

            val copy = current.clone()
            current.items.forEach {
                if (!it.goTo()) return@forEach

                nrStates++
                val newState = State(grammar, mutableSetOf(it), nrStates)
                if (newState !in temp) temp.add(newState)
                else nrStates--

                if (it == initialItem) copy.nextStates.add(current)
                else copy.nextStates.add(temp.first { it == newState })
            }
            canonicalCollection.add(copy)
        }
    }

}
