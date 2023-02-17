package hr.fika.budge.models.crypto

enum class CoinTags(val tag: String, val uuid: String, val icon: String) {
    BTC("BTC","Qwsogvtv82FCd", "https://cryptologos.cc/logos/bitcoin-btc-logo.png?v=024"),
    ETH("ETH", "razxDUgYGNAdQ", "https://cryptologos.cc/logos/ethereum-eth-logo.png?v=024"),
    USDC("USDC", "aKzUVe4Hh_CON", "https://cryptologos.cc/logos/usd-coin-usdc-logo.png?v=024"),
    BUSD("BUSD", "vSo2fu9iE1s0Y", "https://cryptologos.cc/logos/binance-usd-busd-logo.png?v=024"),
    DOGE("DOGE", "a91GCGd_u96cF", "https://cryptologos.cc/logos/dogecoin-doge-logo.png?v=024"),
    MATIC("MATIC", "uW2tk-ILY0ii", "https://cryptologos.cc/logos/polygon-matic-logo.png?v=024"),
    LINK("LINK", "VLqpJwogdhHNb", "https://cryptologos.cc/logos/chainlink-link-logo.png?v=024"),
    SHIB("SHIB", "xz24e0BjL", "https://cryptologos.cc/logos/shiba-inu-shib-logo.png?v=024"),
    LTC("LTC", "D7B1x_ks7WhV5", "https://cryptologos.cc/logos/litecoin-ltc-logo.png?v=024"),
}