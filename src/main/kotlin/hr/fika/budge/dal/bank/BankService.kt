package hr.fika.budge.dal.bank

import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.models.bank.*
import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDate
import kotlin.random.Random

object BankService {
    fun getAllBanks(): List<DbBank> {
        val list = mutableListOf<DbBank>()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Banks)
            list.addAll(DbBank.all().toList())
        }
        return list
    }

    fun registerBankAccount(userId: Int, bankId: Int): BankAccount {
        var account = BankAccount()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Users)
            SchemaUtils.create(BankAccounts)
            SchemaUtils.create(Transactions)
            val check = DbBankAccount.find { BankAccounts.userId eq userId }.toList()
            if (check.isEmpty()) {
                account = DbBankAccount.new {
                    this.userId = userId
                    this.bankId = bankId
                }.toBankAccount()
                commit()
                DbTransaction.new {
                    description = "Initial balance"
                    transactionTimestamp = Instant.now()
                    amount = (1000.00).toBigDecimal()
                    reoccurring = false
                    this.accountId = account.idBankAccount!!
                }
                for (i in 1..5) {
                    generateTransactions(account.idBankAccount!!)
                }
            }
        }
        return account
    }

    fun getBankAccount(userId: Int): BankAccount? {
        var account : BankAccount? = null
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(BankAccounts)
            val check = DbBankAccount.find { BankAccounts.userId eq userId }.toList()
            check.firstOrNull()?.let { account = it.toBankAccount() }
        }
        return account
    }

    fun getBankAtmLocations(bankId: Int): List<AtmLocation> {
        val list = mutableListOf<AtmLocation>()
        transaction(DatabaseProvider.provideDb()) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Locations)
            DbAtmLocation.find { Locations.bankId eq bankId }.forEach { dbAtmLocation ->
                list.add(dbAtmLocation.toAtmLocation())
            }
        }
        return list
    }

    fun getTransactions(accountId: Int): List<Transaction> {
        val list = mutableListOf<Transaction>()
        transaction(DatabaseProvider.provideDb()) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Transactions)
            DbTransaction.find { Transactions.accountId eq accountId }.forEach { dbTransaction ->
                list.add(dbTransaction.toTransaction())
            }
        }
        return list
    }

    private fun generateTransactions(accountId: Int) {
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Transactions)
            DbTransaction.new {
                description = "Transaction"
                transactionTimestamp = Instant.now()
                amount = Random.nextDouble(-100.00, 100.00).toBigDecimal()
                reoccurring = false
                this.accountId = accountId
            }
        }
    }
}