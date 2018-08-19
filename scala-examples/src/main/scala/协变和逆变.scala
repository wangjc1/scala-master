

object 协变和逆变 {

  def main(args: Array[String]): Unit = {

  }

  /*
   协变和逆变：
   scala类型参数的协变和逆变
   scala默认不支持协变和逆变
   要想让scala的泛型支持协变，在泛型前面再加一个"+"
   要想让scala的泛型支持逆变，在泛型前面再加一个"-"
   但是一个类不能同时支持协变和逆变
 */
  def covariance(): Unit = {
    class Person{}
    class Student extends Person{}
    class Teacher extends Person{}

    //支持协变的泛型类
    class A[+T] {}
    val a1:A[Person] = new A[Person]()
    val a2:A[Person] = new A[Student]() //转换成子类

    //支持逆变的泛型类
    class B[-T] {}
    val b1:B[Person] = new B[Person]()
    val b2:B[Student] = new B[Person]() //转换成父类
  }

  /**
    * 协变点和逆变点
    * 参考：http://hongjiang.info/scala-pitfalls-10/
    */
  def covariant() = {

    //逆变点：如果类定义成A[+T] ，func()编译不通过，因为如果定义成协变会导致子类中func方法参数范围变小
    //我们完全可以把它看做一个函数类型，即 A => Unit 与 Function1[-A, Unit]等价，而
    class A[-T] {
      def func(x: T) {}
    }

    //协变点：如果类定义成A[-T] ，func()编译不通过，因为如果定义成逆变会导致子类中func方法返回值处理能力变小
    //则与 Function[+A] 等价。
    class In[+A]{
      def func(): A = null.asInstanceOf[A]
    }

  }
}
