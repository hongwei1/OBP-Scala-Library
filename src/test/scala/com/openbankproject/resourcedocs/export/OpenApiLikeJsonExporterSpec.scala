package com.openbankproject.resourcedocs.exporter

import com.openbankproject.resourcedocs.core.model.{ImplementedByJson, OBPResourceDocJson, RoleInfoJson}
import org.scalatest.funsuite.AnyFunSuite

class OpenApiLikeJsonExporterSpec extends AnyFunSuite {

  private val getBanksDescriptionHtml: String =
    "<p>Get banks on this API instance<br />\nReturns a list of banks supported on this server:</p>\n<ul>\n<li>ID used as parameter in URLs</li>\n<li>Short and full name of bank</li>\n<li>Logo URL</li>\n<li>Website</li>\n</ul>\n<p>User Authentication is Optional. The User need not be logged in.</p>\n<p><strong>JSON response body fields:</strong></p>\n<p><a href=\"/glossary#address\"><strong>address</strong></a>:</p>\n<p><a href=\"/glossary#bank_routings\"><strong>bank_routings</strong></a>: bank routing in form of (scheme, address)</p>\n<p><a href=\"/glossary#banks\"><strong>banks</strong></a>:</p>\n<p><a href=\"/glossary#full_name\"><strong>full_name</strong></a>: full name string</p>\n<p><a href=\"/glossary#id\"><strong>id</strong></a>: d8839721-ad8f-45dd-9f78-2080414b93f9</p>\n<p><a href=\"/glossary#logo\"><strong>logo</strong></a>: logo url</p>\n<p><a href=\"/glossary#name\"><strong>name</strong></a>: ACCOUNT_MANAGEMENT_FEE</p>\n<p><a href=\"/glossary#scheme\"><strong>scheme</strong></a>: OBP</p>\n<p><a href=\"/glossary#short_name\"><strong>short_name</strong></a>:</p>\n<p><a href=\"/glossary#\"><strong>value</strong></a>: 5987953</p>\n<p><a href=\"/glossary#website\"><strong>website</strong></a>: <a href=\"http://www.openbankproject.com\">www.openbankproject.com</a></p>\n<p><a href=\"/glossary#attributes\">attributes</a>: attribute value in form of (name, value)</p>\n"

  private val getBanksDescriptionMarkdown: String =
    "Get banks on this API instance\nReturns a list of banks supported on this server:\n\n* ID used as parameter in URLs\n* Short and full name of bank\n* Logo URL\n* Website\n\nUser Authentication is Optional. The User need not be logged in.\n\n\n**JSON response body fields:**\n\n\n\n[**address**](/glossary#address): \n\n\n\n[**bank_routings**](/glossary#bank_routings): bank routing in form of (scheme, address)\n\n\n\n[**banks**](/glossary#banks): \n\n\n\n[**full_name**](/glossary#full_name): full name string\n\n\n\n[**id**](/glossary#id): d8839721-ad8f-45dd-9f78-2080414b93f9\n\n\n\n[**logo**](/glossary#logo): logo url\n\n\n\n[**name**](/glossary#name): ACCOUNT_MANAGEMENT_FEE\n\n\n\n[**scheme**](/glossary#scheme): OBP\n\n\n\n[**short_name**](/glossary#short_name): \n\n\n\n[**value**](/glossary#): 5987953\n\n\n\n[**website**](/glossary#website): www.openbankproject.com\n\n\n\n[attributes](/glossary#attributes): attribute value in form of (name, value)\n\n\n"

  private val getBanksSuccessResponse: String =
    """{"banks":[{"id":"gh.29.uk","short_name":"short_name ","full_name":"full_name","logo":"logo","website":"www.openbankproject.com","bank_routings":[{"scheme":"OBP","address":"gh.29.uk"}],"attributes":[{"name":"ACCOUNT_MANAGEMENT_FEE","value":"5987953"}]}]}"""

  private val getBanksTypedSuccessResponse: String =
    """{"type":"object","properties":{"banks":{"type":"array","items":{"type":"object","properties":{"bank_routings":{"type":"array","items":{"type":"object","properties":{"address":{"type":"string"},"scheme":{"type":"string"}}}},"website":{"type":"string"},"logo":{"type":"string"},"attributes":{"type":"array","items":{"type":"object","properties":{"name":{"type":"string"},"value":{"type":"string"}}}},"short_name":{"type":"string"},"id":{"type":"string"},"full_name":{"type":"string"}}}}}}"""

  private def getBanksDoc(operationId: String, verb: String, path: String, summary: String): OBPResourceDocJson = {
    OBPResourceDocJson(
      operation_id = operationId,
      implemented_by = ImplementedByJson("OBPv4.0.0", "getBanks"),
      request_verb = verb,
      request_url = path,
      summary = summary,
      description = getBanksDescriptionHtml,
      description_markdown = getBanksDescriptionMarkdown,
      success_response_body = Some(getBanksSuccessResponse),
      error_response_bodies = List("OBP-50000: Unknown Error."),
      tags = List("Bank", "Account Information Service (AIS)", "PSD2"),
      typed_success_response_body = Some(getBanksTypedSuccessResponse),
      specified_url = "/obp/v5.1.0/banks"
    )
  }

  test("export should group getBanks methods by path and verb") {
    val getBanksV3 = getBanksDoc(
      operationId = "OBPv3.0.0-getBanks",
      verb = "GET",
      path = "/obp/v3.0.0/banks",
      summary = "Get Banks V3"
    )
    val createBanks = getBanksDoc(
      operationId = "OBPv4.0.0-createBank",
      verb = "POST",
      path = "/obp/v4.0.0/banks",
      summary = "Create Bank"
    )
    val getBankAttributes = getBanksDoc(
      operationId = "OBPv4.0.0-getBankAttributes",
      verb = "GET",
      path = "/obp/v4.0.0/banks/{bankId}/attributes",
      summary = "Get Bank Attributes"
    )

    val json = OpenApiLikeJsonExporter.render(Seq(getBankAttributes, createBanks, getBanksV3))

    assert(json.contains("\"openapi\": \"3.0.0\""))
    assert(json.contains("\"/obp/v3.0.0/banks\""))
    assert(json.contains("\"/obp/v4.0.0/banks\""))
    assert(json.contains("\"post\""))
    assert(json.contains("\"get\""))
    assert(json.contains("\"operationId\": \"OBPv3.0.0-getBanks\""))
    assert(json.contains("\"operationId\": \"OBPv4.0.0-createBank\""))
    assert(json.contains("\"/obp/v4.0.0/banks/{bankId}/attributes\""))
    assert(json.contains("\"operationId\": \"OBPv4.0.0-getBankAttributes\""))
  }

  test("export should encode getBanks role expressions and include response codes") {
    val securedDoc = getBanksDoc(
      operationId = "OBPv5.1.0-getBanksSecure",
      verb = "GET",
      path = "/obp/v5.1.0/banks",
      summary = "Get Banks Secure"
    ).copy(
      roles = Some(List(RoleInfoJson("CanGetBanks"), RoleInfoJson("CanReadBanks"))),
      error_response_bodies = List("OBP-40300: Insufficient Privileges.", "OBP-50000: Unknown Error.")
    )

    val json = OpenApiLikeJsonExporter.render(Seq(securedDoc))

    assert(json.contains("\"rolesAnyOf\""))
    assert(json.contains("CanGetBanks"))
    assert(json.contains("CanReadBanks"))
    assert(json.contains("\"40300\""))
    assert(json.contains("\"50000\""))
  }
}
