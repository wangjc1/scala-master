package scala基础

import scala.beans.BeanProperty

/**
  * @see https://www.jianshu.com/p/32b5d65226b8
  */
object 面向对象 extends App {

    /** 类 **/
    class HelloWorld {
        // 1、定义不带private的 var field，此时scala生成class时，会自动生成一个private[this]的成员字段（名称与field不同），并还生成一对getter和setter方法，分别叫做field和 field_=，并且getter和setter方法的访问修饰符与field定义相同
        // 2、而如果使用private修饰field，则只生成的getter和setter，且访问修饰也是private的
        // 3、如果定义val field，则只会生成getter方法
        // 4、 如果不希望生成setter和getter方法，则将field声明为private[this]
        var myName = "leo"

        //自定义myName 成员变量getter方法
        def name = "your name is " + myName

        //自定义myName 成员变量的setter方法
        def name_=(newValue: String)  {
            print("you cannot edit your name!!!")
        }

        def sayHello() { print("Hello, " + name) }
        def getName = name
    }
    // 创建类的对象，并调用其方法
    val helloWorld = new HelloWorld
    helloWorld.name = "leo1" //实际上会调用 leo.name_=("leo1")方法
    helloWorld.sayHello()
    print(helloWorld.getName) // 也可以不加括号，如果定义方法时不带括号，则调用方法时也不能带括号


    /** 将某个变量限定在只有类的对象才能访问 **/
    class Student {
        private var myAge = 0 //试着修改成private[this]
        def age_=(newValue: Int) {
            if (newValue > 0) myAge = newValue
            else print("illegal age!")
        }
        def age = myAge
        def older(s: Student) = {
            myAge > s.myAge //修改成private[this]后，就会报错，因为myAge被限定在对象才能访问
        }
    }

    /** Java风格的getter和setter方法 **/
    import scala.beans.BeanProperty
    class Teacher {
        @BeanProperty var name: String = _
    }
    //class Teacher(@BeanProperty var name: String)
    val t = new Teacher
    t.setName("leo")
    t.getName()

    /** 辅助constructor **/
    class Student1 {
        private var name = ""
        private var age = 0
        // 第一辅助构造器
        def this(name: String) {
            this()
            this.name = name
        }
        //第二个辅助构造器
        def this(name: String, age: Int) {
            this(name)
            this.age = age
        }
    }

    /**
    Scala中，主constructor是与类名放在一起的，有且只有一个，与java不同
    而且类中，没有定义在任何方法中的代码（包括成员字段），都属于主constructor的代码
    且执行的顺序与代码书写的顺序一致。这其实与Java是一样的，在Java中方法之外的代码（成员以及代码块）
    会在构造器调用之前最先执行，姑且将这些代码看作也是放到了一个主构造器中进行执行的，只不过这种主构造器不能带构造参数
    主构造器与类定义是在一起的，如果有参数，则在类名后面跟括号即可：
    **/
    class Student2() {}
    // 主constructor中还可以通过使用默认参数，来给参数默认的值
    class Student3(val name: String = "leo", val age: Int = 30) {
        println("your name is " + name + ", your age is " + age)
    }

    /*
    主构造器：
    主构造器会执行类定义中的所有语句。如下，println语句是主构造器的一部分，当类被实例化时，println语句会立即执行。
    这样客户代码将不能调用Person类的主构造器，如果需要设置主构造器中的参数，有两种方案：
    一是添加辅助构造器，二是用伴生对象的apply工厂方法。
    */
    //使用private关键字让外部无法调用构造器，一般在单例中这么使用
    class MyPerson private( private var _name: String,
                          private var _age: Int    ) {
        println("This class is Person")
    }
    //new MyPerson() //error
    //new MyPerson("Jone",20) //error

    /**内部类，Scala中，同样可以在类中定义内部类；但是与java不同的是，每个外部类的对象的内部类，都属于不同的类**/
    import scala.collection.mutable.ArrayBuffer
    class Class {
        class Student(val name: String) {}
        val students = new ArrayBuffer[Student]
        def getStudent(name: String) =  {
            new Student(name)
        }
    }
    val c1 = new Class
    val s1 = c1.getStudent("leo")
    c1.students += s1
    val c2 = new Class
    val s2 = c2.getStudent("leo")
    //c1.students += s2   //报错，因为scala认为s2不是c1类的对象

