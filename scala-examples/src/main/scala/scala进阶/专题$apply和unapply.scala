package scala进阶

/**
 apply方法
 通常，在一个类的半生对象中定义apply方法，在生成这个类的对象时，就省去了new关键字。

 unapply方法
 可以认为unapply方法是apply方法的反向操作，apply方法接受构造参数变成对象，而unapply方法接受一个对象，从中提取值。
 */
object 专题$apply和unapply extends App {
    //模式匹配类
    class Person
    class Student extends Person
    val p: Person = new Student
    p match {
        case per: Person => println("it's Person's object")
        case _  => println("unknown type")
    }

    //模式匹配：邮件地址
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

    //模式匹配：货币转换
    class Currency(val value: Double, val unit: String)
    object Currency{
        def apply(value: Double, unit: String): Currency = new Currency(value, unit)
        def unapply(currency: Currency): Option[(Double, String)] = {
            if (currency == null){
                None
            }
            else{
                Some(currency.value, currency.unit)
            }
        }
    }
    val currency = Currency(30.2, "USD")
    currency match {
        case Currency(amount, "USD") => println("$" + amount)
        case _ => println("No match.")
    }
}
