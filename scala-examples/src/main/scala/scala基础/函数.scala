package scala基础

/**
  *
  * @author: wangjc
  *          2019/9/4
  */
object 函数 extends App{

    //匿名函数
    val a1 = (x: Int) => x+1
    a1(2) //3

    //部分应用，和Python的函数默认参数类似，但是你可以随意改变默认参数的值
    // 这样就能得到很多不同默认参数的函数了。
    val sum  = (a:Int,b:Int,c:Int) => a+b+c
    var s = sum(1,1,_:Int)
    s(1) //3
    s = sum(2,3,_:Int)
    s(1) //5

    //柯里化，分多次调用得到最后的结果
    def multiply(a:Int)(b:Int):Int = a * b
    val step1 = multiply(2) _
    val step2 = step1(3) //6
    println(step2)

    //变长参数
    def addup(nums: Int*) = {
        var res = 0
        for (num <- nums) res += num
        res
    }
    addup(1, 2, 3, 4, 5)

    /*
    在Scala中，定义函数时，如果函数体直接包裹在了花括号里面，而没有使用=连接，
    则函数的返回值类型就是Unit。这样的函数就被称之为过程，即过程就是没有返回值的函数。
    过程还有一种写法，就是将函数的返回值类型定义为Unit
    */
    def sayHello1(name: String) = "Hello, " + name//函数
    def sayHello2(name: String) { print("Hello, " + name)}//过程



}
