package sigmarand.transaction

import org.ergoplatform.appkit.impl.ErgoScriptContract
import org.ergoplatform.appkit.{Address, BoxOperations, ConstantsBuilder, ErgoToken, ErgoValue, NetworkType, Parameters, RestApiErgoClient}
import sigmarand.transaction.constant.Constant.{ADDRESS, NODE_API_KEY, NODE_URL}
import sigmarand.transaction.constant.Contract.{COMMIT_TRANSACTION_SCRIPT, REGISTER_TRANSACTION_SCRIPT}
import sigmarand.transaction.model.MUnsignedTransaction

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
    client.execute(ctx => {
      val deadline = ctx.getHeight + REFUND_BLOCK_WINDOW
      val clientAddress = Address.create(address)
      val serverAddress = Address.create(ADDRESS)

      val commitTransactionScript = ErgoScriptContract.create(
        ConstantsBuilder.create()
          .item("runtimePropositionBytes", Address.create(lockingContractAddress).toPropositionBytes)
          .build(),
        COMMIT_TRANSACTION_SCRIPT,
        NetworkType.MAINNET
      )
      val registerTransactionContract = ErgoScriptContract.create(
        ConstantsBuilder.create()
          .item("clientPK", clientAddress.getPublicKey)
          .item("serverPK", serverAddress.getPublicKey)
          .item("deadline", deadline)
          .item("runtimePropositionBytes", commitTransactionScript.toAddress.toPropositionBytes)
          .build(),
        REGISTER_TRANSACTION_SCRIPT,
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
