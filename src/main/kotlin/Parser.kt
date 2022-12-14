import model.Value


class Parser(
    private val grammarReader: Grammar,
    var firstTable: HashMap<String, MutableSet<String>> = hashMapOf(),
    var followTable: HashMap<String, MutableSet<String>> = hashMapOf(),
) {

    var epsilon = "$"

    fun concatenateLengthOne(list: List<Set<String>?>): Set<String> {
        val result = HashSet<String>()
        var index = 0
        var done = false
        while (!done && index < list.size) {
            if (list[index] != null) {
                for (v in list[index]!!) {
                    if (epsilon != v) {
                        result.add(v)
                    }
                }
                if (list[index]!!.contains(epsilon)) {
                    index += 1
                } else {
                    done = true
                }
            }
        }
        return result
    }

    fun areStatesEqual(state1: HashMap<String, Set<String>>, state2: HashMap<String, Set<String>>): Boolean {
        var match = true
        for (key in state1.keys) {
            if (!state2.containsKey(key)) {
                match = false
                break
            }
            for (value in state1[key]!!) {
                if (!state2[key]!!.contains(value)) {
                    match = false
                    break
                }
            }
        }
        for (key in state2.keys) {
            if (!state1.containsKey(key)) {
                match = false
                break
            }
            for (value in state2[key]!!) {
                if (!state1[key]!!.contains(value)) {
                    match = false
                    break
                }
            }
        }
        return match
    }

    fun computeFirst() {
        var previous = HashMap<String, Set<String>>()
        for (v in grammarReader.nonTerminals) {
            previous[v] = HashSet()
        }
        var current = HashMap<String, MutableSet<String>>()
        var done = false
        while (!done) {
            for (nonTerminal in grammarReader.nonTerminals) {
                current[nonTerminal] = HashSet()
            }
            for (nonTerminal in grammarReader.nonTerminals) {
                val productions = grammarReader.getProductionsForNonterminal(nonTerminal)
                for (production in productions) {
                    val value = production.right[0]
                    if (value.isTerminal) {
                        current[nonTerminal]!!.add(production.right[0].value)
                    } else {
                        val setList: MutableList<Set<String>?> = ArrayList()
                        for (rightValue in production.right) {
                            if (rightValue.isTerminal) {
                                val toAdd: MutableSet<String> = HashSet()
                                toAdd.add(rightValue.value)
                                setList.add(toAdd)
                            } else {
                                val previousList = previous[rightValue.value]
                                if (previousList != null) setList.add(previousList)
                            }
                        }
                        current[nonTerminal]!!.addAll(concatenateLengthOne(setList))
                    }
                }
            }
            firstTable = current
            if (areStatesEqual(previous, current as HashMap<String, Set<String>>)) {
                done = true
            } else {
                previous = current
                current = HashMap()
            }
        }
    }

    fun computeFollow() {
        var previous = HashMap<String, MutableSet<String>>()
        for (v in grammarReader.nonTerminals) {
            if (v == grammarReader.startingSymbol) {
                val auxSet: MutableSet<String> = HashSet()
                auxSet.add(epsilon)
                previous[v] = auxSet
            } else {
                previous[v] = HashSet()
            }
        }
        var current = HashMap<String, MutableSet<String>>()
        var done = false
        while (!done) {
            for (previousValue in previous.keys) {
                val auxSet = HashSet<String>()
                for (prevValue in previous[previousValue]!!) {
                    auxSet.add(prevValue)
                }
                current[previousValue] = auxSet
            }
            for (nonTerminal in grammarReader.nonTerminals) {
                val valueToMatch = Value(nonTerminal, false)
                val productions = grammarReader.productions
                    .filter { it.right.contains(valueToMatch) }
                    .toList()

                for (production in productions) {
                    var value = Value("", true)
                    var index = 0
                    while (value.value != valueToMatch.value && index < production.right.size) {
                        value = production.right[index]
                        index++
                    }
                    if (index < production.right.size) {
                        value = production.right[index]
                    }
                    if (value == valueToMatch) {
                        current[nonTerminal]!!.add(epsilon)
                    } else if (value.isTerminal) {
                        current[nonTerminal]!!.add(value.value)
                    } else {
                        current[nonTerminal]!!.addAll(previous[production.left.value]!!)
                        current[nonTerminal]!!.addAll(firstTable[value.value]!!)
                    }
                }
            }
            followTable = current
            if (areStatesEqual(previous as HashMap<String, Set<String>>, current as HashMap<String, Set<String>>)) {
                done = true
            } else {
                previous = current
                current = HashMap()
            }
        }
    }
}