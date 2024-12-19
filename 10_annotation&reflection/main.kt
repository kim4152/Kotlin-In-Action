package `10_annotation&reflection`

import kotlin.reflect.full.memberProperties

/*
리플렉션 : 실행 시점에 (동적으로) 객체의 프로퍼티와 메서드에 접근할 수 있게 해주는 방법.
보통 컴파일 단계에서 구체적으로 선언한 메서드나 프로퍼티 이름을 사용하지만 오직 실행 시점에만 알 수 있는 경우가 있다.
예: JSON 직렬화 라이브러리 -> 실행 시점이 되기 전까지는 라이브러리가 직렬화할 프로퍼티나 클래스에 대한 정보를 알 수 없음.
 */

/*
코틀린 리플렉션 API 종류
1. java.lang.reflect (표준 리플렉션) : 코틀린 클ㄹ래스를 컴파일한 바이트코드를 완벽히 지원
2. kotlin.reflect : 자바에는 없는 프로퍼티나 널이 될 수있는 타입과 같은 코틀린 고유 개념에 대한 리플렉션을 제공
   하지만 자바 리플렉션 API를 완전히 대체할 수 있는 복잡한 기능을 제공하지는 않는다. (jar파일 추가해야함)
 */

class Person(val name: String, val age: Int)


//KClass
fun a() {
    val person = Person("Alice", 20)
    val kClass =
        person.javaClass.kotlin // KClass : 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업 가능. 클래스::clss 로도 가능
    println(kClass.simpleName) // Person
    println(kClass.memberProperties.forEach { println(it.name) }) // age, name, kotlin.Unit
}


//KFunction
/**
KCallable : 함수와 프로퍼티를 아루르는 공통 상위 인터페이스
public fun call(vararg args: Any?): R // KCallable 안에있는 메서드. 함수나 프로퍼티의 게터를 호출할 수 있다.
 */
fun foo(x: Int) = println(x)
fun b() {
    val kFunc = ::foo // 타입: KFunction1<Int,Unit> -> 1은 파라미터 수, Int -> 파라미터 타입, Unit -> 반환 타입
    kFunc.call(42) // 리플렉션이 제공하는 call을 사용해 함수를 호출할 수 있음
    kFunc.invoke(42)  // invoke를 사용하면 인자 개수나 타입이 맞아 떨어지지 않으면 컴파일이 안된다.(추천)
}

// KProperty
fun main() {
    val person = Person("Alice", 20)
    val memberProperty = Person::age // 멤버프로퍼티에 프로퍼티 참조 저장. 타입 : KProperty<Person,Int>. 첫 번째: 수신 객체 타입, 두 번째: 프로퍼티 타입
    println(memberProperty.get(person)) // 20
}
