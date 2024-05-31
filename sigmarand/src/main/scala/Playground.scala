import org.ergoplatform.appkit.config.ErgoToolConfig
import scorex.crypto.hash.Blake2b256
import sigmarand.transaction.{CommitTransaction, RegisterTransaction, RevealTransaction}

object Playground extends App {
  // config
  val conf = ErgoToolConfig.load("config.json")
  // constants
  val NODE_URL = conf.getParameters.get("node_url")
  val NODE_API_KEY = conf.getParameters.get("node_api_key")
  val MNEMONIC = conf.getParameters.get("mnemonic")
  val ADDRESS = conf.getParameters.get("address")

  val randomA = Blake2b256("randomA")
  val hashRandomA = Blake2b256(randomA)
  val randomB = Blake2b256("randomB")

//  val registerTransaction = new RegisterTransaction(ADDRESS,
//    hashRandomA,
//    ADDRESS,
//    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
//    1)
//    .buildUnsignedTx()
//  val commitTransaction = new CommitTransaction(
//    "ce6dac13b941a280e1eb3740008d9e1b5ee2eee9afef66fbd0b0b738918d8da7",
//    randomB,
//    ADDRESS,
//    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
//    1
//  ).submitTx()
  val revealTransaction = new RevealTransaction(
    "3a1a35bffc08ac57eb6e7360889b0f5e97f2672f61721b7f1f36b6fd2b41dd89",
    randomA,
    ADDRESS,
    ADDRESS,
    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
    1
  ).buildUnsignedTx()

  println(revealTransaction.toJson)
}
