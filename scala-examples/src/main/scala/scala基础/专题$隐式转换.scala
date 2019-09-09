package scala基础

import java.io.File
import scala.collection.script.Index


/**
  * https://blog.csdn.net/ab748998806/article/details/78046045
  *  引入隐式转换：
  * 1. 需要一个增强的类，里面提供我们想要的方法，接收的参数的类型一定要是被增强类的类型。
  * 2. 还需要在单例对象中写明隐式转换
  * 3. 把隐式转换函数导进来
  *
 */
object 专题$隐式转换 extends App {

  /*
   "120"/12这个表达式在正常情况是无法计算的，但是如果定义了一个string2Int的隐式转换时，就会把自动把String转换成Int类型
   相当于string2Int("120")/12
  */
  def string2int(): Unit ={
    implicit def String2Int(str: String) = {
      str.toInt
    }
    println("120"/12) //10
  }

  /*
   与新类型的交互操作
   val person = Person("xiaohong", 1)
   person + 1 //2
   1 + person //error,需定义隐式转换： implicit def Add1(x: Int) = Person("Empty", x)
  */
  def plusNewType(): Unit ={
    case class Person(name: String, age: Int) {
      def +(x: Int) = age + x
      def +(p: Person) = age + p.age
    }

    val person = Person("xiaohong", 1)
    println(person + 1) //2

    //1 + person，本来是不能运算的，但如果把Int类型转换成Person，然后因为Person重载了+操作符，就成了Person + Person了，这样就可以操作了
    implicit def Int2Person(x: Int) = Person("Empty", x)
    println(1 + person) //2,相当于1.+(person)
    println(person + 1) //无需隐式转换，相当于调用person.+(1)

    //定义一个RichFile来扩展现有的File功能，同时定义一个隐式转换函数File2RichFile
    import java.io.File
    import scala.io.Source
    class RichFile(val file:File){
      def read=Source.fromFile(file).getLines().mkString
    }
    implicit def File2RichFile(from:File) = new RichFile(from)
    //当调用new File()时，会自动调用隐式转换函数File2RichFile，虽然表面上创建的是File类，但实际上是RichFile类
    val file = new File("D:\\controller.log")
    println(file.read)

    //自定义-->操作符
    implicit class MyRange(start: Int) {
      def -->(end: Int) = start to end
    }
    print((1 --> 10).sum) // 55

  }


  //隐式类
  def implicitClass(): Unit = {
    //1. 先看一个公共隐式转换方法来转换的例子
    //前面提到，隐式转换最重要的应用是扩展已存在的类，它的功能和c#中的扩展方法很类似。比如我们想对已有的Int类型添加一个sayhi的方法，可以这样做：
    class SayhiImpl(ivalue:Int) {
      val value:Int = ivalue
      def sayhi = println(s"Hi $value!")
    }
    implicit def int2Sayhi(x:Int) = new SayhiImpl(x)
    //调用过程：发现‘123’是Int类型，通过隐式转换成new SayhiImpl(123)对象，然后调用该对象的sayhi()方法
    123.sayhi //Hi 123!

    //2. 发现上面比较啰嗦，先定义一个类，在定义一个隐式转换方法，而使用隐式类实现等价的功能就比较简略多了
    implicit class SayhiImpl2(ivalue:String) {//在这里就是构造函数SayhiImpl(ivalue:String)定义了String到SayhiImpl的隐式转换。
      val value:String = ivalue
      def sayhi = println(s"Hi $value!")
    }
    "123".sayhi  //Hi 123!

    /**** 使用隐式转换需要注意的问题 ****/
    //1.只能在别的trait/类/对象内部定义。
    object Helpers {
      implicit class RichInt(x: Int) // 正确！
    }
    //implicit class RichDouble(x: Double) // 错误！

    //2.构造函数只能携带一个非隐式参数。
    implicit class RichDate(date: java.util.Date) // 正确！
    //implicit class Indexer[T](collecton: Seq[T], index: Int) // 错误！
    implicit class Indexer[T](collecton: Seq[T])(implicit index: Index) // 正确！

    //虽然我们可以创建带有多个非隐式参数的隐式类，但这些类无法用于隐式转换。

    //3.implict关键字不能用于case类
    //implicit case class Baz(x: Int) // 错误！
  }

