package sigmarand.transaction.constant

object Contract {
  val HASH_UTXO_SCRIPT: String =
    s"""
       |{
       |  val clientPK = _CLIENT_PK
       |  val serverPK = _SERVER_PK
       |  val deadline = _DEADLINE
       |  val runtimePropositionBytes = _RUNTIME_PROPOSITION_BYTES
       |  val tokenId = _TOKEN_ID
       |  val tokenAmount = _TOKEN_AMOUNT
       |
       |  val commitBox = OUTPUTS(0)
       |  val refund = HEIGHT > deadline
       |  val propositionCondition = runtimePropositionBytes == commitBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = commitBox.tokens(0)._1 == tokenId && commitBox.tokens(0)._2 == tokenAmount
       |  val feeCondition = OUTPUTS(1).value == 1000000L
       |  clientPK && sigmaProp(refund) || serverPK && sigmaProp(propositionCondition && tokenCondition && sizeCondition && feeCondition)
       |}
       |""".stripMargin
  val COMMIT_UTXO_SCRIPT: String =
    s"""
       |{
       |  val runtimePropositionBytes = _RUNTIME_PROPOSITION_BYTES
       |  val tokenId = _TOKEN_ID
       |  val tokenAmount = _TOKEN_AMOUNT
       |
       |  val commitBox = INPUTS(0)
       |  val revealBox = OUTPUTS(0)
       |  val hashCondition = commitBox.R4[Coll[Byte]].get == blake2b256(revealBox.R4[Coll[Byte]].get)
       |  val commitCondition = commitBox.R5[Coll[Byte]].get == revealBox.R5[Coll[Byte]].get
       |  val propositionCondition = runtimePropositionBytes == revealBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = revealBox.tokens(0)._1 == tokenId && revealBox.tokens(0)._2 == tokenAmount
       |  val feeCondition = OUTPUTS(1).value == 1000000L
       |  sigmaProp(hashCondition && commitCondition && propositionCondition && tokenCondition && sizeCondition && feeCondition)
       |}
       |""".stripMargin
}
