import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 *
 * @author  wangjc
 *          2018/6/27
 */
object 集合 {

  def main(args: Array[String]) {
    不可变集合运算符()
    可变集合运算符()
    操作方法()
    java集合互转换()
    并行集合()
    集合中的对象()

    some2List()
  }

  def some2List(): Unit = {
    val dirs = mutable.ArrayBuffer("abc","abcd","abcde")
    val jobs = for {
      dir <- Option(dirs).toList
      logDir <- dir
    } yield {
      logDir +"\n"
    }
    print(jobs)
  }

  def  不可变集合运算符(): Unit = {
    val left = List(1,2,3)
    val right = List(4,5,6)

    //以下操作等价
    left ++ right   // List(1,2,3,4,5,6)
    left ++: right  // List(1,2,3,4,5,6)
    right.++:(left)    // List(1,2,3,4,5,6)
    right.:::(left)  // List(1,2,3,4,5,6)

    //在头部追加元素，以下操作等价
    0 +: left    //List(0,1,2,3)
    left.+:(0)   //List(0,1,2,3)

    //在尾部追加元素，以下操作等价
    left :+ 4    //List(1,2,3,4)
    left.:+(4)   //List(1,2,3,4)

    //等同于“+:”以下操作等价
    0 :: left      //List(0,1,2,3)
    left.::(0)     //List(0,1,2,3)
  }

  def  可变集合运算符(): Unit = {
    val arryBuf = ArrayBuffer(1,2,3)
    arryBuf += 4 //(1,2,3,4)

    //可变集合加减操作
    var set = Set(1,2,3)
    set - 2 //Set(1,3)
    set -- Set(1,3) //Set()
    set ++ Set(1,2,3) //Set(1, 2, 3)

    //交集、并集，差集
    val A = Set(1,2,3)
    val B = Set(3,4,5)
    A & B  //Set(3)
    A | B  //Set(5, 1, 2, 3, 4)
    A &~ B //Set(1, 2)
    //intersect,union,diff等同于 &，|，&~
    val C = List(1,2,3)
    val D = List(3,4,5)
    C intersect D
    C union D
    C diff D
  }

