import scala.collection.mutable.ArrayBuffer

/**
 *
 * @author  wangjc
 *          2018/6/28
 */
object 类和对象 {
  def main(args: Array[String]): Unit = {

    val a = new ArrayElement2(Array("124","54454"))
    println(a.height)

    val ant = new Ant //env.length = 0
    val ant2 = new Ant2 //env.length = 2
    val ant3 = new Ant3(3)//env.length = 3

    val boy = new Boy()
    val apple = new Apple()
    val banana = new Banana()
    boy.eat(apple); //Boy eats Apple
    boy.eat(banana);//Boy eats Banana

    val half =new Rational(1,2)
    val third=new Rational(1,3)
    half > third // true


    val queue1 = new BasicIntQueue with Doubling {
    }
    queue1.put(2)

  }
}

//抽象类
abstract class Element {
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length
}

//重写contends方法
class ArrayElement(conts: Array[String]) extends Element {
  def contents: Array[String] = conts
}

//可以通过一个成员变量来实现基类中定义的抽象函数 contents。
//可以看到，这是使用成员变量来实现基类中不带参数的抽象函数的一个非常恰当的例子。 Scala 中的这种实现是 Java 语言所不支持的，一般来说只有两个不同的命名空间来定义类，
//而 Java 可以有四个，Scala 支持的两个命名空间如下：
// 值（字段，方法，包还有单例对象）
// 类型（类和Trait名）
class ArrayElement2(conts: Array[String]) extends Element {
  val contents: Array[String] = conts
}

/*
我们回到前面定义的类 ArrayElement，它有一个参数 conts，其唯一的目的是用来复制到 contents 成员变量。而参数名称 conts 是为了让它看起来和成员变量 contents 类似，而有不至于和成员变量名冲突。
Scala 支持使用参数化成员变量，也就是把参数和成员变量定义合并到一起来避免上述代码：
 */
class ArrayElement3(val contents: Array[String]) extends Element {
}

/*
 调用基类构造器
 */
class LineElement(s:String) extends ArrayElement(Array(s)) {
  override def width = s.length
  override def height = 1
}


//定义一个动物抽象类，range是看到的范围，默认是10
abstract class Creature {
  val range: Int = 10
  val env:Array[Int] = new Array[Int](range)
}
//定义一个蚂蚁看到的范围为2
class Ant extends Creature {
  override val range: Int = 2
}
//提前定义：在超类的构造方法执行前初始化子类的val变量
class Ant2 extends {
  override val range: Int = 2
} with Creature
//子类定义一个参数的构造方法
class Ant3(override val range: Int)extends Creature{}


//动态绑定:在java中是无法实现动态绑定的，但scala中可以
abstract class Fruit{
}
class Apple extends Fruit{
}
class Banana extends Fruit{
}
abstract class People{
   def eat( f:Fruit)
  {
    System.out.println("People eat Fruit");
  }
  def eat( f:Apple)
  {
    System.out.println("People eat Apple");
  }
  def eat( f:Banana)
  {
    System.out.println("People eat Banana");
  }
}
class Boy extends People {
  override def eat(f: Fruit) {
    System.out.println("Boy eats Fruit");
  }

  override def eat(f: Apple) {
    System.out.println("Boy eats Apple");
  }

  override def eat(f: Banana) {
    System.out.println("Boy eats Banana");
  }
}


/*
主构造器：
主构造器会执行类定义中的所有语句。如下，println语句是主构造器的一部分，当类被实例化时，println语句会立即执行。
这样客户代码将不能调用Person类的主构造器，如果需要设置主构造器中的参数，有两种方案：
一是添加辅助构造器，二是用伴生对象的apply工厂方法。
 */
class Person private( //通过把private修饰符添加在类参数列表的前边把主构造器隐藏起来
                      private var _name: String,
                      private var _age: Int) {
  println("This class is Person")
}

/*
对象比较，继承Ordered[T]接口，实现compare()方法
 */
class Rational (n:Int, d:Int) extends Ordered[Rational]{
  require(d!=0)
  private val g =gcd (n.abs,d.abs)
  val numer =n/g
  val denom =d/g
  override def toString = numer + "/" +denom
  def +(that:Rational)  =
    new Rational(
      numer * that.denom + that.numer* denom,
      denom * that.denom
    )
  def * (that:Rational) =
    new Rational( numer * that.numer, denom * that.denom)
  def this(n:Int) = this(n,1)
  private def gcd(a:Int,b:Int):Int =
    if(b==0) a else gcd(b, a % b)

  override def compare (that:Rational)=
    (this.numer*that.denom)-(that.numer*that.denom)

}

//混入特质
import scala.collection.mutable.ArrayBuffer
abstract class IntQueue {
  def get():Int
  def put(x:Int)
}
class BasicIntQueue extends IntQueue{
  private val buf =new ArrayBuffer[Int]
  def get()= buf.remove(0)
  def put(x:Int) { buf += x }
  override def toString = "BasicIntQueue method"
}
trait  Doubling extends IntQueue{
     abstract override def put(x:Int) { super.put(2*x)}//super是上一层级的对象，这里是 BasicIntQueue
}

