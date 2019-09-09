package scala基础

/**
  * @see https://www.cnblogs.com/nowgood/p/scalatrait.html
  *  Scala 中类只能继承一个超类, 可以扩展任意数量的特质,与Java接口相比,
  *  Scala 的特质可以有具体方法和抽象方法; Java 的抽象基类中也有具体方
  *  法和抽象方法, 不过如果子类需要多个抽象基类的方法时, Java 就做不到了
  *  (没法多继承), Scala 中类可以扩展任意数量的特质.
  *
  */
object 面向对象$特质  extends App {

    /** 带有特质的类 **/
    //Scala可以完全像Java接口一样工作, 你不需要将抽象方法声明为 abstract, 特质中未被实现的方法默认就是抽象方法;
    //类可以通过 extends 关键字继承特质, 如果需要的特质不止一个, 通过 with 关键字添加额外特质
    //重写特质的抽象方法时, 不需要 override 关键字
    //所有 Java 接口都可以当做 Scala 特质使用
    //让特质混有具体行为有一个弊端. 当特质改变时, 所有混入该特质的类都必须重新编译.
    trait Logger {
        def log(msg: String){} // 抽象方法
    }
    trait ConsoleLogger extends Logger with Serializable {  // 使用extends
        override def log(msg: String): Unit = {  // 不需要override关键字
            println("ConsoleLogger: " + msg)
        }
    }


    /** 特质可以将对象原本没有的方法与字段加入对象中
        如果特质和对象改写了同一超类的方法, 则排在右边的先被执行.**/
    // Feline 猫科动物
    abstract class Feline {
        def say()
    }
    trait Tiger extends Feline {
        // 在特质中重写抽象方法, 需要在方法前添加 abstract override 2个关键字
        abstract override def say() = println("嗷嗷嗷")
        def king() = println("I'm king of here")
    }
    class Cat extends Feline {
        override def say() = println("喵喵喵")
    }

    /** 特质的叠加，动态叠加特质类 **/
    trait Account1 {
        def withdraw (amount: Double)
    }
    //这里如果混入ConsoleLogger特质，则可以打印出消息；如果混入Logger特质则不输出东西
    class SavingsAccount1 extends Account1 with Logger {
        override def withdraw (amount: Double): Unit = {
            if (amount > 100)
                log("Insufficient funds")
        }
    }
    new SavingsAccount1().withdraw(200)


    /** 叠加在一起的特质执行顺序 **/
    //特质的构造顺序是从左到右的
    //特质的执行顺序是从右到左的
    trait Logged {
        def log(msg: String){println(msg)}
    }
    //给日志消息添加时间戳
    trait TimestampLogger extends Logged {
        override  def log(msg: String): Unit = super.log(new java.util.Date() + " " + msg)
    }
    //截断冗长的日志消息
    trait ShortLogger extends Logged {
        val maxLength = 15
        override def log(msg: String): Unit = {
            if (msg.length < maxLength) {
                super.log(msg)
            } else {
                super.log(msg.substring(0, msg.length - 3))
            }
        }
    }
    class Account
    class SavingsAccount extends Account
    //从右向左执行:ShortLogger => TimestampLogger => Logged.log
    val account = new SavingsAccount with TimestampLogger with ShortLogger
    account.log("saving....")


    /** 创建对象时初始化 **/
    //特质不能有构造器参数. 每个特质都有一个无参构造器. 值得一提的是, 缺少构造器参数是特质与类唯一不相同的技术差别. 除此之外, 特质可以具有类的所有特性, 比如具体的和抽象的字段, 以及超类.
    //这种局限对于那些需要定制才有用的特质来说会是一个问题, 这个问题具体就表现在一个带有特质的对象身上. 我们先来看下面的代码, 然后在分析一下, 就能一目了然了.
    trait Fruit {
        val name: String
        // 由于是字段, 构造时就输出
        val valPrint = println("valPrint: " + name)
        // lazy 定义法, 由于是lazy字段, 第一次使用时输出
        lazy val lazyPrint = println("lazyPrint: " + name)
        // def 定义法,  方法, 每次调用时输出
        def defPrint = println("defPrint: " + name)
    }

    // 方法1. lazy定义法
    println("** lazy定义法 构造输出 **")
    val apple1 = new Fruit {
       override val name = "Apple"
    }
    println("\n** lazy定义法 调用输出 **")
    apple1.lazyPrint
    apple1.defPrint

    // 方法2. 提前定义法
    println("\n** 提前定义法 构造输出 **")
    val apple2= new {
        override  val name = "Apple"
    } with Fruit
    println("\n** 提前定义法 调用输出 **")
    apple2.lazyPrint
    apple2.defPrint

}
