import java.util.Scanner

fun main() {
    val grammar = Grammar("src/main/resources/g3.txt")
    val parser = Parser(grammar)
    val lr0Parser = LR0Parser(grammar)
    lr0Parser.createCanonicalCollection()
    lr0Parser.createTables()

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
            7 -> println(lr0Parser.goToTable)
            8 -> println(lr0Parser.actionTable)
            9 -> println(lr0Parser.canonicalCollection)
            10 -> {
                print("Input: ")
                lr0Parser.parse(scanner.next()) // aabb$
                lr0Parser.result.forEach { println(it) }
            }
        }
    }
}

fun menu(): String
    = """
        0. Menu
        1. Non terminals
        2. Terminals
        3. Productions
        4. Production for terminal
        5. First
        6. Follow
        7. GoTo table
        8. Action table
        9. Canonical collection
        10. Parse
    """.trimIndent()
