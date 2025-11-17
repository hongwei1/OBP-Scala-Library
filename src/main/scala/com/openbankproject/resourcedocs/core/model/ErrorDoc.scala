package com.openbankproject.resourcedocs.core.model

/** Error documentation entry for an endpoint.
  */
final case class ErrorDoc(code: String, httpStatus: Int, message: Option[String] = None)
