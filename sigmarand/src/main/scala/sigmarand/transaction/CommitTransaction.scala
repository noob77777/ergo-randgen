package sigmarand.transaction

import org.ergoplatform.appkit.{Address, ConstantsBuilder, ErgoToken, ErgoValue, NetworkType, Parameters, RestApiErgoClient, SecretString}
import org.ergoplatform.appkit.impl.ErgoScriptContract
import sigmarand.transaction.constant.Constant.{ADDRESS, MNEMONIC, NODE_API_KEY, NODE_URL}
import sigmarand.transaction.constant.Contract.COMMIT_UTXO_SCRIPT
import sigmarand.transaction.util.Util.hexToBase64

class CommitTransaction(hashBoxId: String,
                        random: Array[Byte],
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

  def submitTx(): String = {
    if (random.length != 32) {
      throw new IllegalArgumentException("Random must be 256 bits")
    }
    client.execute(ctx => {
      val commitTransactionScript = ErgoScriptContract.create(
        ConstantsBuilder.create()
          .item("_RUNTIME_PROPOSITION_BYTES", Address.create(lockingContractAddress).toPropositionBytes)
          .item("_TOKEN_ID", hexToBase64(lockingTokenId))
          .item("_TOKEN_AMOUNT", lockingTokenAmount.longValue())
          .build(),
        COMMIT_UTXO_SCRIPT,
        NetworkType.MAINNET
      )
      val contractAddress = commitTransactionScript.toAddress
      val serverAddress = Address.create(ADDRESS)

      // TODO: add validation for index accesses
      val inputBox = ctx.getBoxesById(hashBoxId)(0)
      val outputBox = ctx.newTxBuilder.outBoxBuilder
        .value(Parameters.MinFee * 2)
        .tokens(new ErgoToken(lockingTokenId, lockingTokenAmount.longValue()))
        .registers(inputBox.getRegisters.get(0), ErgoValue.of(random))
        .contract(contractAddress.toErgoContract)
        .build()
      val unsignedTx = ctx.newTxBuilder()
        .addInputs(inputBox)
        .addOutputs(outputBox)
        .fee(Parameters.MinFee)
        .sendChangeTo(serverAddress)
        .build()

      val prover = ctx.newProverBuilder
        .withMnemonic(
          SecretString.create(MNEMONIC),
          SecretString.empty(),
          false
        )
        .withEip3Secret(0)
        .build()

      val signedTx = prover.sign(unsignedTx)
      val txId = ctx.sendTransaction(signedTx)
      txId.stripPrefix("\"").stripSuffix("\"").trim
    })
  }
}
