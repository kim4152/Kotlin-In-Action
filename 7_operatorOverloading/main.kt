package `7_operatorOverloading`

import kotlin.reflect.KProperty

/*
위임 프로퍼티
https://medium.com/@l2hyunwoo/by%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-delegate-456397c08a59

위임 : 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴. ( 이때 작업을 처리하는 도우미 객체를 위임 객체라고 부른다)
 */

class Delegate() {
    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Any {
        return ""
    }
}

fun a() {
    val delegate by Delegate()
}

// by lazy() 를 사용한 프로퍼티 초기화 지연

class Person() {
    var email: String? = null
        get() {
            if (email == null) {
                email = getEmaill()
            }
            return email
        }

    fun getEmaill(): String = ""

    val name by lazy { "" }

}


/*
위 방식은 email이 null일 경우 가져오고, null이 아닐경우 기존 email을 반환한다
하지만 위 방식은 스레드 안전하지 않고 번거롭다.
그래서 by lazy를 사용한다.
 */
