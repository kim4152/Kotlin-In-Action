package `12_coroutine`

import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun now() = ZonedDateTime.now().toLocalDateTime().truncatedTo(ChronoUnit.MILLIS)
fun log(msg: String) = println("${now()}: ${Thread.currentThread()}: ${msg}")

fun yieldExample() {
    runBlocking {
        launch {
            log("1")
            yield()
            log("3")
            yield()
            log("5")
        }

        log("After first launch")

        launch {
            log("2")
            delay(1_000)
            log("4")
            delay(1_000)
            log("6")
        }
        log("After second launch")
    }
}
/**
결과
2024-12-20T16:19:47.928: Thread[#1,main,5,main]: After second launch
2024-12-20T16:19:47.929: Thread[#1,main,5,main]: 1
2024-12-20T16:19:47.929: Thread[#1,main,5,main]: 2
2024-12-20T16:19:47.932: Thread[#1,main,5,main]: 3
2024-12-20T16:19:47.932: Thread[#1,main,5,main]: 5
2024-12-20T16:19:48.938: Thread[#1,main,5,main]: 4
2024-12-20T16:19:49.944: Thread[#1,main,5,main]: 6
 */
/**
특징
1. launch는 즉시 반환된다.
2. runBlocking은 내부 코루틴이 모두 끝난 다음에 반환된다.
3. delay()를 사용한 코루틴은 그 시간이 지날 때까지 다른 코루틴에게 실행을 양보한다.
 */


/*
async
launch와 같은 일을 한다.
유일한 차이는 launch가 Job을 반환하는 반면, async는 Defferred를 반환한다.
심지어 Deferred는 Job을 상속한 클래스이기 때문에 launch 대신 async를 사용해도 항상 아무 문제가 없다.
 */

fun main() {
    yieldExample()
    runBlocking {
        async { }
    }
}
