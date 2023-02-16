package hr.fika.budge.dal.bank

import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.models.bank.*
import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

object BankService {
    fun getAllBanks(): List<Bank> {
        val list = mutableListOf<Bank>()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Banks)
            list.addAll(DbBank.all().map { it.toBank() })
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
        var account: BankAccount? = null
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

    fun addTransaction(transaction: Transaction) {
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Transactions)
            DbTransaction.new {
                description = transaction.description!!
                amount = transaction.amount!!.toBigDecimal()
                reoccurring = transaction.reoccurring!!
                transactionTimestamp = Instant.ofEpochMilli(transaction.transactionTimestamp!!)
                accountId = transaction.accountId!!

            }
        }
    }

    fun deleteTransaction(transactionId: Int) {
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Transactions)
            DbTransaction.findById(transactionId)?.delete()
        }
    }

    fun getFlow(accountId: Int): List<Transaction> {
        val list = mutableListOf<Transaction>()
        transaction(DatabaseProvider.provideDb()) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Transactions)
            DbTransaction.find { Transactions.accountId eq accountId }
                .filter { it.reoccurring }
                .forEach { dbTransaction ->
                    list.add(dbTransaction.toTransaction())
                }
        }
        return list
    }

    fun getPendingPayment(accountId: Int): Boolean {
        val flow = getFlow(accountId)
        flow.forEach { transaction ->
            val paymentTimestamp = Instant.ofEpochMilli(transaction.transactionTimestamp!!)
            val paymentDate = paymentTimestamp.atZone(ZoneId.systemDefault()).toLocalDate()
            if (paymentDate.minusDays(3) == LocalDate.now()) {
                return true
            }
        }
        return false
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

    fun getBudgetCalculation(accountId: Int) : BudgetCalculationInfo {
        val flow = getFlow(accountId)
        val netMonthlyChange = flow.sumOf { transaction -> transaction.amount!! }
        var total = 0.0
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Transactions)
            DbTransaction.find { Transactions.accountId eq accountId }
                .filter { !it.reoccurring }
                .forEach { dbTransaction ->
                    total += dbTransaction.amount.toDouble()
                }
        }
        return BudgetCalculationInfo(flow, total, netMonthlyChange)
    }

    fun getBudgets(userID: Int): List<Budget> {
        val list = mutableListOf<Budget>()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Budgets)
            DbBudget.find { Budgets.userId eq userID }
                .forEach { dbBudget ->
                    list.add(dbBudget.toBudget())
                }
        }
        return list
    }

    fun addBudget(budget: Budget) {
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Budgets)
            DbBudget.new {
                description = budget.description!!
                amount = budget.amount!!.toBigDecimal()
                budgetDate = Instant.ofEpochMilli(budget.budgetDate!!)
                userId = budget.userId!!
            }
        }
    }

    fun getOverspend(userId: Int): Boolean {
        val accountId = getBankAccount(userId)
        accountId?.let {
            val projections = getBudgetsAndProjections(userId, it.idBankAccount!!)

            return projections.count { budgetProjection -> !budgetProjection.isOnTarget } != 0
        }
        return false
    }

    fun getBudgetsAndProjections(userId: Int, accountId: Int): List<BudgetProjection> {
        val budgets = getBudgets(userId)
        val calcData = getBudgetCalculation(accountId)
        return budgets.map { calculateBudgetProjection(it, calcData, budgets) }
    }

    private fun calculateBudgetProjection(
        budget: Budget,
        calculationInfo: BudgetCalculationInfo,
        budgets: List<Budget>
    ): BudgetProjection {
        val targetDate = getLocalDateFromTimestamp(budget.budgetDate!!)
        var projection = 0.0
        for (transaction in calculationInfo.flow) {
            var comparisonDate = LocalDate.now()
            val transactionDate = getLocalDateFromTimestamp(transaction.transactionTimestamp!!)
            while (comparisonDate.isBefore(targetDate)) {
                if (transactionDate.dayOfMonth < comparisonDate.dayOfMonth) {
                    projection += transaction.amount!!
                }
                comparisonDate = comparisonDate.plusMonths(1)
            }
        }
        // val budgetCalculation = budgets.sumOf { it.amount!! } - budget.amount!!
        var budgetCalculation = 0.0
        budgets.forEach { b ->
            if (!getLocalDateFromTimestamp(b.budgetDate!!).isAfter(targetDate)) {
                budgetCalculation += b.amount!!
            }
        }
        val netChange = projection + calculationInfo.total - (budgetCalculation - budget.amount!!)
        val comparisonToBudget = (netChange - budget.amount).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
        return BudgetProjection(budget, comparisonToBudget, comparisonToBudget >= 0)
    }

    fun deleteBudget(budgetId: Int) {
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Budgets)
            DbBudget.findById(budgetId)?.delete()
        }
    }

    private fun getLocalDateFromTimestamp(timestamp: Long) : LocalDate {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}