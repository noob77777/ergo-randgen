package sigmarand.transaction.constant

object Contract {
  val REGISTER_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  val refund = HEIGHT > deadline
       |  val propositionCondition = runtimePropositionBytes == OUTPUTS(0).propositionBytes && OUTPUTS.size == 2
       |  val tokenCondition = OUTPUTS(0).tokens(0)._1 == tokenId && OUTPUTS(0).tokens(0)._2 == tokenAmount
       |  clientPK && sigmaProp(refund) || serverPK && sigmaProp(propositionCondition && tokenCondition)
       |}
       |""".stripMargin
  val COMMIT_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  val commitBox = INPUTS(0)
       |  val revealBox = OUTPUTS(0)
       |  val hashCondition = commitBox.R4[Coll[Byte]].get == blake2b256(revealBox.R4[Coll[Byte]].get)
       |  val commitCondition = commitBox.R5[Coll[Byte]].get == revealBox.R5[Coll[Byte]].get
       |  val propositionCondition = runtimePropositionBytes == revealBox.propositionBytes && OUTPUTS.size == 2
       |  val tokenCondition = OUTPUTS(0).tokens(0)._1 == tokenId && OUTPUTS(0).tokens(0)._2 == tokenAmount
       |  sigmaProp(hashCondition && commitCondition && propositionCondition && tokenCondition)
       |}
       |""".stripMargin
}
