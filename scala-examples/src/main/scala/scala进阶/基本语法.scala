package scala进阶

/**
  * https://www.jianshu.com/p/32b5d65226b8
  * https://blog.csdn.net/u010159842/article/details/77644414
  *
  */
object 基本语法 extends App { //继承了App就不用定义main方法了  def main(args: Array[String]) {}

    val name = "小牛学堂"
    val pirce = 998.88d
    val url = "www.edu360.com"
    // 普通输出
    println("name=" + name, "pirce=" + pirce, "url=" + url)
    // 文字'f'插值器允许创建一个格式化的字符串，类似于 C语言中的 printf。
    // 在使用'f'插值器时，所有变量引用都应该是 printf 样式格式说明符，如％d，％i，％f 等
    // 这里$name％s打印 String 变量 James 和$height％2.2f打印浮点值 1.90。
    println(f"$name%s 学费 $pirce%1.2f, 网址是$url") // 该行输出有换行
    printf("%s 学费 %1.2f, 网址是%s", name, pirce, url) // 该行输出没有换行

    // 's'允许在处理字符串时直接使用变量。
    // 在 println 语句中将 String 变量($name)附加到普通字符串中。
    println(s"name=$name, pirce=$pirce, url=$url")
    // 字符串插入器还可以处理任意表达式。
    // 使用's'字符串插入器处理具有任意表达式(${1 + 1})的字符串(1 + 1)的以下何表达式都可以嵌入到${}中。
    println(s"1 + 1 = ${1 + 1}") // output: 1 + 1 = 2

    //to不包含结尾
    for(i<- 1 to  10 if i%2==0){
        println(i)
    }

    //until包含结尾
    for(i<- 1 until  10 if i%2==0){
        println(i)
    }

    //循环中任意多个变量
    for(i <- 1 to 3; from = i-1; j = from to 3) println(i,from,j)

    //yield 有点类似python中的x=[i+1 for i in range(10)]
    val x= for(i<- 1 until  10 if i%2==0) yield i+1
    println(x) //Vector(3, 5, 7, 9)

    //switch语句，有常量模式，变量模式，构造函数模式，序列模式，元组模式，或者类型模式
    val i = 1
    //使用val x =  (i: @switch) match {...} 语句@switch可以检查是否生成跳转表，否则给出警告，这样更高效
    val n = i match {
        case 0 => "one"
        case 1 => "two"
        case _ => "any"
    }
    println(n)

    def mycase(x: Any): String = x match {
        case 0 => "zero"
        case true => "true"
        case "hello " => "hello wuzheng"
        case Nil => "空数组"
        case List(0, _, _) => "三个元素的序列，其中第一个为0"
        case List(1, _*) => "第一个元素为1序列"
        case s: String => s"输入的值是 String : $s"
        case i: Int => s"输入的是整数： $i"
        case _ => "我不知道了"
    }
    println(mycase(2))
    println(mycase(List(0,1,2)))
    println(mycase(List()))

    //异常处理，异常匹配
    try{
        2/0
    }catch {
        case e:Exception => println(e.getMessage)
    }

    val v = if(2>1) 1 else 0
    val msg = if(v>0) "OK" else -1 //混合类型表达式

    var m = 10
    while(m > 0) {
        println(m)
        m -= 1
    }

    import scala.util.control.Breaks._
    breakable {
        var n = 10
        for(c <- "Hello World") {
            if(n == 5) break;
            print(c)
            n -= 1
        }
    }

}