  /*
   隐式参数
   在一个方法的参数名前加上 implicit 关键字
  */
  def implicitParam(): Unit ={
    implicit val a = 2
    implicit val b = "B"

    def F(implicit x: Int, y:String) = {
      x + y
    }
    println(F) // 2B,必须存在隐式的Int和隐式的String类型，否则会报错
    println(F(1, "A")) // 1A


    //隐式参数，compare和compare2是等价的，不同的是implicitly方法可以获取到隐式转换的标的对象，从而可以直接调用其方法
    def compare[T](x:T,y:T)(implicit ordered: Ordering[T]):Int = {
      ordered.compare(x,y)
    }
    //implicit是隐式的隐式转换，implicitly是显实的隐式转换
    //在函数定义的时候，支持在最后一组参数使用 implicit，表明这是一组隐式参数。在调用该函数的时候，可以不用传递隐式参数，而编译器会自动寻找一个implict标记过的合适的值作为该参数。
    def compare2[T: Ordering](x: T, y: T) = {
      val ord = implicitly[Ordering[T]]
      ord.compare(x, y)
    }
    //例如上面的函数，调用compare时不需要显式提供ordered，而只需要直接compare(1,2)这样使用即可。
    compare(1,2)
    compare2(1,2)

    //再举一个例子：
    trait Adder[T] {
      def add(x:T,y:T):T
    }
    //先定义缺省的隐式参数，当调用addTest(...)不传入最后一个参数Adder[Int]时，默认就使用addIt
    implicit val addIt = new Adder[Int] {
      override def add(x: Int, y: Int): Int = x+y
    }
    //定义一个addTest()加法方法，同时定义一个隐式参数
    def addTest(x:Int,y:Int)(implicit adder: Adder[Int]) = {
      adder.add(x,y)
    }

    addTest(1,2)      // 正确, = 3
    addTest(1,2)(addIt)   // 正确, = 3
    addTest(1,2)(new Adder[Int] {
      override def add(x: Int, y: Int): Int = x-y
    })   // 同样正确, = -1
    //Adder是一个trait，它定义了add抽象方法要求子类必须实现。
    //addTest函数拥有一个Adder[Int]类型的隐式参数。
    // 在当前作用域里存在一个Adder[Int]类型的隐式值implicit val a。
    //在调用addTest时，编译器可以找到implicit标记过的a，所以我们不必传递隐式参数而是直接调用addTest(1,2)。而如果你想要传递隐式参数的话，你也可以自定义一个传给它，像后两个调用所做的一样。
  }

  //Manifest[T]上下文界定
  //如果去掉:Manifest，则会报错：cannot find class tag for element type T
  //因为Array编译是泛型会被抹掉，所以需要借助Manifest类隐式推断出类型然后转换
  def makePair[T:Manifest](first:T,second:T) {
    val r = new Array[T](2);r(0) = first;r(1)=second;r
  }

  /**
    * 类型约束
    *
    */
  def typeConstraint() = {
    val capital = List(
      "US" -> "Washington",
      "France" -> "Paris",
    "Japan" -> "Tokyo")

    capital.toMap  // OK
    //当List的元素类型不是Tuple2时，试图调用toMap是编译失败的。

    val phones = List("+9519728872", "+9599820012")
    //phones.toMap // error: Cannot prove that String <:< (T, U).
    //其中，toMap定义在TraversableOnce特质中。
    /*trait TraversableOnce[+A] {
      //可以看出限定A 必须是 tuple2
      def toMap[T, U](implicit ev: A <:< (T, U)): immutable.Map[T, U] = {
        val b = immutable.Map.newBuilder[T, U]
        for (x <- self)
          b += x
        b.result
      }
    }*/
  }


}
