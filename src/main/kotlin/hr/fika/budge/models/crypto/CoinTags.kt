package hr.fika.budge.models.crypto

enum class CoinTags(val tag: String, val uuid: String, val icon: String) {
    BTC("BTC","Qwsogvtv82FCd", "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"),
    ETH("ETH", "razxDUgYGNAdQ", "https://cdn.coinranking.com/rk4RKHOuW/eth.svg"),
    USDC("USDC", "aKzUVe4Hh_CON", "https://cdn.coinranking.com/jkDf8sQbY/usdc.svg"),
    BUSD("BUSD", "vSo2fu9iE1s0Y", "https://cdn.coinranking.com/6SJHRfClq/busd.svg"),
    DOGE("DOGE", "a91GCGd_u96cF", "https://cdn.coinranking.com/H1arXIuOZ/doge.svg"),
    MATIC("MATIC", "uW2tk-ILY0ii", "https://cdn.coinranking.com/M-pwilaq-/polygon-matic-logo.svg"),
    LINK("LINK", "VLqpJwogdhHNb", "https://cdn.coinranking.com/9NOP9tOem/chainlink.svg"),
    SHIB("SHIB", "xz24e0BjL", "https://cdn.coinranking.com/fiZ4HfnRR/shib.png"),
    LTC("LTC", "D7B1x_ks7WhV5", "https://cdn.coinranking.com/BUvPxmc9o/ltcnew.svg"),
}