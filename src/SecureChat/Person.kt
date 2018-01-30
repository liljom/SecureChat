package SecureChat

data class Person(val name: String, val IP: String, val port: Integer, val key: ByteArray)
{
    override fun toString(): String = name
}