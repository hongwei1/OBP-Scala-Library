package com.openbankproject.resourcedocs.export

import com.openbankproject.resourcedocs.core.model.{ImplementedByJson, OBPResourceDocJson, RoleInfoJson}
import org.scalatest.funsuite.AnyFunSuite

import java.time.Instant

class OBPLikeJsonExporterSpec extends AnyFunSuite {

  private val getBanksDescriptionHtml: String =
    "<p>Get banks on this API instance<br />\nReturns a list of banks supported on this server:</p>\n<ul>\n<li>ID used as parameter in URLs</li>\n<li>Short and full name of bank</li>\n<li>Logo URL</li>\n<li>Website</li>\n</ul>\n<p>User Authentication is Optional. The User need not be logged in.</p>\n<p><strong>JSON response body fields:</strong></p>\n<p><a href=\"/glossary#address\"><strong>address</strong></a>:</p>\n<p><a href=\"/glossary#bank_routings\"><strong>bank_routings</strong></a>: bank routing in form of (scheme, address)</p>\n<p><a href=\"/glossary#banks\"><strong>banks</strong></a>:</p>\n<p><a href=\"/glossary#full_name\"><strong>full_name</strong></a>: full name string</p>\n<p><a href=\"/glossary#id\"><strong>id</strong></a>: d8839721-ad8f-45dd-9f78-2080414b93f9</p>\n<p><a href=\"/glossary#logo\"><strong>logo</strong></a>: logo url</p>\n<p><a href=\"/glossary#name\"><strong>name</strong></a>: ACCOUNT_MANAGEMENT_FEE</p>\n<p><a href=\"/glossary#scheme\"><strong>scheme</strong></a>: OBP</p>\n<p><a href=\"/glossary#short_name\"><strong>short_name</strong></a>:</p>\n<p><a href=\"/glossary#\"><strong>value</strong></a>: 5987953</p>\n<p><a href=\"/glossary#website\"><strong>website</strong></a>: <a href=\"http://www.openbankproject.com\">www.openbankproject.com</a></p>\n<p><a href=\"/glossary#attributes\">attributes</a>: attribute value in form of (name, value)</p>\n"

  private val getBanksDescriptionMarkdown: String =
    "Get banks on this API instance\nReturns a list of banks supported on this server:\n\n* ID used as parameter in URLs\n* Short and full name of bank\n* Logo URL\n* Website\n\nUser Authentication is Optional. The User need not be logged in.\n\n\n**JSON response body fields:**\n\n\n\n[**address**](/glossary#address): \n\n\n\n[**bank_routings**](/glossary#bank_routings): bank routing in form of (scheme, address)\n\n\n\n[**banks**](/glossary#banks): \n\n\n\n[**full_name**](/glossary#full_name): full name string\n\n\n\n[**id**](/glossary#id): d8839721-ad8f-45dd-9f78-2080414b93f9\n\n\n\n[**logo**](/glossary#logo): logo url\n\n\n\n[**name**](/glossary#name): ACCOUNT_MANAGEMENT_FEE\n\n\n\n[**scheme**](/glossary#scheme): OBP\n\n\n\n[**short_name**](/glossary#short_name): \n\n\n\n[**value**](/glossary#): 5987953\n\n\n\n[**website**](/glossary#website): www.openbankproject.com\n\n\n\n[attributes](/glossary#attributes): attribute value in form of (name, value)\n\n\n"

  private val getBanksSuccessResponse: String =
    """{
      |  "banks": [
      |    {
      |      "id": "gh.29.uk",
      |      "short_name": "short_name ",
      |      "full_name": "full_name",
      |      "logo": "logo",
      |      "website": "www.openbankproject.com",
      |      "bank_routings": [
      |        {
      |          "scheme": "OBP",
      |          "address": "gh.29.uk"
      |        }
      |      ],
      |      "attributes": [
      |        {
      |          "name": "ACCOUNT_MANAGEMENT_FEE",
      |          "value": "5987953"
      |        }
      |      ]
      |    }
      |  ]
      |}""".stripMargin

  private val getBanksTypedSuccessResponse: String =
    """{
      |  "type": "object",
      |  "properties": {
      |    "banks": {
      |      "type": "array",
      |      "items": {
      |        "type": "object",
      |        "properties": {
      |          "bank_routings": {
      |            "type": "array",
      |            "items": {
      |              "type": "object",
      |              "properties": {
      |                "address": {
      |                  "type": "string"
      |                },
      |                "scheme": {
      |                  "type": "string"
      |                }
      |              }
      |            }
      |          },
      |          "website": {
      |            "type": "string"
      |          },
      |          "logo": {
      |            "type": "string"
      |          },
      |          "attributes": {
      |            "type": "array",
      |            "items": {
      |              "type": "object",
      |              "properties": {
      |                "name": {
      |                  "type": "string"
      |                },
      |                "value": {
      |                  "type": "string"
      |                }
      |              }
      |            }
      |          },
      |          "short_name": {
      |            "type": "string"
      |          },
      |          "id": {
      |            "type": "string"
      |          },
      |          "full_name": {
      |            "type": "string"
      |          }
      |        }
      |      }
      |    }
      |  }
      |}""".stripMargin

