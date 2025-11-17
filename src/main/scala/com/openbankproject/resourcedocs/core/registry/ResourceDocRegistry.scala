package com.openbankproject.resourcedocs.core.registry

import com.openbankproject.resourcedocs.core.model.OBPResourceDocJson

import scala.collection.concurrent.TrieMap

/** Thread-safe registry for OBPResourceDocJson by operationId.
  */
object ResourceDocRegistry {
  private[this] val byOperationId: TrieMap[String, OBPResourceDocJson] = TrieMap.empty

  def register(doc: OBPResourceDocJson): Unit = {
    byOperationId.put(doc.operation_id, doc)
    ()
  }

  def registerAll(docs: Iterable[OBPResourceDocJson]): Unit = docs.foreach(register)

  def get(operationId: String): Option[OBPResourceDocJson] = byOperationId.get(operationId)

  def all: Vector[OBPResourceDocJson] = byOperationId.values.toVector.sortBy(_.operation_id)

  def clear(): Unit = byOperationId.clear()
}
