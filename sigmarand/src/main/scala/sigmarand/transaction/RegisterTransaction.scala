package sigmarand.transaction

import org.ergoplatform.appkit.impl.ErgoScriptContract
import org.ergoplatform.appkit.{Address, BoxOperations, ConstantsBuilder, ErgoId, ErgoToken, ErgoValue, NetworkType, Parameters, RestApiErgoClient}
import sigmarand.transaction.constant.Constant.{ADDRESS, NODE_API_KEY, NODE_URL}
import sigmarand.transaction.constant.Contract.{COMMIT_UTXO_SCRIPT, HASH_UTXO_SCRIPT}
import sigmarand.transaction.model.MUnsignedTransaction
import sigmarand.transaction.util.Util.hexToBase64

class RegisterTransaction(address: String,
                          randomHash: Array[Byte],
                          lockingContractAddress: String,
                          lockingTokenId: String,
                          lockingTokenAmount: Integer
                         ) {
  // setup client
  private val client = RestApiErgoClient.create(
    NODE_URL,
    NetworkType.MAINNET,
    NODE_API_KEY,
    RestApiErgoClient.defaultMainnetExplorerUrl
  )
  private val REFUND_BLOCK_WINDOW = 60

  def buildUnsignedTx(): MUnsignedTransaction = {
    if (randomHash.length != 32) {
      throw new IllegalArgumentException("Random hash must be Blake2b256 encoded")
    }
    client.execute(ctx => {
      val deadline = ctx.getHeight + REFUND_BLOCK_WINDOW
      val clientAddress = Address.create(address)
      val serverAddress = Address.create(ADDRESS)

      val commitTransactionScript = ErgoScriptContract.create(
        ConstantsBuilder.create()
          .item("_RUNTIME_PROPOSITION_BYTES", Address.create(lockingContractAddress).toPropositionBytes)
          .item("_TOKEN_ID", hexToBase64(lockingTokenId))
          .item("_TOKEN_AMOUNT", lockingTokenAmount.longValue())
          .build(),
        COMMIT_UTXO_SCRIPT,
        NetworkType.MAINNET
      )
      val registerTransactionContract = ErgoScriptContract.create(
        ConstantsBuilder.create()
          .item("_CLIENT_PK", clientAddress.getPublicKey)
          .item("_SERVER_PK", serverAddress.getPublicKey)
          .item("_DEADLINE", deadline)
          .item("_RUNTIME_PROPOSITION_BYTES", commitTransactionScript.toAddress.toPropositionBytes)
          .item("_TOKEN_ID", hexToBase64(lockingTokenId))
          .item("_TOKEN_AMOUNT", lockingTokenAmount.longValue())
          .build(),
        HASH_UTXO_SCRIPT,
        NetworkType.MAINNET
      )
      val contractAddress = registerTransactionContract.toAddress

      val outBox = ctx.newTxBuilder().outBoxBuilder()
        .value(Parameters.MinFee * 3)
        .tokens(new ErgoToken(lockingTokenId, lockingTokenAmount.longValue()))
        .registers(ErgoValue.of(randomHash))
        .contract(contractAddress.toErgoContract)
        .build()
      val unsignedTx = BoxOperations.createForSender(clientAddress, ctx)
        .withAmountToSpend(outBox.getValue)
        .withTokensToSpend(outBox.getTokens)
        .buildTxWithDefaultInputs(tb =>
          tb.addOutputs(outBox)
        )

      MUnsignedTransaction(unsignedTx)
    })
  }
}
