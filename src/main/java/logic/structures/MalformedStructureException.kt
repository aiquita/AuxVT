package logic.structures

data class MalformedStructureException(override val message: String) : Exception(message)
