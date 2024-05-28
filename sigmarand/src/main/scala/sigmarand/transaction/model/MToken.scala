package sigmarand.transaction.model

import play.api.libs.json.{Json, OFormat}

final case class MToken(tokenId: String, amount: String)

object MToken {
  implicit val json: OFormat[MToken] = Json.format[MToken]
}
