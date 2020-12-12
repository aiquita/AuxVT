package logic.signature

data class MalformedSignatureException(override val message: String) : Exception(message)
