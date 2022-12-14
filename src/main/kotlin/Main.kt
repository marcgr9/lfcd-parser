import java.util.Scanner

fun main() {
    val grammar = Grammar("src/main/resources/g1.txt")
    val parser = Parser(grammar)

    val scanner = Scanner(System.`in`)

    println(menu())
    while (true) {
        when (scanner.nextInt()) {
            0 -> println(menu())
            1 -> println(grammar.nonTerminals)
            2 -> println(grammar.terminals)
            3 -> println(grammar.productions)
            4 -> {
                print("Nonterminal: ")
                grammar.getProductionsForNonterminal(scanner.next()).forEach(::println)
            }
            5 -> {
                parser.computeFirst()
                parser.firstTable.forEach(::println)
            }
            6 -> {
                parser.computeFollow()
                parser.followTable.forEach(::println)
            }
        }
    }
}

fun menu(): String
    = """
        1. Non terminals
        2. Terminals
        3. Productions
        4. Production for terminal
        5. First
        6. Follow
    """.trimIndent()