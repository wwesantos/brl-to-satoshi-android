package co.javacafe.satoshirealconverter
import java.math.BigDecimal
import java.text.SimpleDateFormat



val MILLI_BTC: BigDecimal = BigDecimal("1000")
val MICRO_BTC: BigDecimal = BigDecimal("1000000")
val SATOSHI_BTC: BigDecimal = BigDecimal("1000000000")
val DATE_FORMAT = SimpleDateFormat("HH:mm dd-MMM")
enum class ValueType {BRL, BTC, MILLI_BTC, MICRO_BTC, SATOSHI_BTC}