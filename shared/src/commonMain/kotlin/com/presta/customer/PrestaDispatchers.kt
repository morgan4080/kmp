import kotlinx.coroutines.CoroutineDispatcher

interface PrestaDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

expect val prestaDispatchers: PrestaDispatchers