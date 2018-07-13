import scala.math._

/**
 *
 * @author  wangjc
 *          2018/6/28
 */
object 模式匹配 {

  def main(args: Array[String]) {
    OptioAndSome()
    caseClass()
    caseVar()
  }

  /*
  Option[T] 是一个类型为 T 的可选值的容器： 如果值存在， Option[T] 就是一个 Some[T] ，如果不存在， Option[T] 就是对象 None
   */
  def OptioAndSome(): Unit = {
    val myMap: Map[String, String] = Map("key1" -> "value")
    val value1: Option[String] = myMap.get("key1") //Some("value1")
    val value2: Option[String] = myMap.get("key2") // None
    val value3 = myMap.getOrElse("key2", "Nothing") //Nothing
  }

  /**
   * 用case申明的类，和普通类差不多，可以自动实现伴身类和模式匹配
   */
  def caseClass(): Unit = {
    abstract class Person
    case class Student(name:String,age:Int,studentNo:Int) extends Person
    case class Teacher(name:String,age:Int,teacherNo:Int) extends Person
    case object Nobody extends Person

    //case class 会自动生成apply方法，从而省去new操作
    val p:Person=Student("john",18,1024)
    //匹配具体的类
    p  match {
      case Student(name,age,studentNo)=>println(name+":"+age+":"+studentNo)
      case Teacher(name,age,teacherNo)=>println(name+":"+age+":"+teacherNo)
      case Nobody=> "" //继承Object不用实例化
    }

  }

  /**
   * 匹配时自动调用Email的unapply方法，把user和domain解析出来
   */
  def unapply(): Unit = {
    case class Email(user:String,domain:String)
    val e = Email("alex","localhost")
    e match {case Email(user,domain)=>user+"@"+domain} //String = alex@localhost
  }

  /**
   * 匹配列表
   * E,Pi,Nil以大写字母开头的命名，scala认为都是常量
   */
  def caseList(): Unit = {
    def listMatch(l: List[Int]) = l match {
      case 0 :: Nil => "0"
      // 多个中置表达式更易读
      case x :: y :: Nil => x + " " + y
      case h :: t => h + " " + t.mkString(" ")
      // 上面的一种写法,将调用：：.unapply(lst)
      case ::(h,t) => h
      case 0 :: tail => "0 ..."
      case _ => "something else"
    }
  }

  /**
   * 变量模式匹配
   * 如果匹配的不是0，则直接匹配someThing，同时把要匹配的值传给someThing
   */
  def caseVar(): Unit = {
    val v = 1
    var n = v match {
      case 0 => "0"
      case someThing => "Not zero:"+someThing;
    }
    println(n)

    //case后如果不用`pi`符号包括，则scala编译器认为pi是个变量，可以匹配任何值，从而代码根本不会往后执行
    import math.{E,Pi}
    val pi = Math.PI
    var p = E match {
      //如果本地也有个变量叫pi，则用`pi`符号包裹后才能识别成本地变量，否则会认为是个变量
      case `pi` => "Strange Math PI = "+pi
      case _  => "OK"
    }
    println(p)

  }

}



