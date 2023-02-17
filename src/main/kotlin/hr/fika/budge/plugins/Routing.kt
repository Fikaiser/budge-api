package hr.fika.budge.plugins


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import hr.fika.budge.JwtSecrets
import hr.fika.budge.dal.bank.BankService
import hr.fika.budge.dal.crypto.CoinRepository
import hr.fika.budge.dal.crypto.CryptoService
import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.dal.stock.StockRepository
import hr.fika.budge.dal.stock.StockService
import hr.fika.budge.models.bank.Budget
import hr.fika.budge.models.bank.Transaction
import hr.fika.budge.models.user.DbUser
import hr.fika.budge.models.user.User
import hr.fika.budge.models.user.Users
import hr.fika.budge.utils.EncryptionManager
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.TimeUnit


fun Application.configureRouting() {
    routing {

        get("/test") {
            val text = "Hello from API"
            call.respondText(text)
        }

        get("/payment") {
            val accountId = call.request.queryParameters["accountId"]
            accountId?.let {
                call.respond(BankService.getPendingPayment(it.toInt()))
            }
            call.respond(false)
        }

        get("/overspend") {
            val userId = call.request.queryParameters["userId"]
            userId?.let {
                call.respond(BankService.getOverspend(it.toInt()))
            }
            call.respond(false)
        }

        post("/login") {
            val params = call.request.queryParameters
            val email = params["email"]
            val pass = params["pass"]
            var user: DbUser? = null

            if (!email.isNullOrBlank() && !pass.isNullOrBlank()) {
                transaction(DatabaseProvider.provideDb()) {
                    SchemaUtils.create(Users)
                    val dbUser = DbUser.find { Users.email like email }.firstOrNull()
                    if (dbUser != null && EncryptionManager.verifyPassword(pass, dbUser.passHash)) {
                        user = dbUser
                    }
                }
                if (user != null) {
                    val account = BankService.getBankAccount(user!!.idUser.value)
                    val wallet = CryptoService.getCryptoWallet(user!!.idUser.value)
                    val portfolio = StockService.getStockPortfolio(user!!.idUser.value)
                    val token = JWT.create()
                        .withAudience(JwtSecrets.AUDIENCE.value)
                        .withIssuer(JwtSecrets.ISSUER.value)
                        .withClaim("username", user!!.nickname)
                        .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(45)))
                        .sign(Algorithm.HMAC256(JwtSecrets.SECRET.value))
                    val response = User(
                        user!!.idUser.value,
                        user!!.nickname,
                        token,
                        bankAccount = account,
                        cryptoWalletId = wallet?.idWallet,
                        stockPortfolioId = portfolio?.idStockPortfolio)
                    call.respond(response)
                } else {
                    call.respondText(status = HttpStatusCode.BadRequest, text = "Such user not found")
                }
            } else {
                call.respondText(status = HttpStatusCode.BadRequest, text = "")
            }

        }

        post("/register") {
            val params = call.request.queryParameters
            val nickname = params["nickname"]
            val email = params["email"]
            val pass = params["pass"]

            if (!nickname.isNullOrBlank() && !email.isNullOrBlank() && !pass.isNullOrBlank()) {

                val hash = EncryptionManager.encryptPassword(pass)
                transaction(DatabaseProvider.provideDb()) {
                    SchemaUtils.create(Users)
                    DbUser.new {
                        this.nickname = nickname
                        this.email = email
                        this.passHash = hash
                    }
                }
                call.respondText("Added")
            } else {
                call.respondText("Failed")
            }
        }

        authenticate("auth") {
            get("/hello") {
                call.respondText("Hello world")
            }

            get("/atms") {
                val params = call.request.queryParameters
                params["bankId"]?.let {
                    val bankId = it.toInt()
                    val locations = BankService.getBankAtmLocations(bankId)
                    call.respond(locations)
                }

            }

            get("/banks") {
                call.respond(BankService.getAllBanks())
            }

            get("/bankaccount") {
                val params = call.request.queryParameters
                val userId = params["userId"]
                userId?.let {
                    val account = BankService.getBankAccount(it.toInt())
                    if (account != null) {
                        call.respond(account)
                    } else {
                        call.respond("")
                    }
                }
            }

            get("/transactions") {
                val params = call.request.queryParameters
                val bankAccountId = params["bankAccountId"]?.toInt()
                bankAccountId?.let {
                    val transactions = BankService.getTransactions(it)
                    call.respond(transactions)
                }
            }

            post("/transactions") {
                val transaction = call.receive(Transaction::class)
                BankService.addTransaction(transaction)
            }

            delete("/transactions") {
                val params = call.request.queryParameters
                val transactionId = params["transactionId"]?.toInt()
                BankService.deleteTransaction(transactionId!!)
            }

            get("/flow") {
                val params = call.request.queryParameters
                val bankAccountId = params["bankAccountId"]?.toInt()
                bankAccountId?.let {
                    val transactions = BankService.getFlow(it)
                    call.respond(transactions)
                }
            }

            post("/bankaccount") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                val bankId = params["bankId"]?.toInt()
                if (userId != null && bankId != null) {
                    val account = BankService.registerBankAccount(userId, bankId)
                    if (account.idBankAccount != null) {
                        call.respond(account)
                    }
                }
                call.respond("")
            }

            post("/wallet") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                if (userId != null) {
                    val wallet = CryptoService.registerCryptoWallet(userId)
                    if (wallet.idWallet != null) {
                        call.respond(wallet)
                    }
                }
                call.respond("")
            }

            get("/wallet") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                userId?.let {
                    val wallet = CryptoService.getCryptoWallet(it)
                    if (wallet != null) {
                        call.respond(wallet)
                    } else {
                        call.respond("")
                    }
                }
            }

            get("/cryptobalance") {
                val params = call.request.queryParameters
                val walletId = params["walletId"]?.toInt()
                walletId?.let {
                    val balances = CryptoService.getCryptoBalances(it)
                    balances.forEach { balance ->
                        balance.price = CoinRepository.getCoinPrice(balance.tag!!)
                    }
                    call.respond(balances)
                }
            }

            get("/cryptohistory") {
                val params = call.request.queryParameters
                val tag = params["tag"]
                val period = params["period"]
                if (tag != null && period != null) {
                    val history = CoinRepository.getCoinPriceHistory(tag, period )
                    history?.let {
                        call.respond(it)
                    }
                }
            }

            post("/portfolio") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                if (userId != null) {
                    val portfolio = StockService.registerStockPortfolio(userId)
                    if (portfolio.idStockPortfolio != null) {
                        call.respond(portfolio)
                    }
                }
                call.respond("")
            }

            get("/portfolio") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                userId?.let {
                    val portfolio = StockService.getStockPortfolio(it)
                    if (portfolio != null) {
                        call.respond(portfolio)
                    } else {
                        call.respond("")
                    }
                }
            }

            get("/stockbalance") {
                val params = call.request.queryParameters
                val portfolioId = params["portfolioId"]?.toInt()
                portfolioId?.let {
                    val balances = StockService.getStockBalances(it)
                    balances.forEach { balance ->
                        balance.price = StockRepository.getStockPrice(balance.tag!!)
                    }
                    call.respond(balances)
                }
            }

            get("/stockhistory") {
                val params = call.request.queryParameters
                val tag = params["tag"]
                val interval = params["interval"]
                if (tag != null && interval != null) {
                    var realInterval = ""
                    var outputSize = 0
                    when (interval) {
                        "month" -> {
                            realInterval = "1day"
                            outputSize = 30
                        }
                        "year" -> {
                            realInterval = "1day"
                            outputSize = 356
                        }
                        else -> {
                        realInterval = "1day"
                        outputSize = 7
                    }
                    }
                    val history = StockRepository.getStockPriceHistory(tag, realInterval, outputSize)
                    history?.let {
                        call.respond(it)
                    }
                }
            }

            get("/budgetcalculation"){
                val params = call.request.queryParameters
                val accountId = params["accountId"]
                accountId?.let {
                    val data = BankService.getBudgetCalculation(it.toInt())
                    call.respond(data)
                }
                call.respond("Error")
            }

            get("/budgets") {
                val params = call.request.queryParameters
                val userId = params["userId"]?.toInt()
                val accountId = params["accountId"]?.toInt()
                if (userId != null && accountId != null) {
                    val budgets = BankService.getBudgetsAndProjections(userId, accountId)
                    call.respond(budgets)
                }
            }

            post("/budgets") {
                val budget = call.receive(Budget::class)
                BankService.addBudget(budget)
                call.respond("OK")
            }

            delete("/budgets") {
                val params = call.request.queryParameters
                val budgetId = params["budgetId"]?.toInt()
                budgetId?.let {
                    BankService.deleteBudget(it)
                    call.respond("Deleted")
                }
                call.respond("")
            }

        }
    }
}
