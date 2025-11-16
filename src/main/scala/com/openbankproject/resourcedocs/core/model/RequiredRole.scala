package com.openbankproject.resourcedocs.core.model

/**
 * Expresses required roles for accessing an endpoint.
 */
sealed trait RequiredRole extends Product with Serializable {
  def isAuthorized(userRoles: Set[String]): Boolean
}

object RequiredRole {
  case object Public extends RequiredRole {
    override def isAuthorized(userRoles: Set[String]): Boolean = true
    override def toString: String = "Public"
  }

  final case class AnyOf(roles: Set[String]) extends RequiredRole {
    override def isAuthorized(userRoles: Set[String]): Boolean =
      roles.isEmpty || roles.exists(userRoles.contains)
  }

  final case class AllOf(roles: Set[String]) extends RequiredRole {
    override def isAuthorized(userRoles: Set[String]): Boolean =
      roles.forall(userRoles.contains)
  }

  /**
   * Disjunction of conjunctions; represents (A & B) | (C) ...
   */
  final case class OrOfAnds(groups: Seq[Set[String]]) extends RequiredRole {
    override def isAuthorized(userRoles: Set[String]): Boolean =
      groups.exists(group => group.forall(userRoles.contains))
  }

  def anyOf(first: String, rest: String*): RequiredRole = AnyOf((first +: rest).toSet)
  def allOf(first: String, rest: String*): RequiredRole = AllOf((first +: rest).toSet)
  def oneOfAllOf(firstGroup: Set[String], otherGroups: Set[String]*): RequiredRole = OrOfAnds(firstGroup +: otherGroups)
  val public: RequiredRole = Public
}


