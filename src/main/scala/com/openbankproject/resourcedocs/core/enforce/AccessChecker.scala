package com.openbankproject.resourcedocs.core.enforce

import com.openbankproject.resourcedocs.core.model.{RequiredRole, RequestContext, RoleInfoJson}
import com.openbankproject.resourcedocs.core.registry.ResourceDocRegistry

/** Performs access checks against registered ResourceDocs by operationId.
  */
object AccessChecker {

  /** Convert role descriptors to RequiredRole for authorization checks. The semantics follow OBP-API: providing
    * multiple roles means logical OR.
    */
  private def rolesToRequiredRole(roles: Option[List[RoleInfoJson]]): RequiredRole = {
    roles match {
      case None      => RequiredRole.public
      case Some(Nil) => RequiredRole.public
      case Some(roleInfos) =>
        val identifiers = roleInfos.map(_.role).toSet
        if (identifiers.isEmpty) RequiredRole.public else RequiredRole.AnyOf(identifiers)
    }
  }

  def require(operationId: String, ctx: RequestContext): Either[String, Unit] = {
    ResourceDocRegistry.get(operationId) match {
      case None => Left(s"OperationNotRegistered: $operationId")
      case Some(docJson) =>
        val requiredRole = rolesToRequiredRole(docJson.roles)
        if (isAuthorized(requiredRole, ctx.userRoles)) Right(())
        else Left(s"InsufficientRoles: $operationId")
    }
  }

  private def isAuthorized(required: RequiredRole, userRoles: Set[String]): Boolean =
    required.isAuthorized(userRoles)
}
