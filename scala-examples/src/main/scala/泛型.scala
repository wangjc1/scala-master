
/**
  *
  * @author wangjc
  *         2018/6/29
  */
object 泛型 {

    def main(args: Array[String]) {

    }

    /*
     泛型类
    */
    class Pair[T](val first: T, val second: T)

    /*
    泛型函数
    */
    def getMiddle[T](a: Array[T]) = a(a.length / 2)

    /*
    类型变量界定：上限
    注意：这相当于是对类型T加了一条限制：T必须是Comparable[T]的子类型。原来给T指定什么类型都可以，现在就不行了。
        这样一来，我们可以实例化Student[String]。但是不能实例化Student[File]，因为String是Comparable[String]的子类型，
        而File并没有实现Comparable[File]接口。
    */
    class Student[T <: Comparable[T]](val first: T, val second: T) {
        def smaller = if (first.compareTo(second) < 0) first else second
    }


    /*
      视图界定：刚才将的类型变量界定建立在类继承层次结构的基础上，但有时候这种限定不能满足实际要求，如果希望跨越类继承层次结构时，
             可以使用视图界定来实现的，其后面的原理是通过隐式转换（我们在下一讲中会详细讲解什么是隐式转换）来实现。视图界定利用<%符号来实现。
    */
    def agreement(): Unit = {
        class Student[T <: Comparable[T]](val first: T, val second: T) {
            def smaller = if (first.compareTo(second) < 0) first else second
        }
        //val student = new Student[Int](4,2) //error
        //println(student.smaller)
        /*
        可惜，如果我们尝试用Student(4,2)的实现，编译器会报错。因为Int和Integer不一样，Integer是包装类型，但是Scala的Int并没有实现Comparable。
        不过RichInt实现了Comparable[Int]，同时还有一个Int到RichInt的隐士转换。解决途径就是视图界定。
        <%关系意味着T可以被隐式转换成Comparable[Int]。
        */
        class Student2[T <% Comparable[T]](val first: T, val second: T) {
            def smaller = if (first.compareTo(second) < 0) first else second
        }

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
        class Person {}
        class Student extends Person {}
        class Teacher extends Person {}

        //支持协变的泛型类
        class A[+T] {}
        val a1: A[Person] = new A[Person]()
        val a2: A[Person] = new A[Student]() //转换成子类

        //支持逆变的泛型类
        class B[-T] {}
        val b1: B[Person] = new B[Person]()
        val b2: B[Student] = new B[Person]() //转换成父类
    }

    /*
      类型通配符:
      类型通配符是指在使用时不具体指定它属于某个类，而是只知道其大致的类型范围
      通过”_<:” 达到类型通配的目的
    */
    def typeWildcard: Unit = {
        class Person(val name: String) {
            override def toString() = name
        }
        class Student(name: String) extends Person(name)
        class Teacher(name: String) extends Person(name)
        class Pair[T](val first: T, val second: T) {
            override def toString() = "first:" + first + ", second: " + second;
        }
        //Pair的类型参数限定为[_<:Person]，即输入的类为Person及其子类
        //类型通配符和一般的泛型定义不一样，泛型在类定义时使用，而类型通配符号在使用类时使用
        def makeFriends(p: Pair[_ <: Person]) = {
            println(p.first + " is making friend with " + p.second)
        }

        makeFriends(new Pair(new Student("john"), new Teacher("摇摆少年梦")))
    }

    //type T定义一种类型
    trait Abstract {
        type T

        def transform(x: T): T

        val initial: T
        var current: T
    }

    class Concrete extends Abstract {
        type T = String

        def transform(x: T) = x + x

        val initial = "hi"
        var current = initial
    }

    //用泛型和上面的type差不多
    trait Abstract2[T] {
        def transform(x: T): T

        val initial: T
        var current: T
    }

    class Concrete2 extends Abstract2[String] {
        def transform(x: String) = x + x

        val initial = "hi"
        var current = initial
    }

}
