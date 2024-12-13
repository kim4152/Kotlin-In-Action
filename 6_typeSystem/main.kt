/*
Nothing 타입 :  어떠한 값도 포함하지 않는 타입

결코 성공적으로 값을 돌려주는 일이 없으므로 반환 값이라는 개념 자체가 의미 없을 때 사용.
예 : 예외 처리 함수, 반환값 없는 무한 반복문
 */

// Nothing 타입은 아무 값도 포함하지 않는다. 따라서 Nothing은 함수의 반환타입이나 반환 타입으로 쓰일 타입 파라미터로만 쓸 수 있다.
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}


/*
Unit vs Nothing

Unit : return 동작은 하지만 아무것도 return하지 않겠다는 의미
Nothing : return 동작 자체를 하지 않겠다는 의미
 */
fun a(): Unit {
    return Unit // 생략
}

fun b(): Nothing {
    return Nothing  // 오류
}

/*
읽기 전용 컬렉션이 항상 스레드 안전하지는 않다.

아래와같이  동일한 컬렉션 객체를 가리키는 읽기 전용 컬렉션 타입의 참조와 변경 가능한 컬렉션 타입의 참조가 있을 수 있다.
따라서 읽기 전용 컬렉션이 항상 스레드 안전하지는 않다.
.toList() 를 사용해 불변 복사본을 생성하는 것도 한 가지 방법이다.
 */
fun main() {
    val mutableList: MutableList<String> = mutableListOf("A", "B", "C")
    val readOnlyList: List<String> = mutableList

    mutableList.add("D")
    println(readOnlyList) // [A, B, C, D]
}
