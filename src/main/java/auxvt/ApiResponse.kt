package auxvt

data class ApiResponse(val state: ApiState, val message: String)

fun message(message: String) = ApiResponse(ApiState.OK, message)

fun success(message: String) = ApiResponse(ApiState.SUCCESSFULL, message)

fun warning(message: String) = ApiResponse(ApiState.WARNING, message)

fun error(message: String) = ApiResponse(ApiState.ERROR, message)
