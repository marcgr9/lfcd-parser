import model.Production
import model.Value
import java.io.File
import java.util.*

class Grammar(
    private val file: String,
) {

    val nonTerminals: MutableList<String> = mutableListOf()
    val terminals: MutableSet<String> = mutableSetOf()
    val productions: MutableList<Production> = mutableListOf()

    lateinit var startingSymbol: String

    init {
        grammarFromFile()
//        val augmentedStart = "$startingSymbol'"
//        nonTerminals.add(augmentedStart)
//        productions.add(0, Production(Value(augmentedStart, true), listOf(Value(startingSymbol, true))))
    }

    private fun grammarFromFile() {
        val file = File(file)
        val reader = Scanner(file)
        val nonTerminalsInput = reader.nextLine().split(" ")
        nonTerminalsInput.forEach { nonTerminals.add(it) }
        val terminalsInput = reader.nextLine().split(" ")
        terminalsInput.forEach { terminals.add(it) }
        startingSymbol = reader.nextLine()
        while (reader.hasNextLine()) {
            productions.add(parseProduction(reader.nextLine()))
        }

    }

    fun parseProduction(line: String): Production {
        val splitLine = line.split("~")
        val left = Value(splitLine[0].replace("]", "").replace("[", ""), true)

        val right = mutableListOf<Value>()

        val rightPart = splitLine[1]
        var index = 0
        var current = ""
        while (index <= rightPart.length) {
            if (index == rightPart.length) {
                if (rightPart[index - 1] == ']') break

                if (current.isNotEmpty()) {
                    right.add(Value(current, true))
                }
            } else if (rightPart[index] == '[') {
                if (current.isNotEmpty()) {
                    right.add(Value(current, true))
                }
                current = ""
            } else if (rightPart[index] == ']') {
                if (current.isEmpty()) continue

                right.add(Value(current, false))
                current = ""
            } else {
                current += rightPart[index]
            }

            index++
        }

        return Production(left, right)
    }

    fun getProductionsForNonterminal(nonterminal: String): List<Production> {
        return productions.filter {
            it.left.value == nonterminal
        }
    }

    fun isNonTerminal(symbol : String) : Boolean {
        return nonTerminals.contains(symbol)
    }

    fun checkCFG(): Boolean {
        return productions.stream().allMatch {
            it.left.value.length == 1 && nonTerminals.contains(it.left.value)
        }
    }

}