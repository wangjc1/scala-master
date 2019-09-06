package scala进阶

/**
  * @see https://blog.csdn.net/Luomingkui1109/article/details/82881684
  */
object 专题$高阶函数 extends  App {

    /**** 1. 函数作为参数  ***/
    //函数作为一个变量传入到了另一个函数中，那么该作为参数的函数的类型是：function1，即：（参数类型） => 返回类型
    def plus(x: Int) = 3 + x
    Array(1, 2, 3, 4).map(plus(_)) //4-5-6-7

    /*** 匿名函数 **/
    //即没有名字的函数，可以通过函数表达式来设置匿名函数。
    //即没有名字的函数，可以通过函数表达式来设置匿名函数。
    val triple = (x: Int) => 3 * x

    /*** 能够接受函数作为参数的函数，叫做高阶函数。 ***/
    //能够接受函数作为参数的函数，叫做高阶函数。
    // （1）高阶函数的使用
    def highOrderFunction1(f: Double => Double) = f(10) //Double => Double 匹配到 一个Double参数返回类型也是Double的任意方法
    def minus7(x: Double) = x - 7
    highOrderFunction1(minus7)

    // （2）高阶函数同样可以返回函数类型
    def minusxy(x: Int) = (y: Int) => x - y
    minusxy(3)(5)

    /*** 参数（类型）推断 ***/
    // 传入函数表达式
    highOrderFunction1((x: Double) => 3 * x)
    // 参数推断省去类型信息
    highOrderFunction1((x) => 3 * x)
    // 单个参数可以省去括号
    highOrderFunction1(x => 3 * x)
    // 如果变量旨在=>右边只出现一次，可以用_来代替
    highOrderFunction1(3 * _)

    /*** 闭包 ****/
    //闭包就是一个函数把外部的那些不属于自己的对象也包含(闭合)进来。
    //（1）匿名函数(y: Int) => x -y嵌套在minusxy函数中。
    //（2）匿名函数(y: Int) => x -y使用了该匿名函数之外的变量x
    //（3）函数minusxy返回了引用了局部变量的匿名函数
    //此处f1,f2这两个函数就叫闭包，闭包大概意思就是将局部变量限定在某个域中同时还能访问外部的变量
    val f1 = minusxy(10)
    val f2 = minusxy(10)
    f1(3) + f2(3)


    /*** 柯里化 ***/
    //函数编程中，接受多个参数的函数都可以转化为接受单个参数的函数，这个转化过程就叫柯里化，柯里化就是证明了函数只需要一个参数而已。其实我们刚才的学习过程中，已经涉及到了柯里化操作，所以这也印证了，柯里化就是以函数为主体这种思想发展的必然产生的结果。
    //（1）柯里化示例
    def mul(x: Int, y: Int) = x * y
    mul(10, 10)

    def mulCurry(x: Int) = (y: Int) => x * y
    mulCurry(10)(9)

    def mulCurry2(x: Int)(y:Int) = x * y
    mulCurry2(10)(8)

    //（2）柯里化的应用
    // 比较两个字符串在忽略大小写的情况下是否相等，注意，这里是两个任务：
    //  ① 全部转大写（或小写）
    //  ② 比较是否相等
    // 针对这两个操作，我们用一个函数去处理的思想，其实无意间也变成了两个函数处理的思想。示例如下：
    val a = Array("Hello", "World")
    val b = Array("hello", "world")
    a.corresponds(b)(_.equalsIgnoreCase(_))


    /*** 7.控制抽象 ***/
    // 控制抽象是一类函数：
    // 参数是函数。
    //函数参数没有输入值也没有返回值。
    //（1）使用示例，可以把线程封装到一个函数中，直接调用方法即可创建一个线程并运行
    def runInThread(f1: () => Unit): Unit = {
        new Thread {
            override def run(): Unit = {
                f1()
            }
        }.start()
    }
    //调用runInThread方法并创建线程
    runInThread(() => {
        println("run in thread....")
        Thread.sleep(5000)
        println("done！")
    })

    //（2）进阶用法：实现类似while的until函数
    def until(condition: => Boolean)(block: => Unit): Unit = {
        if(!condition) {
            block
            until(condition)(block)
        }
    }
    var x=10
    //小提示：对于一个类似柯里化的方法 until()()，调用时最后一个参数可以用{},就变成了 until(){}
    until(x==0){
        x -= 1
        println(x)
    }
}