    /*** object 伴生对象 * ***/
    //1、object，相当于class的单个实例（但与从class实例化出来的对象的内容决不一样），通常在里面放一些class层面上共享的内容，如Java中的静态field或者method即在定义在object中（注：Scala中没有Java的静态概念，所以延伸出了object这个东东）
    //2、你可以将object看作是一个类class，只是这个类在内存中只有一个单例，且定义的object名就是实例名，不需我们自己实例化，运行时JVM已帮我们new出来了
    //3、第一次调用object的方法时，就会执行object的constructor，也就是object内部不在method中的代码；但是object不能定义接受参数的constructor
    //4、注意，object的constructor只会在其第一次被调用时执行一次，以后再次调用就不会再次执行constructor了
    //5、object通常用于作为单例模式的实现，或者放class的静态成员，比如工具方法
    object Person {
        private val eyeNum = 2
        def getEyeNum = eyeNum
        /** apply方法 **/
        // object中非常重要的一个特殊方法，就是apply方法
        // 通常在伴生对象中实现apply方法，并在其中实现构造伴生类的对象的功能，一般用作工厂方法
        // 而创建伴生类的对象时，通常不会使用new Class的方式，而是使用Class()的方式，隐式地调用伴生对象得apply方法，这样会让对象创建更加简洁
        // 比如，Array类的伴生对象的apply方法就实现了接收可变数量的参数，并创建一个Array对象的功能
        def apply(name: String, age: Int) = new Person(name,age)
    }
    class Person(val name: String, val age: Int) {
        def sayHello = println("Hi, " + name + ", I guess you are " + age + " years old!" + ", and usually you must have " + Person.eyeNum + " eyes.")
    }
    val alex = Person("wang",28) //可以直接用名字实例化，不用new


    /** 让object继承抽象类 **/
    // object的功能其实和class类似，除了不能定义接受参数的constructor之外
    // object也可以继承抽象类，并覆盖抽象类中的方法
    abstract class Hello(var message: String) {
        def sayHello(name: String): Unit
    }
    object HelloImpl extends Hello("hello") {
        override def sayHello(name: String) = {
            println(message + ", " + name)
        }
    }

    /**  继承： Scala中，让子类继承父类，与Java一样，也是使用extends关键字 **/
    //1. 如果超类中主构造器中有参数，则需要子类把参数传递到超类中
    class Employee(name:String,age:Int,salary:Double) extends Person(name,age) {
        private var score = "A"
        def getScore = score
    }
    //2 子类除了可以继承超类的主构造器外，还可以继承超类的辅助构造器
    class GoodStudent1 extends Student1 {
    }
    class GoodStudent2(name:String,NO:String) extends Student1(name) {
    }
    class GoodStudent3(newName:String,age:Int,NO:String) extends Student1(newName,age) {
    }

    //scala的类重写java的构造器
    class Square(x:Int,y:Int,width:Int) extends java.awt.Rectangle(x,y,width,width)


    /** 重写：Override 和Super***/
    //1. 使用override关键字可以尽早发现问题
    //2. 使用super关键字可以调用父类中的属性和方法
    //3. 子类可以覆盖父类的同名的非private成员
    //4. Scala中，子类可以覆盖父类的val field，而且子类的val field还可以覆盖父类的val field的getter方法；只要在子类中使用override关键字即可
    //5. var变量是不能被重写的
    class Employee2(name:String,age:Int,salary:Double) extends Person(name,age) {
        override  def sayHello() = { println(s"${super.sayHello},salary=$salary%1.2f") }
    }

    /** 关于def重写规则**/
    //1. def 只能重写另一个def
    //2. val可以重写另一个val或者不带参数的def
    //3. var只能重写另一个抽象的var
    {
        //更常见的是重一个val重写一个抽象的def
        abstract class Person{
            def id:Int
        }
        class Student extends Person {
            override val id: Int = 2
        }
    }


    /**  关于重写字段被超类中被提前使用的问题 ***/
    class Creature {
        val range:Int = 10
        val env:Array[Int] = new Array[Int](range)
    }

    //初始化后Array将被初始化为Array[0]
    class AntErr extends Creature {
        override val range:Int = 2
    }

