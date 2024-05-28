package sigmarand.transaction.constant

object Contract {
  val REGISTER_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  clientPK && sigmaProp(HEIGHT > deadline) || serverPK && sigmaProp(runtimePropositionBytes == OUTPUTS(0).propositionBytes && OUTPUTS.size == 2)
       |}
       |""".stripMargin
  val COMMIT_TRANSACTION_SCRIPT: String =
    s"""
       |{
       |  val commitBox = INPUTS(0)
       |  val revealBox = OUTPUTS(0)
       |  val hashA = commitBox.R4[Coll[Byte]].get
       |  val actualA = revealBox.R4[Coll[Byte]].get
       |  val revealHashA = blake2b256(actualA)
       |  val commitB = commitBox.R5[Coll[Byte]].get
       |  val revealB = revealBox.R5[Coll[Byte]].get
       |  val revealPropositionBytes = revealBox.propositionBytes
       |  sigmaProp(hashA == revealHashA && commitB == revealB && runtimePropositionBytes == revealPropositionBytes && OUTPUTS.size == 2)
       |}
       |""".stripMargin
}
