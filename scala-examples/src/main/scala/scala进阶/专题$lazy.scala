package scala进阶


/**
  * 添加了lazy 的变量，只有在变量被使用时才进行初始化
  */
object 专题$lazy extends App {
    /** 场景一 **/
    //在类的初始化的时候可能某个变量初始化比较耗时，那么可以使用lazy，等真正使用到这个变量的时候再初始化
    class Person{
        /*lazy*/ val properties = { //如果不加lazy，则初始化Person时，需要等待2秒
            //模拟长时间的某种操作
            println("init")
            Thread.sleep(2000)
        }
    }
    println("Start")
    val startTime = System.currentTimeMillis()
    val person = new Person
    val endTime = System.currentTimeMillis()
    println("End and take " + (endTime - startTime) + "ms")


    /** 构造顺序问题 **/
    //这个问题除了lazy解决外(每次使用前都会检查是否已经初始化，会有额外的开销，并没有那么高效)，还可以用提前定义来解决
    trait Person2{
        val name: String
        /*lazy*/ val nameLen = name.length //如果不加lazy会出现空指针问题
    }
    class Student extends Person2{
        override val name: String = "Tom"
    }
    new Student
}
