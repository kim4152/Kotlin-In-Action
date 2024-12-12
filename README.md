# Kotlin-In-Action

[코틀린 인 액션] 책 읽고 정리하기

# 궁금한 내용 실습해보기

## 5장

<details><summary>
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
