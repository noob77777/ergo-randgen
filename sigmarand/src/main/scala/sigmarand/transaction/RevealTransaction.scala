package sigmarand.transaction

import org.ergoplatform.appkit.{Address, ErgoToken, ErgoValue, NetworkType, Parameters, RestApiErgoClient}
import sigmarand.transaction.constant.Constant.{NODE_API_KEY, NODE_URL}
import sigmarand.transaction.model.MUnsignedTransaction
import special.collection.Coll

import scala.collection.JavaConverters._
import java.util.ArrayList

class RevealTransaction(commitBoxId: String,
                        random: Array[Byte],
                        address: String,
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

  def buildUnsignedTx(): MUnsignedTransaction = {
    client.execute(ctx => {
      val clientAddress = Address.create(address)
      val contractAddress = Address.create(lockingContractAddress)

      // TODO: add validation for index accesses
      val inputBox = ctx.getBoxesById(commitBoxId)(0)
      val xr = xor(random, inputBox.getRegisters.get(1).getValue.asInstanceOf[Coll[Byte]].toArray)
      val outputBox = ctx.newTxBuilder.outBoxBuilder
        .value(Parameters.MinFee)
        .tokens(new ErgoToken(lockingTokenId, lockingTokenAmount.longValue()))
        .registers(ErgoValue.of(random), inputBox.getRegisters.get(1), ErgoValue.of(xr))
        .contract(contractAddress.toErgoContract)
        .build()
      val unsignedTx = ctx.newTxBuilder()
        .addInputs(inputBox)
        .addOutputs(outputBox)
        .fee(Parameters.MinFee)
        .sendChangeTo(clientAddress)
        .build()

      MUnsignedTransaction(unsignedTx)
    })
  }

  private def xor(r1: Array[Byte], r2: Array[Byte]): Array[Byte] = {
    val res = new ArrayList[Byte]()
    val l = r2 zipWithIndex;
    for (p <- l)
      res.add(((if (r1.length > p._2) r1(p._2) else 0) ^ p._1).toByte)
    res.asScala.toArray
  }
}
