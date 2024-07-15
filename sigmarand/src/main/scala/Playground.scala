import org.ergoplatform.appkit.config.ErgoToolConfig
import scorex.crypto.hash.Blake2b256
import sigmarand.transaction.util.Util
import sigmarand.transaction.{CommitTransaction, RegisterTransaction, RevealTransaction}

import java.util.UUID

object Playground extends App {
  // config
  val conf = ErgoToolConfig.load("config.json")
  // constants
  val NODE_URL = conf.getParameters.get("node_url")
  val NODE_API_KEY = conf.getParameters.get("node_api_key")
  val MNEMONIC = conf.getParameters.get("mnemonic")
  val ADDRESS = conf.getParameters.get("address")

  val randomA = "36845d2ac36444a35f314f001a93e708".getBytes
  val hashRandomA = Util.hexToBase64("54e15ad6e8b896861bd7f4fddce11c9a94d4a6286004d4d3ab6b3664b361ef35")
  val randomB = Blake2b256(UUID.randomUUID.toString)

  val registerTransaction = new RegisterTransaction(ADDRESS,
    hashRandomA,
    ADDRESS,
    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
    1)
    .buildUnsignedTx()
  //  val commitTransaction = new CommitTransaction(
  //    "9e3606231364ec3dd6f05dc620b7e4fc2e59a7878f2d19c3cb0280347e0b6655",
  //    randomB,
  //    ADDRESS,
  //    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
  //    1
  //  ).submitTx()
  //  val revealTransaction = new RevealTransaction(
  //    "fdca20af3146c5aabc34e09f38f12991194103588aaa057be6e26e5bb272f883",
  //    randomA,
  //    ADDRESS,
  //    ADDRESS,
  //    "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
  //    1
  //  ).buildUnsignedTx()

  println(registerTransaction.toJson)
}
