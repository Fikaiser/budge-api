package hr.fika.budge.models.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable(columnName = "idUser") {
    val nickname = varchar("nickname", 50)
    val email = varchar("email", 50)
    val passHash = varchar("passhash", 200)
}

class DbUser(val idUser: EntityID<Int>) : IntEntity(idUser) {
    companion object : IntEntityClass<DbUser>(Users)

    var nickname by Users.nickname
    var email by Users.email
    var passHash by Users.passHash

    override fun toString() = "User ID - $idUser Name - $nickname Email - $email"
}