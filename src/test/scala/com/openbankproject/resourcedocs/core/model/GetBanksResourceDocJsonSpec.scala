package com.openbankproject.resourcedocs.core.model

import com.openbankproject.resourcedocs.core.model.{ImplementedByJson, OBPResourceDocJson}
import org.scalatest.funsuite.AnyFunSuite

class GetBanksResourceDocJsonSpec extends AnyFunSuite {

  test("GetBanks ResourceDocJson should match expected structure") {
    val getBanksDocJson: OBPResourceDocJson = OBPResourceDocJson(
      operation_id = "OBPv3.0.0-getBanks",
      implemented_by = ImplementedByJson(version = "OBPv3.0.0", function = "getBanks"),
      request_verb = "GET",
      request_url = "/obp/v3.0.0/banks",
      summary = "Get Banks",
      description =
        "<p>Get banks on this API instance<br />\nReturns a list of banks supported on this server:</p>\n<ul>\n<li>ID used as parameter in URLs</li>\n<li>Short and full name of bank</li>\n<li>Logo URL</li>\n<li>Website</li>\n</ul>\n<p>User Authentication is Optional. The User need not be logged in.</p>\n<p><strong>JSON response body fields:</strong></p>\n<p><a href=\"/glossary#address\"><strong>address</strong></a>:</p>\n<p><a href=\"/glossary#bank_routing\"><strong>bank_routing</strong></a>:</p>\n<p><a href=\"/glossary#banks\"><strong>banks</strong></a>:</p>\n<p><a href=\"/glossary#full_name\"><strong>full_name</strong></a>: full name string</p>\n<p><a href=\"/glossary#id\"><strong>id</strong></a>: d8839721-ad8f-45dd-9f78-2080414b93f9</p>\n<p><a href=\"/glossary#logo\"><strong>logo</strong></a>: logo url</p>\n<p><a href=\"/glossary#scheme\"><strong>scheme</strong></a>: OBP</p>\n<p><a href=\"/glossary#short_name\"><strong>short_name</strong></a>:</p>\n<p><a href=\"/glossary#website\"><strong>website</strong></a>: <a href=\"http://www.openbankproject.com\">www.openbankproject.com</a></p>\n",
      description_markdown =
        "Get banks on this API instance\nReturns a list of banks supported on this server:\n\n* ID used as parameter in URLs\n* Short and full name of bank\n* Logo URL\n* Website\n\nUser Authentication is Optional. The User need not be logged in.\n\n\n**JSON response body fields:**\n\n\n\n[**address**](/glossary#address): \n\n\n\n[**bank_routing**](/glossary#bank_routing): \n\n\n\n[**banks**](/glossary#banks): \n\n\n\n[**full_name**](/glossary#full_name): full name string\n\n\n\n[**id**](/glossary#id): d8839721-ad8f-45dd-9f78-2080414b93f9\n\n\n\n[**logo**](/glossary#logo): logo url\n\n\n\n[**scheme**](/glossary#scheme): OBP\n\n\n\n[**short_name**](/glossary#short_name): \n\n\n\n[**website**](/glossary#website): www.openbankproject.com\n\n\n",
      success_response_body = Some(
        """{"banks":[{"id":"gh.29.uk","short_name":"short_name ","full_name":"full_name","logo":"logo","website":"www.openbankproject.com","bank_routing":{"scheme":"OBP","address":"gh.29.uk"}}]}"""
      ),
      error_response_bodies = List("OBP-50000: Unknown Error."),
      tags = List("Bank", "Account Information Service (AIS)", "PSD2"),
      typed_success_response_body = Some(
        """{"type":"object","properties":{"banks":{"type":"array","items":{"type":"object","properties":{"website":{"type":"string"},"logo":{"type":"string"},"bank_routing":{"type":"object","properties":{"address":{"type":"string"},"scheme":{"type":"string"}}},"short_name":{"type":"string"},"id":{"type":"string"},"full_name":{"type":"string"}}}}}}"""
      ),
      is_featured = false,
      special_instructions = "",
      specified_url = "/obp/v3.1.0/banks",
      connector_methods = List("obp.getBanks", "obp.getBankAccountsForUser")
    )

    // Verify structure
    assert(getBanksDocJson.operation_id == "OBPv3.0.0-getBanks")
    assert(getBanksDocJson.request_verb == "GET")
    assert(getBanksDocJson.request_url == "/obp/v3.0.0/banks")
    assert(getBanksDocJson.summary == "Get Banks")
    assert(getBanksDocJson.implemented_by.version == "OBPv3.0.0")
    assert(getBanksDocJson.implemented_by.function == "getBanks")

    val docTags = getBanksDocJson.tags
    assert(docTags.contains("Bank"))
    assert(docTags.contains("Account Information Service (AIS)"))
    assert(docTags.contains("PSD2"))

    assert(getBanksDocJson.error_response_bodies.contains("OBP-50000: Unknown Error."))
    assert(getBanksDocJson.connector_methods.contains("obp.getBanks"))
    assert(getBanksDocJson.connector_methods.contains("obp.getBankAccountsForUser"))
    assert(getBanksDocJson.specified_url == "/obp/v3.1.0/banks")
  }

