package com.openbankproject.resourcedocs.core.model

/** HTTP method enumeration independent of any web framework.
  */
sealed trait HttpMethod extends Product with Serializable { def name: String }

object HttpMethod {
  case object GET extends HttpMethod { val name: String = "GET" }
  case object POST extends HttpMethod { val name: String = "POST" }
  case object PUT extends HttpMethod { val name: String = "PUT" }
  case object DELETE extends HttpMethod { val name: String = "DELETE" }
  case object PATCH extends HttpMethod { val name: String = "PATCH" }
  case object HEAD extends HttpMethod { val name: String = "HEAD" }
  case object OPTIONS extends HttpMethod { val name: String = "OPTIONS" }

  val all: Set[HttpMethod] = Set(GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS)

  def fromString(method: String): Option[HttpMethod] =
    all.find(_.name.equalsIgnoreCase(method))
}
