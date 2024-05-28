package sigmarand.transaction.model

import play.api.libs.json.{Json, OFormat}

final case class MOutput(value: String,
                         ergoTree: String,
                         assets: Array[MToken],
                         additionalRegisters: Map[String, String],
                         creationHeight: Int
                        )

object MOutput {
  implicit val json: OFormat[MOutput] = Json.format[MOutput]
}
