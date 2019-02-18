import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  * @author: wangjc
  *          2019/2/18
  */
object SparkApp {
    val logFile = Thread.currentThread().getContextClassLoader().getResource("test.txt").getPath

    def main(args: Array[String]): Unit = {
        sortByKey()

    }

    /**
      * 如果我们在rdd上调用mapPartition（func）方法，则func（）操作将在每个分区上而不是在每一行上调用。
      * 在这种特殊情况下，它将被称为10次（分区数）。通过这种方式，你可以在涉及时间关键的应用程序时阻止一些处理。
      * trasfermation
      */
    def mapPartition(): Unit = {
        //输入文件既可以是本地linux系统文件，也可以是其它来源文件，例如HDFS

        //以本地线程方式运行，可以指定线程个数，
        //如.setMaster("local[2]")，两个线程执行
        //下面给出的是单线程执行
        val conf = new SparkConf().setAppName("Count").setMaster("local[2]")
        val sc = new SparkContext(conf)
        // 看看文件中非数数字单词有多少个
        sc.textFile(logFile).mapPartitions(partiton => {
            //正式情况下，这里不能调用size()方法
            println("Partiton Size:"+partiton.size)
            /*while (partiton.hasNext){
                //line => partiton.next()
            }*/
            partiton.map(
                line => {
                    line
                }
            )
        }).foreach(println) //因为上面调用了size()方法，所以这里打印不出东西

        //和mapPartitions差不多，就是多了个分区索引(index)
        sc.textFile(logFile).mapPartitionsWithIndex((index,partiton) => {
            //正式情况下，这里不能调用size()方法
            println("Index :"+index)
            partiton.map(
                line => {
                    line
                }
            )
        }).foreach(println)

        sc.stop()
    }


    /**
      * 统计单纯数量
      * action
      */
    def count(): Unit = {
        //以本地线程方式运行，可以指定线程个数，
        //如.setMaster("local[2]")，两个线程执行
        //下面给出的是单线程执行
        val conf = new SparkConf().setAppName("Count").setMaster("local[2]")
        val sc = new SparkContext(conf)
        // 看看文件中非数数字单词有多少个
        val count = sc.textFile(logFile).flatMap(_.split("\t")).filter(_.trim().matches("\\D+")).count()
        println("count=" + count)

        sc.stop()
    }

    /**
      * 先局部聚合运算，然后再整体运算，运算时可以传入一个初始值
      * 参考: https://blog.csdn.net/jiaotongqu6470/article/details/78457966
      * trasfermation
      */
    def aggregateByKey(): Unit = {
        //以本地线程方式运行，可以指定线程个数，
        //如.setMaster("local[2]")，两个线程执行
        //下面给出的是单线程执行
        val conf = new SparkConf().setAppName("Count").setMaster("local[2]")
        val sc = new SparkContext(conf)

        //先定义一个各种动物数量的RDD
        val pairRdd = sc.parallelize(List(
            ("cat", 2), ("cat", 5),
            ("mouse", 4), ("cat", 12),
            ("dog", 12), ("mouse", 2)), 2)
        /*
          第一步：将每个分区内key相同数据放到一起
             分区一
            ("cat",(2,5)),("mouse",4)
             分区二
            ("cat",12),("dog",12),("mouse",2)
          第二步：局部求最大值
             对每个分区应用传入的第一个函数，math.max(_ , _)，这个函数的功能是求每个分区中每个key的最大值
             这个时候要特别注意，aggregateByKe(100)(math.max(_ , _),_+_)里面的那个100，其实是个初始值
             在分区一中求最大值的时候,100会被加到每个key的值中，这个时候每个分区就会变成下面的样子
             分区一
            ("cat",(2,5，100)),("mouse",(4，100))
            然后求最大值后变成：
            ("cat",100), ("mouse",100)
             分区二
            ("cat",(12,100)),("dog",(12.100)),("mouse",(2,100))
             求最大值后变成：
            ("cat",100),("dog",100),("mouse",100)
          第三步：整体聚合，将上一步的结果进一步的合成，这个时候100不会再参与进来
              最后结果就是：
              (dog,100),(cat,200),(mouse,200)
        */
        pairRdd.aggregateByKey(100)(math.max(_ , _),  _ + _ ).foreach(println)

        sc.stop()
    }

    /**
      * 先聚合在分组
      * trasfermation
      */
    def reduceBy(): Unit = {
        //以本地线程方式运行，可以指定线程个数，
        //如.setMaster("local[2]")，两个线程执行
        //下面给出的是单线程执行
        val conf = new SparkConf().setAppName("Count").setMaster("local[2]")
        val sc = new SparkContext(conf)

        val rdd = sc.textFile(logFile).map(line => {
            val t = line.split("\t");
            (t(0), t(1).trim.toInt)
        }).reduceByKey(_ + _).foreach(print)

        sc.stop()
    }

    def sortByKey(): Unit ={
        val conf = new SparkConf().setAppName("Count").setMaster("local[2]")
        val sc = new SparkContext(conf)
        // 看看文件中非数数字单词有多少个
        sc.textFile(logFile).map(line => {
            val t = line.split("\t");
            (t(0), t(1).trim.toInt)
        }).sortByKey(true).foreach(tuple => println(tuple._2 + "->" + tuple._1))
    }
}
