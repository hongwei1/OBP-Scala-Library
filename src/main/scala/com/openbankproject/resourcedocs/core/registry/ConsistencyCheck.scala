package com.openbankproject.resourcedocs.core.registry

import com.openbankproject.resourcedocs.core.model.OBPResourceDocJson

object ConsistencyCheck {
  def findDuplicateOperationIds(docs: Seq[OBPResourceDocJson]): Map[String, Int] =
    docs
      .groupBy(_.operation_id)
      .iterator
      .map { case (operationId, entries) => operationId -> entries.size }
      .filter(_._2 > 1)
      .toMap
}
