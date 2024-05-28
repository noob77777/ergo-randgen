package sigmarand.transaction.model

import play.api.libs.json.{Json, OFormat}

final case class MInput(extension: Map[String, String],
                        boxId: String,
                        value: String,
                        ergoTree: String,
                        assets: Array[MToken],
                        additionalRegisters: Map[String, String],
                        creationHeight: Int,
                        transactionId: String,
                        index: Short
                       )

object MInput {
  implicit val json: OFormat[MInput] = Json.format[MInput]
}
