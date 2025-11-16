package com.openbankproject.resourcedocs.core.model

/**
 * Minimal request context required for access checks.
 */
final case class RequestContext(
  userRoles: Set[String],
  attributes: Map[String, String] = Map.empty
)


