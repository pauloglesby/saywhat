package ly.analogical.saywhat.model

final case class ApiToken(value: String) extends AnyVal

final case class Rule(value: String, tag: String, id: String)
final case class RuleRaw(value: String, tag: String)
final case class AddRule(add: List[RuleRaw])
final case class AddRuleResponse(data: List[Rule])

final case class Data(id: String, text: String)
final case class Place(id: String, country_code: Option[String], full_name: Option[String], place_type: Option[String])
final case class Includes(places: List[Place])
final case class RuleSummary(id: String, tag: String)
final case class TweetItem(data: Data, includes: Option[Includes], matching_rules: List[RuleSummary])
