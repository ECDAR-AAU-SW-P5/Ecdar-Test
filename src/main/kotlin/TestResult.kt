import EcdarProtoBuf.QueryProtos.QueryResponse.ResultCase
import EcdarProtoBuf.QueryProtos.QueryResponse.ResultCase.*
import com.beust.klaxon.Json
import tests.SingleTest

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

enum class ResultType {
    SATISFIED,
    UNSATISFIED,
    EXCEPTION,
    NON_EXCEPTION;

    companion object {
        fun from(result: Boolean): ResultType {
            return when (result) {
                true -> SATISFIED
                false -> UNSATISFIED
            }
        }

        fun from(result: ResultCase): ResultType {
            return when (result) {
                SUCCESS,
                COMPONENT,
                REACHABILITY,
                IMPLEMENTATION -> SATISFIED
                CONSISTENCY,
                DETERMINISM,
                REFINEMENT,
                REACHABILITY_PATH,
                PARSING_ERROR,
                MODEL,
                ERROR,
                COMPONENTSNOTINCACHE,
                SYNTAX,
                RESULT_NOT_SET -> UNSATISFIED
            }
        }
    }

    fun colored(): String {
        return when (this) {
            SATISFIED -> "${ANSI_GREEN}SATISFIED$ANSI_RESET"
            UNSATISFIED -> "${ANSI_CYAN}UNSATISFIED$ANSI_RESET"
            EXCEPTION -> "${ANSI_RED}EXCEPTION$ANSI_RESET"
            NON_EXCEPTION -> "${ANSI_PURPLE}NON_EXCEPTION$ANSI_RESET"
        }
    }
}

class TestResult(
    @Json(index = 7) val test: SingleTest,
    @Json(index = 4) val result: ResultType,
    @Json(index = 5) val expected: ResultType,
    @Json(index = 8) val inner: List<TestResult>,
) {
    @Json(index = 3) var time: Double? = null
    @Json(index = 6) var exception: String? = null
}
