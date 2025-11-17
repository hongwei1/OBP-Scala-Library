package com.openbankproject.resourcedocs.core.model

/** JSON-friendly DTO for API resource documentation. This structure avoids framework types and uses primitive-friendly
  * fields.
  */
final case class RoleInfoJson(
    role: String,
    requires_bank_id: Boolean = false
)

/** Represents where an API call is implemented.
  */
final case class ImplementedByJson(
    version: String, // Short hand for the version e.g. "OBPv3.0.0" means Implementations3_0_0
    function: String // The val / partial function that implements the call e.g. "getBanks"
)

/** Extended JSON-friendly DTO for API resource documentation that matches OBP-API structure. This structure includes
  * all fields from OBP-API's ResourceDocJson for compatibility. Field names use snake_case to match OBP-API's
  * ResourceDocJson structure.
  */
final case class OBPResourceDocJson(
    operation_id: String,
    implemented_by: ImplementedByJson,
    request_verb: String,
    request_url: String,
    summary: String,
    description: String, // HTML format
    description_markdown: String, // Markdown format
    example_request_body: Option[String] = None,
    success_response_body: Option[String] = None,
    error_response_bodies: List[String] = List.empty,
    tags: List[String] = List.empty,
    typed_request_body: Option[String] = None, // JSON Schema as string
    typed_success_response_body: Option[String] = None, // JSON Schema as string
    roles: Option[List[RoleInfoJson]] = None,
    is_featured: Boolean = false,
    special_instructions: String = "",
    specified_url: String,
    connector_methods: List[String] = List.empty,
    created_by_bank_id: Option[String] = None
)
