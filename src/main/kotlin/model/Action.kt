package model

open class Action { }

data class Shift(val stateID: Int) : Action() {
    override fun toString(): String {
        return "S$stateID"
    }
}

data class Reduce(val prodID: Int) : Action() {
    override fun toString(): String {
        return "R$prodID"
    }
}

class Accept() : Action() {
    override fun toString(): String {
        return "accept"
    }
}