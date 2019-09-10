package scala算法

/**
  * 队列
  * @author: wangjc
  *  2019/9/10
  */
class CircleQueue(val maxSize:Int) {
    val queue = new Array[Int](maxSize)
    var head:Int = 0
    var tail:Int = 0

    def isFull()  = {
        (this.tail + 1) % this.maxSize == this.head
    }

    def isEmpty() = {
        this.tail == this.head
    }

    def push(value:Int): Unit = {
        if(!isFull){
            this.queue(this.tail) = value
            this.tail = (this.tail + 1) % this.maxSize
        }
    }

    def pop():Int = {
        var result:Int = -1
        if(!isEmpty){
            result = this.queue(this.head)
            this.head = (this.head + 1) % this.maxSize
        }
        result
    }

}

object CircleQueue extends App {
    val queue = new CircleQueue(10)
    queue.push(1)
    queue.push(2)
    queue.push(3)
    println(queue.pop())
    println(queue.pop())
    println(queue.pop())
    println(queue.pop())
}

