package hr.fika.budge.models.bank

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Locations : IntIdTable(columnName = "idlocation") {
    val street = varchar("street", 50)
    val latitude = decimal("latitude", 10, 8)
    val longitude = decimal("longitude", 10, 8)
    val bankId = integer("bankid").uniqueIndex().references(Banks.id)
}
class DbAtmLocation(val idLocation: EntityID<Int>) : IntEntity(idLocation) {
    companion object : IntEntityClass<DbAtmLocation>(Locations)

    var street by Locations.street
    var latitude by Locations.latitude
    var longitude by Locations.longitude
    var bankId by Locations.bankId

    fun toAtmLocation() = AtmLocation(
        idLocation.value,
        street,
        latitude.toDouble(),
        longitude.toDouble(),
        bankId
    )
    override fun toString() = "Street - $street Latitude - $latitude Longitude - $longitude"
}