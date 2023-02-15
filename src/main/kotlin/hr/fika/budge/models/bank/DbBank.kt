package hr.fika.budge.models.bank

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Banks : IntIdTable(columnName = "idbank") {
    val name = varchar("name", 50)
}
class DbBank(val idBank: EntityID<Int>) : IntEntity(idBank) {
    companion object : IntEntityClass<DbBank>(Banks)

    var name by Banks.name

    fun toBank() = Bank(idBank.value, name)

    override fun toString() = "Bank ID - $idBank Name - $name"
}