  test("GetBanks ResourceDocJson JSON output should match expected JSON string") {
    val getBanksDocJson = OBPResourceDocJson(
      operation_id = "OBPv3.0.0-getBanks",
      implemented_by = ImplementedByJson(version = "OBPv3.0.0", function = "getBanks"),
      request_verb = "GET",
      request_url = "/obp/v3.0.0/banks",
      summary = "Get Banks",
      description =
        "<p>Get banks on this API instance<br />\nReturns a list of banks supported on this server:</p>\n<ul>\n<li>ID used as parameter in URLs</li>\n<li>Short and full name of bank</li>\n<li>Logo URL</li>\n<li>Website</li>\n</ul>\n<p>User Authentication is Optional. The User need not be logged in.</p>\n<p><strong>JSON response body fields:</strong></p>\n<p><a href=\"/glossary#address\"><strong>address</strong></a>:</p>\n<p><a href=\"/glossary#bank_routing\"><strong>bank_routing</strong></a>:</p>\n<p><a href=\"/glossary#banks\"><strong>banks</strong></a>:</p>\n<p><a href=\"/glossary#full_name\"><strong>full_name</strong></a>: full name string</p>\n<p><a href=\"/glossary#id\"><strong>id</strong></a>: d8839721-ad8f-45dd-9f78-2080414b93f9</p>\n<p><a href=\"/glossary#logo\"><strong>logo</strong></a>: logo url</p>\n<p><a href=\"/glossary#scheme\"><strong>scheme</strong></a>: OBP</p>\n<p><a href=\"/glossary#short_name\"><strong>short_name</strong></a>:</p>\n<p><a href=\"/glossary#website\"><strong>website</strong></a>: <a href=\"http://www.openbankproject.com\">www.openbankproject.com</a></p>\n",
      description_markdown =
        "Get banks on this API instance\nReturns a list of banks supported on this server:\n\n* ID used as parameter in URLs\n* Short and full name of bank\n* Logo URL\n* Website\n\nUser Authentication is Optional. The User need not be logged in.\n\n\n**JSON response body fields:**\n\n\n\n[**address**](/glossary#address): \n\n\n\n[**bank_routing**](/glossary#bank_routing): \n\n\n\n[**banks**](/glossary#banks): \n\n\n\n[**full_name**](/glossary#full_name): full name string\n\n\n\n[**id**](/glossary#id): d8839721-ad8f-45dd-9f78-2080414b93f9\n\n\n\n[**logo**](/glossary#logo): logo url\n\n\n\n[**scheme**](/glossary#scheme): OBP\n\n\n\n[**short_name**](/glossary#short_name): \n\n\n\n[**website**](/glossary#website): www.openbankproject.com\n\n\n",
      success_response_body = Some(
        """{"banks":[{"id":"gh.29.uk","short_name":"short_name ","full_name":"full_name","logo":"logo","website":"www.openbankproject.com","bank_routing":{"scheme":"OBP","address":"gh.29.uk"}}]}"""
      ),
      error_response_bodies = List("OBP-50000: Unknown Error."),
      tags = List("Bank", "Account Information Service (AIS)", "PSD2"),
      typed_success_response_body = Some(
        """{"type":"object","properties":{"banks":{"type":"array","items":{"type":"object","properties":{"website":{"type":"string"},"logo":{"type":"string"},"bank_routing":{"type":"object","properties":{"address":{"type":"string"},"scheme":{"type":"string"}}},"short_name":{"type":"string"},"id":{"type":"string"},"full_name":{"type":"string"}}}}}}"""
      ),
      is_featured = false,
      special_instructions = "",
      specified_url = "/obp/v3.1.0/banks",
      connector_methods = List("obp.getBanks", "obp.getBankAccountsForUser")
    )

    assert(getBanksDocJson.operation_id == "OBPv3.0.0-getBanks")
    assert(getBanksDocJson.implemented_by.version == "OBPv3.0.0")
    assert(getBanksDocJson.implemented_by.function == "getBanks")
    assert(getBanksDocJson.request_verb == "GET")
    assert(getBanksDocJson.request_url == "/obp/v3.0.0/banks")
    assert(getBanksDocJson.summary == "Get Banks")
    assert(getBanksDocJson.specified_url == "/obp/v3.1.0/banks")
    assert(getBanksDocJson.tags.length == 3)
    assert(getBanksDocJson.error_response_bodies.length == 1)
    assert(getBanksDocJson.connector_methods.length == 2)
  }
}
