# 12장 코루틴과 Async/ Await

함수 = 서브루틴 : 여러 명령을 모아 이름을 부여해서 반복 호출할 수 있게 정의한 것.  
코루틴 = 서로 협력해서 실행을 주고받으면서 작동하는 여러 서브루틴

### launch

코루틴을 Job으로 반환하며, 만들어진 코루틴은 기본적으로 즉시 실행된다.  
원하면 launch가 반환한 Job의 cancel()을 호출해 코루틴 실행을 중단시킬 수 있다.  
launch가 작동하려면 CoroutineScope 객체가 블록의 this로 지정돼야 한다.

```kotlin
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
```

```kotlin
결과  1->2->3->5->4->6
```

특징

1. launch는 즉시 반환된다.
2. runBlocking은 내부 코루틴이 모두 끝난 다음에 반환된다.
3. delay()를 사용한 코루틴은 그 시간이 지날 때까지 다른 코루틴에게 실행을 양보한다.

### async

launch와 같은 일을 한다.  
유일한 차이는 launch가 Job을 반환하는 반면, async는 Defferred를 반환한다.  
심지어 Deferred는 Job을 상속한 클래스이기 때문에 launch 대신 async를 사용해도 항상 아무 문제가 없다.

```kotlin
// launch
public fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val newContext = newCoroutineContext(context)
    val coroutine = if (start.isLazy)
        LazyStandaloneCoroutine(newContext, block) else
        StandaloneCoroutine(newContext, active = true)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```

```kotlin
// async
public fun <T> CoroutineScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    val newContext = newCoroutineContext(context)
    val coroutine = if (start.isLazy)
        LazyDeferredCoroutine(newContext, block) else
        DeferredCoroutine<T>(newContext, active = true)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```

Deferred와 Job 차이

1. Job은 아무 타입 파라미터가 없는데, Deferred는 타입 파라미터가 있는 제네릭 타입.
2. Deferred 안에는 await() 함수가 정의돼 있다 `public suspend fun await(): T`

Job은 Unit을 돌려주는 Deferred<Unit>이라고 생각할 수도 있다.

---

launch, async 등은 모두 CoroutineScope의 확장함수이다.  
그런데 CoroutineScope 에는 CoroutineContext 타입의 필드 하나만 들어있다.

```kotlin
public interface CoroutineScope {
    public val coroutineContext: CoroutineContext
}
```

사실 CoroutineScope 는 CoroutineContext 필드를 launch 등에 CoroutineContext 를 넘길 수도 있다는 점에서  
실제로 CoroutineContext가 코루틴 실행에 더 중요한 의미가 있음을 유추할 수 있다.  
CouroutineContext는 실제로 코루틴이 실행 중인 여러 작업과 디스패처를 저장하는 일종의 맵이라고 할 수 있다.  
전달하는 컨텍스트에 따라 서로 다른 스레드상에서 코루틴이 실행된다.

---

kotlinx-coroutines-core 모듈의 최상위에 정의된 일시 중단 함수들

- withContext : 다른 컨텍스트로 코루틴을 전환한다.
- withTimeout : 코루틴이 정해진 시간 안에 실행되지 않으면 예외를 발생시킴
- withTimeoutOrNull : 위와 비슷하지만 null을 결과로 돌려줌
- awithAll : 모든 작업의 성공을 기다린다. 작업 중 어느 하나가 예외로 실패하면 awaitAll도 그 예외로 실패한다.
- joinAll : 모든 작업이 끝날 때까지 현재 작업을 일시 중단시킨다.
