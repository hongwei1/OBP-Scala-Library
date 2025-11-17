package com.openbankproject.resourcedocs.exporter

import com.openbankproject.resourcedocs.core.model.{OBPResourceDocJson, RoleInfoJson}

/** Lightweight JSON exporter without third-party JSON dependencies. It produces an OpenAPI-like document containing
  * essential fields only.
  */
object OpenApiLikeJsonExporter {

  def render(docs: Seq[OBPResourceDocJson]): String = {
    val byPath = docs.groupBy(_.request_url)
    val sb = new StringBuilder
    sb.append("{\n")
    sb.append("  \"openapi\": \"3.0.0\",\n")
    sb.append("  \"info\": { \"title\": \"OBP ResourceDocs\", \"version\": \"0.1.0\" },\n")
    sb.append("  \"paths\": {\n")

    val pathEntries = byPath.toVector.sortBy(_._1).zipWithIndex
    pathEntries.foreach { case ((path, pathDocs), i) =>
      sb.append("    \"" + escape(path) + "\": {\n")
      val methodEntries = pathDocs.sortBy(_.request_verb).zipWithIndex
      methodEntries.foreach { case (doc, j) =>
        val methodName = doc.request_verb.toLowerCase
        sb.append("      \"" + methodName + "\": {\n")
        sb.append("        \"operationId\": \"" + escape(doc.operation_id) + "\",\n")
        sb.append("        \"summary\": \"" + escape(doc.summary) + "\",\n")
        sb.append("        \"description\": \"" + escape(doc.description) + "\",\n")
        sb.append("        \"tags\": " + toJsonArray(doc.tags.sorted.toVector) + ",\n")
        sb.append("        \"security\": " + rolesToSecurityJson(doc.roles) + ",\n")
        sb.append("        \"responses\": {\n")
        val errs =
          if (doc.error_response_bodies.isEmpty) Vector("200")
          else {
            // Extract HTTP status codes from error messages if possible
            val statusPattern = """OBP-(\d+):""".r
            doc.error_response_bodies
              .flatMap { errMsg =>
                statusPattern.findFirstMatchIn(errMsg).map(_.group(1))
              }
              .distinct
              .sorted
              .toVector
          }
        val errEntries = errs.zipWithIndex
        errEntries.foreach { case (status, k) =>
          val comma = if (k < errEntries.size - 1) "," else ""
          sb.append("          \"" + status + "\": { \"description\": \"\" }" + comma + "\n")
        }
        sb.append("        }\n")
        val comma = if (j < methodEntries.size - 1) "," else ""
        sb.append("      }" + comma + "\n")
      }
      val comma = if (i < pathEntries.size - 1) "," else ""
      sb.append("    }" + comma + "\n")
    }

    sb.append("  }\n")
    sb.append("}\n")
    sb.toString()
  }

  private def rolesToSecurityJson(roleInfos: Option[List[RoleInfoJson]]): String =
    roleInfos match {
      case None | Some(Nil) => "[]"
      case Some(infos) =>
        val identifiers = infos.map(_.role).toVector.sorted
        "[{\"rolesAnyOf\": " + toJsonArray(identifiers) + "}]"
    }

  private def toJsonArray(values: Vector[String]): String = {
    val body = values.map(v => "\"" + escape(v) + "\"").mkString(", ")
    "[ " + body + " ]"
  }

  private def escape(s: String): String = {
    s.replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\n", "\\n")
      .replace("\r", "\\r")
      .replace("\t", "\\t")
  }
}
