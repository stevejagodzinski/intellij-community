// "Propagate '@MyExperimentalAPI' annotation to 'bar'" "false"
// COMPILER_ARGUMENTS: -Xopt-in=kotlin.RequiresOptIn
// WITH_STDLIB
// ACTION: Propagate '@MyExperimentalAPI' annotation to containing class 'Bar'
// ACTION: Opt-in for 'MyExperimentalAPI::class' on 'bar'
// ACTION: Opt-in for 'MyExperimentalAPI::class' on containing class 'Bar'
// ACTION: Opt-in for 'MyExperimentalAPI::class' on containing file 'basicFunctionNotApplicable.kt'
// ACTION: Add '-Xopt-in=MyExperimentalAPI' to module light_idea_test_case compiler arguments
// ERROR: This declaration is experimental and its usage must be marked with '@MyExperimentalAPI' or '@OptIn(MyExperimentalAPI::class)'
// ERROR: This declaration is experimental and its usage must be marked with '@MyExperimentalAPI' or '@OptIn(MyExperimentalAPI::class)'
// ERROR: This annotation is not applicable to target 'member function'

@RequiresOptIn
@Target(AnnotationTarget.CLASS)
annotation class MyExperimentalAPI

@MyExperimentalAPI
class Some {
    @MyExperimentalAPI
    fun foo() {}
}

class Bar {
    fun bar() {
        Some().foo<caret>()
    }
}
