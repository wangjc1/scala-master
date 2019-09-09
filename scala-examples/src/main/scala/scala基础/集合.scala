package scala基础

/**
  *
  * @author: wangjc
  *          2019/9/4
  */
object 集合 extends App {

    ///// 长度不可变，底层其实就是java数组
    // 数组初始化后，长度就固定下来了，而且元素全部根据其类型初始化
    val ia = new Array[Int](10)
    ia(0)
    ia(0) = 1
    val sa = new Array[String](10)
    // 可以直接使用Array()创建数组，元素类型自动推断
    val sa2 = Array("hello", "world")
    sa(0) = "hi"
    val sa3 = Array("leo", 30)

    /////类似于Java中的ArrayList这种长度可变的集合类
    import scala.collection.mutable.ArrayBuffer
    // 使用ArrayBuffer()的方式可以创建一个空的ArrayBuffer
    val b = ArrayBuffer[Int]()
    // 使用+=操作符，可以添加一个元素，或者多个元素
    // 这个语法必须要谨记在心！因为spark源码里大量使用了这种集合操作语法！
    b += 1
    b += (2, 3, 4, 5)
    // 使用++=操作符，可以添加其他集合中的所有元素
    b ++= Array(6, 7, 8, 9, 10)
    // 使用trimEnd()函数，可以从尾部截断指定个数的元素
    b.trimEnd(5)
    // 使用insert()函数可以在指定位置插入元素
    // 但是这种操作效率很低，因为需要移动指定位置后的所有元素
    b.insert(5, 6)
    b.insert(6, 7, 8, 9, 10)
    // 使用remove()函数可以移除指定位置的元素
    b.remove(1)
    b.remove(1, 3)
    // Array与ArrayBuffer可以互相进行转换
    b.toArray
    sa3.toBuffer //Array to ArrayBuffer

    /////  遍历Array和ArrayBuffer
    // 使用for循环和until遍历Array / ArrayBuffer
    // 使until是RichInt提供的函数
    for (i <- 0 until b.length)
        println(b(i))
    // 跳跃遍历Array / ArrayBuffer
    for(i <- 0 until (b.length, 2))
        println(b(i))
    // 从尾部遍历Array / ArrayBuffer
    for(i <- (0 until b.length).reverse)
        println(b(i))
    // 使用“增强for循环”遍历Array / ArrayBuffer
    for (e <- b)
        println(e)

    /////数组常见操作
    // 数组元素求和
    val a = Array(1, 2, 3, 4, 5)
    val sum = a.sum
    // 获取数组最大值
    val max = a.max
    // 对数组进行排序
    scala.util.Sorting.quickSort(a)
    // 获取数组中所有元素内容
    a.mkString
    a.mkString(", ")
    a.mkString("<", ",", ">")
    // toString函数
    a.toString
    b.toString


    ///// 使用yield和函数式编程转换数组
    // 对Array进行转换，获取的还是Array
    val a1 = Array(1, 2, 3, 4, 5)
    val a2 = for (ele <- a) yield ele * ele
    // 对ArrayBuffer进行转换，获取的还是ArrayBuffer
    val b1 = ArrayBuffer[Int]()
    b1 += (1, 2, 3, 4, 5)
    val b2 = for (ele <- b1) yield ele * ele
    // 结合if守卫，仅转换需要的元素
    val a3 = for (ele <- b1 if ele % 2 == 0) yield ele * ele
    // 使用函数式编程转换数组（通常使用第一种方式）
    a1.filter(_ % 2 == 0).map(2 * _)
    a1.filter { _ % 2 == 0 } map { 2 * _ }


    ///// List 可重复集合
    val list = List(1,1,1,4,2)
    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    numbers.partition(_ % 2 == 0) //(List[Int], List[Int]) = (List(2, 4, 6, 8, 10),List(1, 3, 5, 7, 9))

    ///// Set 不重复集合
    val set = Set(1,2,3,4,2)


    ///// 创建一个不可变的Map
    val ages = Map("Leo" -> 30, "Jen" -> 25, "Jack" -> 23)
    //ages("Leo") = 31 //报错，因为Map是不可变的
    // 创建一个可变的Map
    val agesM = scala.collection.mutable.Map("Leo" -> 30, "Jen" -> 25, "Jack" -> 23)
    agesM("Leo") = 31
    // 使用另外一种方式定义Map元素
    val ages2 = Map(("Leo", 30), ("Jen", 25), ("Jack", 23))
    // 创建一个空的HashMap
    val ages3 = new scala.collection.mutable.HashMap[String, Int]

    // 获取指定key对应的value，如果key不存在，会报错
    val leoAge = ages("Leo")
    // 使用contains函数检查key是否存在
    val leoAge2 = if (ages.contains("leo")) ages("leo") else 0
    // getOrElse函数
    val leoAge3 = ages3.getOrElse("leo", 0)

    // 遍历map的entrySet
    for ((key, value) <- agesM) println(key + " " + value)
    // 遍历map的key
    for (key <- agesM.keySet) println(key)
    // 遍历map的value
    for (value <- agesM.values) println(value)
    // 生成新map，反转key和value
    for ((key, value) <- agesM) yield (value, key)
    //SortedMap和LinkedHashMap
    // SortedMap可以自动对Map的key的排序
    val sortedAge = scala.collection.immutable.SortedMap("leo" -> 30, "alice" -> 15, "jen" -> 25)
    // LinkedHashMap可以记住插入entry的顺序
    val modifyAge = new scala.collection.mutable.LinkedHashMap[String, Int]
    modifyAge("leo") = 30
    modifyAge("alice") = 15
    modifyAge("jen") = 25

    //Map的元素类型—Tuple
    // 简单Tuple
    val t = ("leo", 30)
    // 访问Tuple
    t._1

    // zip操作
    val names = Array("leo", "jack", "mike")
    val ages4 = Array(30, 24, 26)
    val nameAges = names.zip(ages4)
    for ((name, age) <- nameAges) println(name + ": " + age)
}