  def  操作方法(): Unit = {
    //map方法
    val nums = List(1, 2, 3)
    val square = (x: Int) => x * x //定义一个计算平方的函数
    val squareNums1 = nums.map(num => num * num) //List(1,4,9)
    val squareNums2 = nums.map(math.pow(_, 2)) //List(1,4,9)
    val squareNums3 = nums.map(square) //List(1,4,9)

    //map方法对字符串处理
    val text = List("Homeway,25,Male", "XSDYM,23,Female")
    val usersList = text.map(_.split(",")(0))
    val usersWithAgeList = text.map(line => {
      val fields = line.split(",")
      val user = fields(0)
      val age = fields(1).toInt
      (user, age) //Tuple类型
    }) //List((Homeway,25), (XSDYM,23))

    //flatten和flatMap
    val text2 = List("A,B,C", "D,E,F")
    val textMapped = text2.map(_.split(",").toList) // List(List("A","B","C"),List("D","E","F"))
    //flatten可以把嵌套的结构展开.
    val textFlattened = textMapped.flatten // List("A","B","C","D","E","F")
    List(List(1, 2), List(3, 4)).flatten //List[Int] = List(1, 2, 3, 4)
    //flatMap结合了map和flatten的功能。接收一个可以处理嵌套列表的函数，然后把返回结果连接起来。
    val textFlatMapped = text2.flatMap(_.split(",").toList) // List("A","B","C","D","E","F")
    //x参数表示List类型，所以可以直接调用map方法
    List(List(1, 2), List(3, 4)).flatMap(x => x.map(x => x * 2)) //List[Int] = List(2, 4, 6, 8)

    //reduce方法，可以看到reduce可以接收一个中间函数来计算
    val nums2 = List(1, 2, 3)
    val sum1 = nums2.reduce((a, b) => a + b) //6
    val sum2 = nums2.reduce(_ + _) //6
    val sum3 = nums2.sum //6


    //reduceLeft从列表的左边往右边应用reduce函数，reduceRight从列表的右边往左边应用reduce函数
    val nums3 = List(2.0, 2.0, 3.0)
    val resultLeftReduce = nums3.reduceLeft(math.pow) // = pow( pow(2.0,2.0) , 3.0) = 64.0
    val resultRightReduce = nums3.reduceRight(math.pow) // = pow(2.0, pow(2.0,3.0)) = 256.0

    //fold 带有初始值的reduce,从一个初始值开始，从左向右将两个元素合并成一个，最终把列表合并成单一元素。
    val nums4 = List(2, 3, 4)
    val sum = nums4.fold(1)(_ + _) // = 1+2+3+4 = 9
    //foldLeft:带有初始值的reduceLeft，foldRight:带有初始值的reduceRight
    val nums5 = List(2.0, 3.0)
    val result1 = nums5.foldLeft(4.0)(math.pow) // = pow(pow(4.0,2.0),3.0) = 4096
    val result2 = nums5.foldRight(1.0)(math.pow) // = pow(1.0,pow(2.0,3.0)) = 8.0

    //排序函数
    val nums6 = List(1, 3, 2, 4)
    //按照元素自身进行排序
    val sorted = nums6.sorted //List(1,2,3,4)
    val users = List(("HomeWay", 25), ("XSDYM", 23))
    //按照应用函数=>之后产生的元素进行排序
    val sortedByAge = users.sortBy { case (user, age) => age } //List(("XSDYM",23),("HomeWay",25))
    //使用自定义的比较函数进行排序
    val sortedWith = users.sortWith { case (user1, user2) => user1._2 < user2._2 } //List(("XSDYM",23),("HomeWay",25))

    //filter函数
    val nums7 = List(1, 2, 3, 4)
    val odd = nums7.filter(_ % 2 != 0) // List(1,3)
    val even = nums7.filterNot(_ % 2 != 0) // List(2,4)

    //count函数
    //计算列表中所有满足条件p的元素的个数，等价于 filter(p).length
    val nums8 = List(-1, -2, 0, 1, 2)
    val plusCnt1 = nums8.count(_ > 0)
    val plusCnt2 = nums8.filter(_ > 0).length

    //distinct: List[A] 保留列表中非重复的元素，相同的元素只会被保留一次
    val list = List("A", "B", "C", "A", "B")
    val distincted = list.distinct // List("A","B","C")

    //groupBy, grouped
    //groupBy : groupBy[K](f: (A) ⇒ K): Map[K, List[A]] 将列表进行分组，分组的依据是应用f在元素上后产生的新元素
    //grouped: grouped(size: Int): Iterator[List[A]] 按列表按照固定的大小进行分组
    val data = List(("HomeWay", "Male"), ("XSDYM", "Female"), ("Mr.Wang", "Male"))
    //按List里面元素的第二列分组，既按性别来分组
    val group1 = data.groupBy(_._2) // = Map("Male" -> List(("HomeWay","Male"),("Mr.Wang","Male")),"Female" -> List(("XSDYM","Femail")))
    val group2 = data.groupBy { case (name, sex) => sex } // = Map("Male" -> List(("HomeWay","Male"),("Mr.Wang","Male")),"Female" -> List(("XSDYM","Femail")))
    val fixSizeGroup = data.grouped(2).toList // = Map("Male" -> List(("HomeWay","Male"),("XSDYM","Femail")),"Female" -> List(("Mr.Wang","Male")))

    //scan函数
    //由一个初始值开始，从左向右，进行积累的op操作，这个比较难解释，具体的看例子吧。
    val nums9 = List(1, 2, 3)
    val result9 = nums9.scan(10)(_ + _) // List(10,10+1,10+1+2,10+1+2+3) = List(10, 11, 13, 16)


    //scanLeft: 从左向右进行scan函数的操作
    //scanRight：从右向左进行scan函数的操作
    val nums10 = List(1.0, 2.0, 3.0)
    val result10_1 = nums10.scanLeft(2.0)(math.pow) // List(2.0,pow(2.0,1.0), pow(pow(2.0,1.0),2.0),pow(pow(pow(2.0,1.0),2.0),3.0) = List(2.0,2.0,4.0,64.0)
    val result10_2 = nums10.scanRight(2.0)(math.pow) // List(2.0,pow(3.0,2.0), pow(2.0,pow(3.0,2.0)), pow(1.0,pow(2.0,pow(3.0,2.0))) = List(1.0,512.0,9.0,2.0)


    //左右截取函数
    // take & takeLeft(n: Int): List[A] 提取列表的前n个元素
    // takeRight: takeRight(n: Int): List[A] 提取列表的最后n个元素
    // takeWhile: takeWhile(p: (A) ⇒ Boolean): List[A] 从左向右提取列表的元素，直到条件p不成立
    val nums11 = List(1, 1, 1, 1, 4, 4, 4, 4)
    val left = nums11.take(4) // List(1,1,1,1)
    val right = nums11.takeRight(4) // List(4,4,4,4)
    //nums11.head 为首元素为1，直到不等于首元素则停止截取
    val headNums = nums11.takeWhile(_ == nums11.head) // List(1,1,1,1)


    //和take相反，drop是丢弃掉一些元素后剩下的元素集合
    //drop: drop(n: Int): List[A] 丢弃前n个元素，返回剩下的元素
    //dropRight: dropRight(n: Int): List[A] 丢弃最后n个元素，返回剩下的元素
    //dropWhile: dropWhile(p: (A) ⇒ Boolean): List[A] 从左向右丢弃元素，直到条件p不成立
    val nums12 = List(1, 1, 1, 1, 4, 4, 4, 4)
    val left2 = nums12.drop(4) // List(4,4,4,4)
    val right2 = nums12.dropRight(4) // List(1,1,1,1)
    val tailNums = nums12.dropWhile(_ == nums12.head) // List(4,4,4,4)


    //span : span(p: (A) ⇒ Boolean): (List[A], List[A]) 从左向右应用条件p进行判断，直到条件p不成立，此时将列表分为两个列表
    //splitAt: splitAt(n: Int): (List[A], List[A]) 将列表分为前n个，与，剩下的部分
    //partition: partition(p: (A) ⇒ Boolean): (List[A], List[A]) 将列表分为两部分，第一部分为满足条件p的元素，第二部分为不满足条件p的元素
    val nums13 = List(1, 1, 1, 2, 3, 2, 1)
    val (prefix, suffix) = nums13.span(_ == 1) // prefix = List(1,1,1), suffix = List(2,3,2,1)
    val (prefix1, suffix1) = nums13.splitAt(3) // prefix = List(1,1,1), suffix = List(2,3,2,1)
    val (prefix2, suffix2) = nums13.partition(_ == 1) // prefix = List(1,1,1,1), suffix = List(2,3,2)

    //padTo(len: Int, elem: A): List[A]  将列表扩展到指定长度，长度不够的时候，使用elem进行填充，否则不做任何操作。
    val nums14 = List(1, 1, 1)
    val padded = nums14.padTo(6, 2) // List(1,1,1,2,2,2)


    //排列组合
    //combinations: combinations(n: Int): Iterator[List[A]] 取列表中的n个元素进行组合，返回不重复的组合列表，结果一个迭代器
    //permutations: permutations: Iterator[List[A]] 对列表中的元素进行排列，返回不重得的排列列表，结果是一个迭代器
    val nums15 = List(1, 1, 3)
    val combinations = nums15.combinations(2).toList //List(List(1,1),List(1,3))
    val permutations = nums15.permutations.toList // List(List(1,1,3),List(1,3,1),List(3,1,1))


    //zip: zip[B](that: GenIterable[B]): List[(A, B)] 与另外一个列表进行拉链操作，将对应位置的元素组成一个pair，返回的列表长度为两个列表中短的那个
    //zipAll: zipAll[B](that: collection.Iterable[B], thisElem: A, thatElem: B): List[(A, B)] 与另外一个列表进行拉链操作，将对应位置的元素组成一个pair，若列表长度不一致，自身列表比较短的话使用thisElem进行填充，对方列表较短的话使用thatElem进行填充
    //zipWithIndex：zipWithIndex: List[(A, Int)] 将列表元素与其索引进行拉链操作，组成一个pair
    //unzip: unzip[A1, A2](implicit asPair: (A) ⇒ (A1, A2)): (List[A1], List[A2]) 解开拉链操作
    //unzip3: unzip3[A1, A2, A3](implicit asTriple: (A) ⇒ (A1, A2, A3)): (List[A1], List[A2], List[A3]) 3个元素的解拉链操作
    val alphabet = List("A","B","C")
    val nums16 = List(1,2)
    val zipped = alphabet zip nums16   // List(("A",1),("B",2))
    val zippedAll = alphabet.zipAll(nums16,"D",3)   // List(("A",1),("B",2),("C",3))
    val zippedIndex = alphabet.zipWithIndex  // List(("A",0),("B",1),("C",3))
    val (list1,list2) = zipped.unzip        // list1 = List("A","B"), list2 = List(1,2)
    val (l1,l2,l3) = List((1, "one", '1'),(2, "two", '2'),(3, "three", '3')).unzip3   // l1=List(1,2,3),l2=List("one","two","three"),l3=List('1','2','3')

    //slice(from: Int, until: Int): List[A] 提取列表中从位置from到位置until(不含该位置)的元素列表
    val nums17 = List(1,2,3,4,5)
    val sliced = nums17.slice(2,4)  //List(3,4)

    //sliding(size: Int, step: Int): Iterator[List[A]] 将列表按照固定大小size进行分组，步进为step，step默认为1,返回结果为迭代器
    val nums18 = List(1,1,2,2,3,3,4,4)
    val groupStep2 = nums18.sliding(2,2).toList  //List(List(1,1),List(2,2),List(3,3),List(4,4))
    val groupStep1 = nums18.sliding(2).toList //List(List(1,1),List(1,2),List(2,2),List(2,3),List(3,3),List(3,4),List(4,4))

    //updated(index: Int, elem: A): List[A] 对列表中的某个元素进行更新操作
    val nums19 = List(1,2,3,3)
    val fixed = nums19.updated(3,4)  // List(1,2,3,4)
  }