  private def baseDoc(operationId: String, request_url: String = "/obp/v4.0.0/banks") = OBPResourceDocJson(
    operation_id = operationId,
    implemented_by = ImplementedByJson(version = "OBPv4.0.0", function = "getBanks"),
    request_verb = "GET",
    request_url = request_url,
    summary = "Get Banks",
    description = getBanksDescriptionHtml,
    description_markdown = getBanksDescriptionMarkdown,
    success_response_body = Some(getBanksSuccessResponse),
    error_response_bodies = List("OBP-50000: Unknown Error."),
    tags = List("BankAccountTag1", "BankAccountTag1", "BankAccountTag1", "Bank", "Account Information Service (AIS)", "PSD2"),
    typed_success_response_body = Some(getBanksTypedSuccessResponse),
    specified_url = "/obp/v5.1.0/banks",
    special_instructions = "",
    connector_methods = Nil
  )

  private def escapeForJson(value: String): String =
    value
      .replace("\\", "\\\\")
      .replace("\"", "\\\"")
      .replace("\n", "\\n")
      .replace("\r", "\\r")
      .replace("\t", "\\t")

  private def normalizeWhitespace(value: String): String =
    value.filterNot(_.isWhitespace)

  test("export should match the official getBanks JSON payload exactly") {
    val doc = baseDoc("OBPv4.0.0-getBanks")
 
    val json = OBPLikeJsonExporter.export(Seq(doc), Instant.parse("2025-11-16T21:57:26Z"))

    val expectedJson =
      s"""
         |{
         |  "resource_docs": [
         |    {
         |      "operation_id": "OBPv4.0.0-getBanks",
         |      "implemented_by": {
         |        "version": "OBPv4.0.0",
         |        "function": "getBanks"
         |      },
         |      "request_verb": "GET",
         |      "request_url": "/obp/v4.0.0/banks",
         |      "summary": "Get Banks",
         |      "description": "${escapeForJson(getBanksDescriptionHtml)}",
         |      "description_markdown": "${escapeForJson(getBanksDescriptionMarkdown)}",
         |      "success_response_body": {
         |        "banks": [
         |          {
         |            "id": "gh.29.uk",
         |            "short_name": "short_name ",
         |            "full_name": "full_name",
         |            "logo": "logo",
         |            "website": "www.openbankproject.com",
         |            "bank_routings": [
         |              {
         |                "scheme": "OBP",
         |                "address": "gh.29.uk"
         |              }
         |            ],
         |            "attributes": [
         |              {
         |                "name": "ACCOUNT_MANAGEMENT_FEE",
         |                "value": "5987953"
         |              }
         |            ]
         |          }
         |        ]
         |        },
         |      "error_response_bodies": [
         |        "OBP-50000: Unknown Error."
         |      ],
         |      "tags": [
         |        "BankAccountTag1",
         |        "BankAccountTag1",
         |        "BankAccountTag1",
         |        "Bank",
         |        "Account Information Service (AIS)",
         |        "PSD2"
         |      ],
         |      "typed_success_response_body": {
         |        "type": "object",
         |        "properties": {
         |          "banks": {
         |            "type": "array",
         |            "items": {
         |              "type": "object",
         |              "properties": {
         |                "bank_routings": {
         |                  "type": "array",
         |                  "items": {
         |                    "type": "object",
         |                    "properties": {
         |                      "address": {
         |                        "type": "string"
         |                      },
         |                      "scheme": {
         |                        "type": "string"
         |                      }
         |                    }
         |                  }
         |                },
         |                "website": {
         |                  "type": "string"
         |                },
         |                "logo": {
         |                  "type": "string"
         |                },
         |                "attributes": {
         |                  "type": "array",
         |                  "items": {
         |                    "type": "object",
         |                    "properties": {
         |                      "name": {
         |                        "type": "string"
         |                      },
         |                      "value": {
         |                        "type": "string"
         |                      }
         |                    }
         |                  }
         |                },
         |                "short_name": {
         |                  "type": "string"
         |                },
         |                "id": {
         |                  "type": "string"
         |                },
         |                "full_name": {
         |                  "type": "string"
         |                }
         |              }
         |            }
         |          }
         |        }
         |      },
         |      "is_featured": false,
         |      "special_instructions": "",
         |      "specified_url": "/obp/v5.1.0/banks",
         |      "connector_methods": []
         |    }
         |  ],
         |  "meta": {
         |    "response_date": "2025-11-16T21:57:26Z",
         |    "count": 1
         |  }
         |}
         |""".stripMargin.stripPrefix("\n")

    assert(normalizeWhitespace(json) == normalizeWhitespace(expectedJson))
  }

  test("export should keep duplicates, render roles, and order by operation id") {
    val docWithRoles = baseDoc("BX-getCustomers").copy(
      roles = Some(List(RoleInfoJson("CanRead"))),
      tags = List("A", "A", "B")
    )
    val laterDoc = baseDoc("CX-getBanks").copy(
      success_response_body = None,
      typed_success_response_body = None,
      connector_methods = Nil
    )

    val json = OBPLikeJsonExporter.export(Seq(laterDoc, docWithRoles), Instant.parse("2025-01-01T00:00:00Z"))

    val firstIdx = json.indexOf("BX-getCustomers")
    val secondIdx = json.indexOf("CX-getBanks")
    assert(firstIdx >= 0 && secondIdx > firstIdx)
    assert(json.contains(""""tags": [
                           |        "A",
                           |        "A",
                           |        "B"
                           |      ],""".stripMargin))
    assert(json.contains(""""roles": [
                           |        {
                           |          "role": "CanRead",
                           |          "requires_bank_id": false
                           |        }
                           |      ]""".stripMargin))
  }
}


