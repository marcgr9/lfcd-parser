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
            val sides = reader.nextLine().split("~")
            val left = sides[0]
            val right = sides[1].split(" ")
            productions.add(Production(left, right))
        }
    }

    fun parseProduction(line: String): Production {

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

    override fun toString(): String {
        return "G =( " + nonTerminals.toString() + ", " + terminals.toString() + ", " +
                productions.toString() + ", " + startingSymbol + " )"
    }
}