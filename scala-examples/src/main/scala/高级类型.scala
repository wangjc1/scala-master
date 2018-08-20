import scala.collection.mutable.ArrayBuffer
import java.awt.event.ActionEvent
import java.io.File

/**
  *
  * https://blog.csdn.net/lovehuangjiaju/article/details/47381601
  */
object 高级类型 {
  def main(args: Array[String]): Unit = {

    //18.5 结构化类型
    //通过new {结构体} 的方式作为参数传入，有点像匿名实现接口
    appendLines(new { def append(s:String) = println(s) },List("wang","Alex"))
    //可以定义一个包含append方法的对象或类作为参数传入
    appendLines(Buffer/*或者 new Buffer*/,List("wang","Alex"))


    //18.9 类型存在
    //forSome是Scala的一个关键字，不是函数。
    /*另外可以指定某些类型,类似于Java中的
    class MyClass<? extends Integer> {
      ...
    }*/
    //用forSome定义一个“?”
    def addToFirst(x : Array[T] forSome {type T <: Integer}) = x(0) + 1
    //一种更简洁的写法：
    def addToFirst2(x: Array[_ <: Integer ]) = x(0) + 1

    //18.9 方法和函数
    //scala中方法和函数区别：https://blog.csdn.net/qq_29343201/article/details/56282779
    //类似(x: Int)Int这种在scala中表示一个方法,函数一般表示为b: Int => Int
    def a(x:Int) = x*x //a: (x: Int)Int
    //通过 a _ 操作符把一个方法转换成函数，真是奇葩
    a _ //res0: Int => Int = <function1>
    //定义一个函数，函数也可以 b _ 这么调用，转换成了另一个函数
    val b = (x:Int) => x*x //b: Int => Int = <function1>

    //18.9 依赖注入和蛋糕模式
    //蛋糕模式（cake pattern）是Scala实现依赖注入的方法之一，它利用Scala Mixin功能，让对象被创建时，才把相依的原件，透过Mixin的方式绑在一起。
    val ctx = new {} with MyContext with InMemoryConfig {
    }
    println (ctx.welcome)

    //18.10 自行类型
    //val file = new File("/user") with LoggedException //File不是Exception的子类
    val re = new RuntimeException() with LoggedException //RuntimeException是Exception的子类

    //18.12 抽象类型
    //在实现类中指定具体的类型
    val reader = new StringReader()
    println(reader.name)

    //18.13 家族多态


  }

  /**
    * 结构化类型： 类似这种{def method:Any{}}被称为结构化类型，有点像个匿名接口
    */
  def appendLines(target: { def append(str: String): Any }, lines: Iterable[String]) {
    for (l <- lines) { target.append(l); target.append("\n") }
  }
  //包含一个append方法的对象
  object Buffer{
    def append(s:String) = println(s)
  }
  //包含一个append方法的类
  class Buffer{
    def append(s:String) = println(s)
  }

  /**
    * 自身类型：指明混入LoggedException特质的类必须是Exception的子类
    */
  trait LoggedException {
    this: Exception =>
    def log(): Unit = {
      println("Please check errors.")
    }
  }

  /**
    * 依赖注入
    */
  trait Config {
    load
    val text: String
    def load: Unit
  }
  trait InMemoryConfig extends Config {
    lazy val text = "Hello"
    def load = println("load: " + text)
  }
  trait Context
  trait MyContext extends Context {
    this: Config =>
    def welcome = this.text
  }

  //抽象类型
  trait Reader {
    //特质中无需指定具体类型
    type Contents
  }
  class StringReader extends Reader {
    //在实现中指定Contents的具体类型参数
    type Contents = String
    val name:Contents = "Action Scala"
  }

  //家族多态
  trait Listener[E] {
    def occurred(e:E) :Unit
  }

  trait Source[E,L <: Listener[E]]{
    private val listeners = new ArrayBuffer[L]
    def add(l:L) {listeners += l}
    def remove(l:L) {listeners -= l}
    def fire(e:E): Unit = {
      for(l <- listeners) l.occurred(e)
    }
  }

  trait ActionListener extends Listener[ActionEvent]
  class Button extends Source[ActionEvent,ActionListener]{
    def click(): Unit ={
      fire(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"click"))
    }
  }

}
