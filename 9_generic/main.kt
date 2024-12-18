package `9_generic`

/*
제니릭스
 */


/*
자바와 달리 코틀린에서는 제네릭 타입의 타입 인자를 프로그래머가 명시하거나 컴파일러가 추론할 수 있어야 한다.
 */

/*
타입 파라미터 제약 : 타입 인자를 제한하는 기능
어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한으로 지정
T: Number,   T: Comparable<T>
 */
fun <T : Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}


/*
where : 타입 인자가 반드시 해당 클래스, 인터페이스 구현해야함
 */
open class AA
interface BB

fun <T> List<T>.sum() where T : AA, T : BB {}


/*
null이 될 수 없는 타입으로 한정
 */
// null이 가능한 T
class Processor<T> {
    fun process(value: T) {
        value.hashCode()
    }
}

fun a() {
    val processor = Processor<String?>()
    processor.process(null)
}

//null이 불가능한 T -> Any를 상한으로 사용
class ProcessorA<T : Any>


/*
실행 시 제네릭스 동작
*/
/*
1. JVM의 제네릭스는 보통 타입 소거를 사용해 구현된다.
(컴파일러가 제네릭 타입 정보를 컴파일 시점에만 사용하고 런타임에는 해당 정보를 제거한다는 뜻.
이를 통해 제네릭스는 바이트코드 레벨에서 런타임 타입 안정성을 보장하면서도 기존의 JVM 및 바이트코드와 호환성을 유지할 수 있다.)
예를 들어 List<String> 객체를 만들고 그 안에 문자열을 여럿 넣더라도 실행 시점에는 그 객체를 오직 List로만 볼 수 있다. 그 List 객체가 어떤 타입의 원소를 저장하는지 실행 시점에는 알 수 없다.

장점 : 저장해야 하는 타입 정보의 크기가 줄어들어서 메모리 사용량이 줄어든다.
*/
// 컴파일러는 두 리스트를 서로 다른 타입으로 인식하지만 실행 시점에 그 둘은 완전히 같은 타입의 객체다.
val list1 = listOf("a", "b")
val list2 = listOf(1, 2, 3)


/*
2. 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다. -> 실체화(refly)
 */
fun <T> isA(value: Any) = value is T  // 런타임에 T가 어떤 타입인지 알 수 없음
inline fun <reified T> isB(value: Any) = value is T


/*
변성: List<String.와 List<Any> 와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념
 */

/*
변성이 있는 이유
List<Any> 타입의 파라미터를 받는 함수에 Lis<String>을 넘기면 안전하지 않다.
하지만 원소 추가나 변경이 없는 경우에는 List<String>을 List<Any> 대신 넘겨도 안전하다.
 */

/*
공변적 : A가 B의 하위 타입이면 List<A>는 List<B>의 하위타입이다. 그런 클래스나 인터페이스를 공변적이라고 말한다.
 */
interface producer<out T> { // out 선언 : 클래스가 T에 대해 공변적이라고 선언. producer가 T의 하위클래스
    fun produce(t: T): T // T를 아웃 위치(두 번째 위치. 즉 리턴값)에서만 사용할 수 있다.
}


/*
반공변성
타입 A가 타입 B의 상위 타입일 때 producer<B>가 producer<A>의 상위타입이 된다.
 */
// InA는 InB의 상위 타입. InA를 받을 수 있는 메서드는 InB도 받을 수 있어야함
open class InA
class InB : InA()

// 반공변적 인터페이스
interface Base<in T> {
    fun base(item: T)
}

fun makeBase(base: Base<InA>) {}

fun main() {
    val baseInA: Base<InA> = object : Base<InA> {
        override fun base(item: InA) {}
    }
    val baseInB: Base<InB> = object : Base<InB> {
        override fun base(item: InB) {}
    }
    makeBase(baseInA)
    makeBase(baseInB) // 컴파일 오류
    /* InA는 InB의 상위 타입이어서 base<InA>를 받을 수 있는 메서드는 base<InB>도 받을 수 있어야 하지만
    base<InB>가 base<InA>의 상위 타입이 되었기 때문에(반공변성) 오류가 난다
     */
}


/*
스타 프로젝션 (*)

MutableList<Any?> : 모든 타입의 원소를 담을 수 있는 리스트
MutableList<*>    : 어떤 정해진 구체적인 타입의 원소만 담는 리스트지만 그 원소의 타입을 정확히 모른다는 표현
*은 Any?의 하위 타입. Any?는 코틀린에서 모든 타입의 상위 타입.
 */
