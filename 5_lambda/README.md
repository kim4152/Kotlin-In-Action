# 5장: 람다

### 함수나 프로퍼티를 반혼하는 역할을 수행하는 람다는 멤버 참조로 대치할 수 있다

```kotlin
data class Person(val name: String, val age: Int)

val people = mutableListOf(Person("Alice", 3))

people.maxBy { it.age } // 람다
people.maxBy(Person::age) // 멤버 참조

```

## 멤버 참조

```kotlin
fun salute() = println("Salute!")

run(::salute) // 최상위 함수를 참조할 경우 클래스 이름을 생략하고 ::로 참조 시작

val createPerson = ::Person // 생성자 참조 -> Person 인스턴스를 만드는 동작을 값으로 저장
val p = createPerson("park", 3) // p = Person(name=park, age=3)
people.add(p)
```

### 확장 함수도 멤버 함수와 똑같은 방식으로 참조할 수 있다.

```kotlin
fun Person.isAudlut() = age >= 21
val predicate = Person::isAudlut
```

### count 와 size

size : 중간 컬렉션이 생김  
count : 조건을 만ㄴ족하는 원소의 개수만 추적

```kotlin
val numbers = listOf(1, 2, 3, 4, 5)
val evenNumbers = numbers.filter { it % 2 == 0 }
println(evenNumbers.size) // 결과: 2

val evenCount = numbers.count { it % 2 == 0 }
println(evenCount) // 결과: 2

```

## 지연 계산(lazy) 컬렉션 연산

map, filter 같은 몇 가지 컬렉션은 결과 컬렉션을 즉시 생성한다.   
이는 컬렉션 함수를 연쇄하면 매 단계마다 새로운 컬렉션이 생성된다.  
시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.

```kotlin
listOf(1, 2, 3, 4).asSequence()
    .map { print("map($it)"); it * it } // 중간 연산
    .filter { print("filter($it)"); it % 2 == 0 } //중간 연산
    .toList()       // 최종연산을 해야 내용이 출력됨
// 결과 :  map(1)filter(1)map(2)filter(4)map(3)filter(9)map(4)filter(16)
// 모든 연산이 각 원소에 대해 순차적으로 적용됨. 첫 번째 원소가 처리되고, 두 번째 원소가 처리


// filter 다음에 map을 하는 경우와, map을 한 다음에 filter를 하는 경우 결과는 같아도 수행해야하는 변환의 전체 횟수가 다를 수 있다.
people.asSequence().map(Person::name).filter { it.length < 4 }
people.asSequence().filter { it.name.length < 4 }.map(Person::name)
// 위 경우에서는 map을 먼저 하면 모든 원소를 변환하지만 filter를 먼저 하면 부적절한 원소를 먼제 제외한다.

```

## 수신 객체 지정 람다: with, apply

with : 반환하는 값은 람다 코드를 실행한 결과며, 그 결과는 람다 식의 본문에 있는 마지막 표현식의 결과  
apply : 항상 자신에게 전달된 객체(즉 수신 객체)를 반환

수신 객체 지정 람다는 DSL을 만들 때 매우 요용하다. DSL은 11장에서..

```kotlin
val with = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nend")
    toString()
}

val apply = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nend")
}.toString()

```

<details>
<summary>
컬렉션 vs 시퀀스 처리시간 비교해보기
</summary>

### 1. 원소가 개수가 적을 때 (1~1,000)

```kotlin
// 일반 컬렉션
val startTime = System.currentTimeMillis()
val memoryUsed = measureMemoryUsage {
    (1..1_000)
        .filter { it % 3 == 0 }
        .map { it * 2 }
        .toList()
}
val endTime = System.currentTimeMillis()
```

```kotlin
// 시퀀스 사용
val startTime = System.currentTimeMillis()
val memoryUsed = measureMemoryUsage {
    (1..1_000_000)
        .filter { it % 3 == 0 }
        .map { it * 2 }
        .toList()
}
val endTime = System.currentTimeMillis()
```

결과

```kotlin
일반 컬렉션 처리 시간 : 3 ms
        일반 컬렉션 메모리 사용량 : 424,560 bytes
        시퀀스 처리 시간: 7 ms
        시퀀스 메모리 사용량: 845,728 bytes  
```

### 2. 원소 개수가 많을 때 (1 ~ 100,000,000)

```kotlin
// 일반 컬렉션
(1..100_000_000)
    .filter { it % 3 == 0 }
    .map { it * 2 }
    .toList()
```

```kotlin
// 시퀀스
(1..100_000_000).asSequence()
    .filter { it % 3 == 0 }
    .map { it * 2 }
    .toList()

```

결과

```kotlin
일반 컬렉션 처리 시간 : 1166 ms
        일반 컬렉션 메모리 사용량 : 1,967,128,576 bytes
        시퀀스 처리 시간: 1013 ms
        시퀀스 메모리 사용량: 1,218,447,360 bytes  
```

결론

```
원소의 개수가 적을 때는 일반적인 컬렉션을 사용하는게 좋다!

지금은 각 원소마다 간단한 산술연산만 해주고 있지만, 복잡한 로직이 들어가면 더 유의미한 값이 출력될 것 같다.  
```

</details>
