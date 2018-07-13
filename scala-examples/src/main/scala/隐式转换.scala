/**
 *
 * @author  wangjc
 *          2018/6/29
 */
object 隐式转换 {
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

    implicit def plus(x: Int) = Person("Empty", x)
    println(1 + person) //2,相当于1.+(person)
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
  }

  def main(args: Array[String]): Unit = {
    plusNewType()
    implicitParam()
  }

}
