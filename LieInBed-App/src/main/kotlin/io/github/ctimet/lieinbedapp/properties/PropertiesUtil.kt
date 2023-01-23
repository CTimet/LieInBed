package io.github.ctimet.lieinbedapp.properties

import org.slf4j.LoggerFactory
import java.util.*

class PropertiesUtil {
    companion object {
        private val logger = LoggerFactory.getLogger(Properties::class.java)
        @JvmStatic
        fun check(check: Properties, checker: HashMap<String, ValueChecker<String>>, default: Properties) {
            val waitChange: LinkedList<Any> = LinkedList()
            check.forEach { k, v ->
                if (checker[k]?.check(v as String) == false) {
                    logger.info("键 $k 对应的值 $v 有问题，值 $v 无法通过ValueChecker检查，请重新设置。值已重置。")
                    waitChange.add(k)
                }
            }
            waitChange.forEach {
                check.remove(it)
            }
        }
    }
}