    //推荐使用提前定义方法提前初始化
    class AntOk extends {
        override val range:Int = 2
    } with Creature


    /** isInstanceOf(判断是否是某个类的实例)和asInstanceOf(转换成某个对象的实例) **/
    val emp = new Employee2("wang",29,500000.00)
    assert(emp.isInstanceOf[Person],"emp is not instance of Person") //true
    emp.asInstanceOf[Person] //ok

    /*** getClass和classOf ****/
    // isInstanceOf只能判断出对象是否是给定类或其子类的实例对象，而不能精确判断出对象就是给定类的实例对象
    // 如果要求精确地判断对象就是指定类的对象，那么就只能使用getClass和classOf了
    // 对象.getClass可以精确获取对象所属的类class，classOf[类]可以精确获取类，然后使用==操作符即可判断
    val stu: Student1 = new GoodStudent1
    stu.isInstanceOf[Student1] //true,无法准确判断出是某个类的实例，有可能是其父类的实例
    stu.getClass == classOf[Student1] //false
    stu.getClass == classOf[GoodStudent1] //true


    /**** 使用模式匹配进行类型判断 ***/
    // 但是在实际开发中，比如spark的源码中，大量的地方都是使用了模式匹配的方式来进行类型的判断，这种方式更加地简洁明了，而且代码得可维护性和可扩展性也非常的高
    // 使用模式匹配，功能性上来说，与isInstanceOf一样，也是判断主要是该类以及该类的子类的对象即可，也不是精准判断的
    {
        class Person
        class Student extends Person
        val p: Person = new Student
        p match {
            case per: Person => println("it's Person's object")
            case _  => println("unknown type")
        }

        //模式匹配
        class EMail(val user: String, val domain: String)
        object EMail {
            // The injection method (optional)
            def apply(user: String, domain: String) =  new EMail(user,domain)
            // The extraction method (mandatory)
            def unapply(email: EMail): Option[(String, String)] = {
                if (email == null){
                    None
                }
                else{
                    Some(email.user, email.domain)
                }
            }
        }
        val obj = EMail("alex","126.com")
        obj match {
            case EMail(user,domain) => println(s"The name is  $user @ $domain")
        }
    }


    /**** protected关键字 ***/
    // 跟java一样，scala中同样可以使用protected关键字来修饰field和method，这样子类就可以继承这些成员或方法
    // private[this]这个表示只能在本类的对象中使用
    // 还可以使用protected[this]，则只能在当前子类对象中访问父类的使用protected[this]修饰的field和method，无法通过其他子类对象访问父类中的这些字段与方法
    {
        class Person {
            protected var name: String = "leo"
            protected[this] var hobby: String = "game"
        }
        class Student extends Person {
            def sayHello = println("Hello, " + name)
            def makeFriends(s: Student) {
                println("my hobby is " + hobby + ", your hobby is "/* + s.hobby*/) //s.hobby将编译出错
            }
        }
        //protected[this]修饰的字段只能在本对象或其子对象中使用，不能在其他对象中使用：
        class Person2 {
            protected var name: String = "leo"
            protected[this] var hobby: String = "game"
            def makeFriends(s: Person2 ){
                println("my hobby is " + hobby + ", your hobby is " /*+ s.hobby*/) //s.hobby将编译出错
            }
        }
        //与private[this]一样，protected[this]也可以修饰方法
    }

    /** object可以实现枚举功能 **/
    object Color extends Enumeration {
        val RED, BLUE, BLACK, YELLOW = Value
    }
    object Season extends Enumeration {
        val SPRING = Value(0, "spring")
        val SUMMER = Value(1, "summer")
        val AUTUMN = Value(2, "autumn")
        val WINTER = Value(3, "winter")
    }
    Season(0)  // spring
    Season.withName("spring") // spring，根据名称找
    // 使用枚举object.values可以遍历枚举值
    for (ele <- Season.values) println(ele)


    /** 特质（接口） ，特质中可以包含实现的方法 **/
    trait Similarity {
        def isSimilar(x: Any): Boolean
        def isNotSimilar(x: Any): Boolean = !isSimilar(x)
    }

    /** 抽象类 **/
    abstract class Animal {
    }
    trait WaggingTail{
    }
    class Dog extends Animal with WaggingTail {
    }
}
