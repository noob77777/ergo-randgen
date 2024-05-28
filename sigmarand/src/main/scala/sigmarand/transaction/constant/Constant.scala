package sigmarand.transaction.constant

import org.ergoplatform.appkit.config.ErgoToolConfig

object Constant {
  // static config
  private val conf = ErgoToolConfig.load("config.json")
  // constants
  val NODE_URL: String = conf.getParameters.get("node_url")
  val NODE_API_KEY: String = conf.getParameters.get("node_api_key")
  val MNEMONIC: String = conf.getParameters.get("mnemonic")
  val ADDRESS: String = conf.getParameters.get("address")
}
