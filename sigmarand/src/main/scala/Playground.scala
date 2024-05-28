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

  val registerTransaction = new RegisterTransaction(ADDRESS,
    hashRandomA,
    ADDRESS,
    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
    1)
    .buildUnsignedTx()
//  val commitTransaction = new CommitTransaction(
//    "2854a56dbaca084b7d3ad625b885712ea60dbe7e9178ed08d421554410aac44b",
//    randomB,
//    ADDRESS,
//    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
//    1
//  ).submitTx()
//  val revealTransaction = new RevealTransaction(
//    "f705a4f6cef1ff31e3b2a932eb9e983f6be088776b00309793f32aed47e7326b",
//    randomA,
//    ADDRESS,
//    ADDRESS,
//    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
//    1
//  ).buildUnsignedTx()

  println(registerTransaction.toJson)
}
