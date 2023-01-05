package hr.fika.budge.dal.db

import hr.fika.budge.Secrets
import org.jetbrains.exposed.sql.Database

object DatabaseProvider {
    fun provideDb() : Database = Database.connect(
        url = Secrets.DB_URL.value,
        driver = Secrets.DB_DRIVER.value,
        user = Secrets.DB_USER.value,
        password = Secrets.DB_PASS.value
    )
}