  /*
    转换对应关系:
    scala.collection.Iterable <=> Java.lang.Iterable
    scala.collection.Iterable <=> java.util.Collection
    scala.collection.Iterator <=> java.util.{ Iterator, Enumeration }
    scala.collection.mutable.Buffer <=> java.util.List
    scala.collection.mutable.Set <=> java.util.Set
    scala.collection.mutable.Map <=> java.util.{ Map, Dictionary }
    scala.collection.mutable.ConcurrentMap <=> java.util.concurrent.ConcurrentMap

    scala.collection.Seq         => java.util.List
    scala.collection.mutable.Seq => java.util.List
    scala.collection.Set         => java.util.Set
    scala.collection.Map         => java.util.Map
    java.util.Properties         => scala.collection.mutable.Map[String, String]

    显示转换需要引入以下包：
    import scala.collection.JavaConversions._
    //props(key) = value 将调用底层Properties对象的put(key,value)方法
    val props:scala.collection.mutable.Map[String,String] = System.getProperties()

    隐式转换需要引入以下包：
    import scala.collection.JavaConversions.mapAsJavaMap
    import scala.collection.JavaConversions.mapAsScalaMap
    更多请参考《快学Scala》201页

    下面以Java中Map和Scala中Map转换为例
    【Java Map自动隐式转换成Scala Map】
    import scala.collection.JavaConversions.mapAsScalaMap
    val javaScores = new java.util.HashMap[String, Int]()
    javaScores.put("Alice", 10)
    javaScores.put("Bob", 3)
    javaScores.put("Cindy", 8)
    // Java Map自动隐式转换 Scala Map
    val scalaScores: scala.collection.mutable.Map[String, Int] = javaScores
    【Scala Map自动隐式转换成Java Map】
    import scala.collection.JavaConversions.mapAsJavaMap
    import java.awt.font.TextAttribute._
    val scalaAttrMap = Map(FAMILY -> "Serif", SIZE -> 12)
    // Scala Map自动隐式转换 Java Map
    val font = new java.awt.Font(scalaAttrMap)
   */
  def  java集合互转换(): Unit = {
    import scala.collection.JavaConversions._
    val props:scala.collection.mutable.Map[String,String] = System.getProperties()
    props("prop1") = "val1"

    val map:scala.collection.mutable.Map[String,String] = new java.util.HashMap[String,String]()
    map + ("key2"->"val2")
    map.put("key1","val1")
  }

