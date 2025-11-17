package com.openbankproject.resourcedocs.exporter

import com.openbankproject.resourcedocs.core.model.{OBPResourceDocJson, RoleInfoJson}

import scala.collection.mutable.ArrayBuffer

import java.time.Instant
import java.time.format.DateTimeFormatter

/** Produces a JSON document that mirrors OBP-API's ResourceDoc response structure. The output contains a resource_docs
  * array plus meta information (response date and count).
  *
  * This exporter deliberately avoids external JSON libraries to keep the module lightweight.
  */
object OBPLikeJsonExporter {

  private val isoFormatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

  def render(docs: Seq[OBPResourceDocJson], responseDate: Instant = Instant.now()): String = {
    val orderedDocs = docs.sortBy(_.operation_id)
    val sb = new StringBuilder
    sb.append("{\n")
    sb.append("  \"resource_docs\": [\n")

    orderedDocs.zipWithIndex.foreach { case (doc, idx) =>
      sb.append("    {\n")
      val fields = ArrayBuffer[String]()
      fields += stringField(6, "operation_id", doc.operation_id)
      fields += implementedByField(6, doc)
      fields += stringField(6, "request_verb", doc.request_verb)
      fields += stringField(6, "request_url", doc.request_url)
      fields += stringField(6, "summary", doc.summary)
      fields += stringField(6, "description", doc.description)
      fields += stringField(6, "description_markdown", doc.description_markdown)
      doc.example_request_body.foreach { body =>
        fields += jsonField(6, "example_request_body", body, 8)
      }
      doc.success_response_body.foreach { body =>
        fields += jsonField(6, "success_response_body", body, 8)
      }
      fields += stringArrayField(6, "error_response_bodies", doc.error_response_bodies)
      fields += stringArrayField(6, "tags", doc.tags)
      doc.typed_request_body.foreach { body =>
        fields += jsonField(6, "typed_request_body", body, 8)
      }
      doc.typed_success_response_body.foreach { body =>
        fields += jsonField(6, "typed_success_response_body", body, 8)
      }
      doc.roles.foreach { roleInfos =>
        fields += rolesField(6, roleInfos)
      }
      fields += booleanField(6, "is_featured", doc.is_featured)
      fields += stringField(6, "special_instructions", doc.special_instructions)
      fields += stringField(6, "specified_url", doc.specified_url)
      fields += stringArrayField(6, "connector_methods", doc.connector_methods)
      doc.created_by_bank_id.foreach { bankId =>
        fields += stringField(6, "created_by_bank_id", bankId)
      }

      val lastIdx = fields.size - 1
      fields.zipWithIndex.foreach { case (entry, fieldIdx) =>
        sb.append(entry)
        val suffix = if (fieldIdx < lastIdx) ",\n" else "\n"
        sb.append(suffix)
      }

      val comma = if (idx < orderedDocs.size - 1) "," else ""
      sb.append("    }" + comma + "\n")
    }
    sb.append("  ],\n")
    sb.append("  \"meta\": {\n")
    sb.append("    \"response_date\": \"" + isoFormatter.format(responseDate) + "\",\n")
    sb.append("    \"count\": " + orderedDocs.size + "\n")
    sb.append("  }\n")
    sb.append("}\n")
    sb.toString()
  }

  private def implementedByField(indent: Int, doc: OBPResourceDocJson): String = {
    val prefix = spaces(indent)
    val builder = new StringBuilder
    builder.append(prefix + "\"implemented_by\": {\n")
    builder.append(prefix + "  \"version\": \"" + escape(doc.implemented_by.version) + "\",\n")
    builder.append(prefix + "  \"function\": \"" + escape(doc.implemented_by.function) + "\"\n")
    builder.append(prefix + "}")
    builder.toString()
  }

  private def stringField(indent: Int, name: String, value: String): String = {
    spaces(indent) + "\"" + name + "\": \"" + escape(value) + "\""
  }

  private def booleanField(indent: Int, name: String, value: Boolean): String = {
    spaces(indent) + "\"" + name + "\": " + value
  }

  private def stringArrayField(indent: Int, name: String, values: Seq[String]): String = {
    val prefix = spaces(indent) + "\"" + name + "\": "
    if (values.isEmpty) {
      prefix + "[]"
    } else {
      val innerIndent = spaces(indent + 2)
      val rendered = values.zipWithIndex
        .map { case (v, idx) =>
          val comma = if (idx < values.size - 1) "," else ""
          innerIndent + "\"" + escape(v) + "\"" + comma
        }
        .mkString("\n")
      prefix + "[\n" + rendered + "\n" + spaces(indent) + "]"
    }
  }

  private def jsonField(indent: Int, name: String, raw: String, nestedIndent: Int): String = {
    val builder = new StringBuilder
    builder.append(spaces(indent) + "\"" + name + "\": ")
    builder.append(renderJsonValue(raw, nestedIndent))
    builder.toString()
  }

  private def rolesField(indent: Int, roleInfos: List[RoleInfoJson]): String = {
    val indentStr = spaces(indent)
    if (roleInfos.isEmpty) indentStr + "\"roles\": []"
    else {
      val entries = roleInfos
        .map { info =>
          val builder = new StringBuilder
          builder.append(spaces(indent + 2) + "{\n")
          builder.append(spaces(indent + 4) + "\"role\": \"" + escape(info.role) + "\",\n")
          builder.append(spaces(indent + 4) + "\"requires_bank_id\": " + info.requires_bank_id + "\n")
          builder.append(spaces(indent + 2) + "}")
          builder.toString()
        }
        .mkString(",\n")
      indentStr + "\"roles\": [\n" + entries + "\n" + indentStr + "]"
    }
  }

  private def renderJsonValue(raw: String, indent: Int): String = {
    val trimmed = raw.trim
    val isJsonBlock = (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
      (trimmed.startsWith("[") && trimmed.endsWith("]"))
    if (isJsonBlock) {
      reindent(trimmed, indent)
    } else {
      "\"" + escape(trimmed) + "\""
    }
  }

  private def reindent(json: String, indent: Int): String = {
    val lines = json.linesIterator.toVector
    if (lines.isEmpty) json
    else {
      val interiorLines = lines.tail.filter { line =>
        val trimmed = line.trim
        trimmed.nonEmpty && !trimmed.startsWith("}") && !trimmed.startsWith("]")
      }
      val baseIndent =
        if (interiorLines.isEmpty) 0
        else interiorLines.map(countLeadingSpaces).min
      val pad = "\n" + spaces(indent)
      lines.head + lines.tail.map { line =>
        val trimmed = line.trim
        val normalized =
          if (trimmed.isEmpty) ""
          else line.drop(math.min(baseIndent, countLeadingSpaces(line)))
        pad + normalized
      }.mkString
    }
  }

  private def countLeadingSpaces(line: String): Int = line.prefixLength(_ == ' ')

  private def toJsonArray(values: Seq[String]): String = {
    if (values.isEmpty) "[]"
    else values.map(v => "\"" + escape(v) + "\"").mkString("[ ", ", ", " ]")
  }

  private def escape(value: String): String = {
    value
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\n", "\\n")
      .replace("\r", "\\r")
      .replace("\t", "\\t")
  }

  private def spaces(count: Int): String = " " * count
}
