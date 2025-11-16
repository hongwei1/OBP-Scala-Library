package com.openbankproject.resourcedocs.export

import com.openbankproject.resourcedocs.core.model.OBPResourceDocJson

/**
 * Simple Markdown exporter.
 */
object MarkdownExporter {
  def export(docs: Seq[OBPResourceDocJson]): String = {
    val sb = new StringBuilder
    val ordered = docs.sortBy(_.operation_id)
    ordered.foreach { d =>
      sb.append(s"### ${d.operation_id}\n\n")
      sb.append(s"- Method: ${d.request_verb}\n")
      sb.append(s"- Path: ${d.request_url}\n")
      if (d.summary.nonEmpty) sb.append(s"- Summary: ${d.summary}\n")
      if (d.description.nonEmpty) sb.append(s"\n${d.description}\n")
      if (d.tags.nonEmpty) sb.append(s"\nTags: ${d.tags.sorted.mkString(", ")}\n")
      sb.append("\n\n")
    }
    sb.toString()
  }
}


