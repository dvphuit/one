package dvp.lib.jsengine.internal

import app.cash.quickjs.QuickJs
import dvp.lib.jsengine.JsType
import dvp.lib.jsengine.toJsType

internal class JsEngineImpl: JsEngine {
    private val engine by lazy { QuickJs.create() }

    @Volatile
    var isClosed = false
        private set

    override fun close() {
        if (isClosed) return
        engine.close()
        isClosed = true
    }

    override fun evaluate(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        if (isClosed) error(message = "Engine already closed")
        return try {
            runWithNewContext(context, script)
        } catch (e: Exception) {
            error(e)
        }
    }

    private fun runWithNewContext(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        val scriptContext = createContext(context) + script
        val result = engine.evaluate(scriptContext)
        return result.toJsType()
    }

    private fun createContext(context: Map<String, JsType>): String {
        if (context.isEmpty()) return ""
        return context.mapNotNull { pair ->
            pair.value.asString().let { "var ${pair.key} = $it;" }
        }.joinToString(separator = "")
    }

}