package hr.fika.budge.dal.crypto

import hr.fika.budge.dal.db.DatabaseProvider
import hr.fika.budge.models.crypto.*
import hr.fika.budge.models.user.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

object CryptoService {

    fun getCryptoWallet(userId: Int): CryptoWallet? {
        var wallet: CryptoWallet? = null
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(CryptoWallets)
            val check = DbCryptoWallet.find { CryptoWallets.userId eq userId }.toList()
            check.firstOrNull()?.let { wallet = it.toCryptoWallet() }
        }
        return wallet
    }

    fun registerCryptoWallet(userId: Int): CryptoWallet {
        var wallet = CryptoWallet()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(Users)
            SchemaUtils.create(CryptoWallets)
            SchemaUtils.create(CryptoBalances)
            val check = DbCryptoWallet.find { CryptoWallets.userId eq userId }.toList()
            if (check.isEmpty()) {
                wallet = DbCryptoWallet.new {
                    this.userId = userId
                }.toCryptoWallet()
                commit()
                for (i in 1..3) {
                    generateCryptoBalance(wallet.idWallet!!)
                }
            } else {
                wallet = check.first().toCryptoWallet()
            }
        }
        return wallet
    }

    fun getCryptoBalances(walletId: Int): List<CryptoBalance> {
        val list = mutableListOf<CryptoBalance>()
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(CryptoBalances)
            DbCryptoBalance.find { CryptoBalances.walletId eq walletId }.forEach { dbCryptoBalance ->
                list.add(dbCryptoBalance.toCryptoBalance())
            }
        }
        return list
    }

    private fun generateCryptoBalance(idWallet: Int) {
        val rand = Random.nextInt(0, CoinTags.values().size - 1)
        val coin = CoinTags.values().toList()[rand]
        transaction(DatabaseProvider.provideDb()) {
            SchemaUtils.create(CryptoBalances)
            DbCryptoBalance.new {
                this.tag = coin.tag
                amount = Random.nextDouble(0.000, 1.000).toBigDecimal()
                walletId = idWallet
            }
        }
    }
}
