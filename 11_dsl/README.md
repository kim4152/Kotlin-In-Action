# 11장 영역 특화 언어(DSL, Domain-Specific Language)

### 깔끔한 API?

1. 읽는 사람들이 어떤 일이 벌어질지 명확히 이해할 수 있어야함.
2. 코드가 간결해야함.

외부 DSL : 독립적인 문법 구조를 가짐  
내부 DSL : 범용 언어로 작성됨

DSL과 일반 API의 경계는 없다.(주관적임)  
하지만 DSL에만 존재하는 한 가지 특징이 있다. 바로 구조 또는 문법이다.  
코틀린 DSL에서는 보통 람다를 중첩시키거나 메서드 호출을 연쇄시키는 방식으로 구조를 만든다.

- 문법 규칙   
  함수 이름 : 동사  
  함수 인자 : 명사

---

DSL 예시

```kotlin
fun myBuildString1(
    builderAction: (StringBuilder) -> Unit
): String {
    val sb = StringBuilder()
    builderAction(sb)
    return sb.toString()
}

// 매번 it을 사용해 StringBuilder 인스턴스를 참조해야함
val s1 = myBuildString1 {
    it.append("Hello ")
    it.append("World")
}
```

수신 객체 지정으로 it 생략 가능

```kotlin
fun myBuildString2(
    builderAction: StringBuilder.() -> Unit  // 수신 객체 지정 람다. 람다인자 중 하나에게 수신 객체라는 상태를 부여하면 이름과 마침표를 명시하지 않아도 그 인자의 멤버를 바로 사용할 수 있다.
): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

val s2 = myBuildString2 {
    append("Hello ")    // this 키워드 생략
    append("World")
}
```

아래처럼 apply를 이용해 간단하게도 표현 가능하다.

```kotlin
fun myBuildString3(builderAction: StringBuilder.() -> Unit): String = StringBuilder().apply(builderAction).toString()
```

### invoke 관례

함수처럼 호출할 수 있는 객체  
관례 : 특혈한 이름이 붙은 함수를 일반 메서드 호출 구문으로 호출하지 않고 더 간단한 구문으로 호출할 수 있게 지원하는 기능

invoke 동작 예제1

```kotlin
class Greeter(val greeting: String) {
    operator fun invoke(name: String) {
        println("$greeting, $name")
    }
}

val bavarianGreeter = Greeter("Servus")
fun a() {
    bavarianGreeter("Dmitry") // Greeter 인스턴스를 함수처럼 호출한다. 내부적으로 bavarianGreeter.invoke("Dmitry") 로 컴파일
// 결과 : Servus, Dmitry
}
```

// 동작 예제2

```kotlin
class DependencyHandler {
    fun compile(coordinate: String) = println("add dependency - $coordinate")

    operator fun invoke(body: DependencyHandler.() -> Unit) {
        body()
    }
}

fun main() {
    val dependencies = DependencyHandler()
    dependencies.compile("aaa")

    dependencies.invoke({ this.compile("ccc") })
    // 위 코드와 아래 코드는 같다.
    dependencies {
        compile("bbb")
    }
}
```

