import org.apache.spark.{SparkContext, SparkConf}

/**
 *
 * @author  wangjc
 *          2018/7/13
 */
object SimpleApp {
  def main(args: Array[String]) {
    //输入文件既可以是本地linux系统文件，也可以是其它来源文件，例如HDFS
    val logFile = Thread.currentThread().getContextClassLoader().getResource("test.txt").getPath

    //以本地线程方式运行，可以指定线程个数，
    //如.setMaster("local[2]")，两个线程执行
    //下面给出的是单线程执行
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    //wordcount操作，计算文件中包含Spark的行数
    val count=sc.textFile(logFile).filter(line => line.contains("Spark")).count()
    //打印结果
    println("count="+count)
    sc.stop()
  }
}
