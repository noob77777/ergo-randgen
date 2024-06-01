package sigmarand.transaction.constant

object Contract {
  val REGISTER_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  val commitBox = OUTPUTS(0)
       |  val refund = HEIGHT > deadline
       |  val propositionCondition = runtimePropositionBytes == commitBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = commitBox.tokens(0)._1 == tokenId && commitBox.tokens(0)._2 == tokenAmount
       |  clientPK && sigmaProp(refund) || serverPK && sigmaProp(propositionCondition && tokenCondition && sizeCondition)
       |}
       |""".stripMargin
  val COMMIT_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  val commitBox = INPUTS(0)
       |  val revealBox = OUTPUTS(0)
       |  val hashCondition = commitBox.R4[Coll[Byte]].get == blake2b256(revealBox.R4[Coll[Byte]].get)
       |  val commitCondition = commitBox.R5[Coll[Byte]].get == revealBox.R5[Coll[Byte]].get
       |  val propositionCondition = runtimePropositionBytes == revealBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = revealBox.tokens(0)._1 == tokenId && revealBox.tokens(0)._2 == tokenAmount
       |  sigmaProp(hashCondition && commitCondition && propositionCondition && tokenCondition && sizeCondition)
       |}
       |""".stripMargin
}
