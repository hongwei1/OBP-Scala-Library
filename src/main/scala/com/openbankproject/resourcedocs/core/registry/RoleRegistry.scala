package com.openbankproject.resourcedocs.core.registry

import scala.collection.concurrent.TrieMap

/**
 * Registry for known role names. This is optional but helps to audit roles used across docs.
 */
object RoleRegistry {
  private[this] val roles: TrieMap[String, Unit] = TrieMap.empty

  def register(role: String): Unit = { roles.put(role, ()) ; () }
  def registerAll(rs: Iterable[String]): Unit = rs.foreach(register)
  def all: Vector[String] = roles.keys.toVector.sorted
  def clear(): Unit = roles.clear()
}


