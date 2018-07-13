import java.io.{PrintWriter, File}

/**
 * 抽象控制实例
 * @author  wangjc
 *          2018/6/27
 */
object 测试 {
  def main(args: Array[String]) {
    for(f<-搜索文件第3版_柯里化.ends(new File("D:\\Personal\\books"),"doc")) println(f)

    //测试until函数
    var x = 10
    until(x==0){
      x -= 1
      println(x)
    }

    //测试thread函数,自己定义线程函数后，调用一个线程是如此的简单
    thread({println("....");Thread.sleep(5000);println("End.")})
  }

  /**
   * 定义一个和while循环相似的关键字
   * @param condition
   * @param block
   * @return
   */
  def until(condition: => Boolean)(block: =>Unit) {
    if(!condition){
      block
      //柯里化
      until(condition)(block)
    }
  }

  /**
   * 简化定义
   */
  def simplization():Unit = {
    //==============初版=============
    //判断是否列表中存在负数
    def containsNeg0(nums: List[Int]): Boolean = {
      var exists = false
      for (num <- nums)
        if (num < 0)
          exists = true
      exists
    }
    //判断是否列表中包含奇数
    def containsOdd0(nums: List[Int]): Boolean = {
      var exists = false
      for (num <- nums)
        if (num % 2 == 1)
          exists = true
      exists
    }

    //==============简化版=============
    def containsNeg(nums: List[Int]) = nums.exists(_ < 0)
    def containsOdd(nums: List[Int]) = nums.exists(_ % 2 == 1)
  }

  /**
   * 柯里化
   */
  def currying():Unit = {
    //方式一
    def curriedSum(x: Int)(y: Int) = x + y
    //方式二
    def first(x: Int) = (y: Int) => x + y

    //通过在调用第一个参数的函数后面加“_”获取到第二个参数的函数
    val onePlus = curriedSum(1)_

    //一个打印例子
    def withPrintWriter(file: File)(op: PrintWriter => Unit) {
      val writer = new PrintWriter(file)
      try {
        op(writer)
      } finally {
        writer.close()
      }
    }
    val file = new File("date.txt")
    withPrintWriter(file)(writer => writer.println(new java.util.Date)) //后面参数用()括号
    withPrintWriter(file){writer => writer.println(new java.util.Date)} //后面参数用{}括号

  }

  /**
   * 定义一个用来创建线程的关键字
   * @param block 相当于block: ()=>Unit,可以把一个块作为参数传递进来
   */
  def thread(block: =>Unit) {
    new Thread(){
      override def run() {block}
    }.start()
  }

  /**
   * 类型推演
   */
  object 搜索文件第1版_类型推演 {
    private def list(file: File, query: String,
                     matcher: (String, String) => Boolean) = {
      for (file <- file.listFiles if matcher(file.getName, query))
        yield file
    }

    def ends(file: File, ext: String) =
      list(file, ext, _.endsWith(_))

    def contains(file: File, query: String) =
      list(file, query, _.contains(_))

    def matches(file: File, regex: String) =
      list(file, regex, _.matches(_))

  }

  /**
   * 类型别名
   */
  object 搜索文件第2版_别名 {
    private type Matcher = (String, String) => Boolean

    private def list(file: File, query: String, matcher: Matcher) = {
      for (file <- file.listFiles if matcher(file.getName, query))
        yield file
    }

    def ends(file: File, ext: String) =
      list(file, ext, _.endsWith(_))

    def contains(file: File, query: String) =
      list(file, query, _.contains(_))

    def matches(file: File, regex: String) =
      list(file, regex, _.matches(_))
  }

  /**
   * 柯里化
   */
  object 搜索文件第3版_柯里化 {
    private type Matcher = String => Boolean

    def list(file: File)(matcher: Matcher) =
      file.listFiles.filter(f => matcher(f.getName))

    def ends(file: File, ext: String) =
      //list()()当只有一个参数时可以写成list(){},可以用{}代替()
      list(file) { _.endsWith(ext) }

    def contains(file: File, query: String) =
      list(file) { _.contains(query) }

    def matches(file: File, regex: String) =
      list(file) { _.matches(regex) }
  }
}

