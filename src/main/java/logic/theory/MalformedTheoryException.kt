package logic.theory

data class MalformedTheoryException(override val message: String) : Exception(message)
