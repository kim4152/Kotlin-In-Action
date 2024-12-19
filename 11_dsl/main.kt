package `11_dsl`

/*
깔끔한 API?
1. 읽는 사람들이 어떤 일이 벌어질지 명확히 이해할 수 있어야함.
2. 코드가 간결해야함.
 */

/*
외부 DSL : 독립적인 문법 구조를 가짐
내부 DSL : 범용 언어로 작성됨
 */

/*
DSL과 일반 API의 경계는 없다.(주관적임)
하지만 DSL에만 존재하는 한 가지 특징이 있다. 바로 구조 또는 문법이다.
코틀린 DSL에서는 보통 람다를 중첩시키거나 메서드 호출을 연쇄시키는 방식으로 구조를 만든다.
 */

/*
문법 규칙
함수 이름 : 동사
함수 인자 : 명사
 */


fun myBuildString(
    builderAction: (StringBuilder) -> Unit
): String {
    val sb = StringBuilder()
    builderAction(sb)
    return sb.toString()
}

val s = myBuildString {
    it.append("Hello ")
    it.append("World")
}