  def  并行集合(): Unit = {
    //普通集合转换成并行集合
    List(1,2).par
    //并行集合转换成普通集合
    List(1,2).par.seq
    //执行时会多个线程同时并行打印
    //Thread[ForkJoinPool-1-worker-5,5,main]  1
    //Thread[ForkJoinPool-1-worker-3,5,main]  3
    //Thread[ForkJoinPool-1-worker-7,5,main]  2
    //Thread[ForkJoinPool-1-worker-7,5,main]  5
    //Thread[ForkJoinPool-1-worker-1,5,main]  4
    (1 to 5).par.foreach{it => println(Thread.currentThread+"  " + it)}

    //并行过滤元素
    List(1,2,3).par.filter(_ % 2 == 0)
  }

  /**
   * Tuple类型测试
   */
  def tuple(): Unit = {
    val t = ("1",1) //Tuple2
    val (a,b) = t //a = "1",b=1

    val l = List((1,2),("1","2"))
    val (c,d) = l(0) //c:Any=1,d:Any=2
    //判断c是否是Int类型
    val f = c.isInstanceOf[Int] //true
    //c从Any类型转换成Int类型
    c.asInstanceOf[Int] //c:Int=1
  }

  /**
   * 集合中对象处理
   */
  def 集合中的对象() : Unit = {
   /* case class Person(name: String, isMale: Boolean, children: Person*)

    val lara = Person("Lara", false)
    val bob = Person("Bob", true)
    val julie = Person("Julie", false, lara, bob)

    val persons = List(lara, bob, julie)

    //用匹配模式过滤等于40岁的User
    persons.foreach(_ match {
      case Person(_name, isMale) if (isMale) => println("The User is :" + _name)
      case _ => None
    })

    persons.filter (_.isMale) flatMap (p => (p.children map (c => (p.name, c.name))))
    persons filter (p => !p.isMale) flatMap (p =>(p.children map (c => (p.name, c.name))))*/

    val obj = EMail.unapply("Alex@www")
    obj match {
      case Some((u, d)) => u
    }
  }

}


object EMail {

  // The injection method (optional)
  def apply(user: String, domain: String) = user +"@"+ domain

  // The extraction method (mandatory)
  def unapply(str: String): Option[(String, String)] = {
    val parts = str split "@"
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }
}
