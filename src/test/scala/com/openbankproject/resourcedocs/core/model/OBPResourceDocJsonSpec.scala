package com.openbankproject.resourcedocs.core.model

import io.circe.{Json => CirceJson}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import json._

case class SampleRequest(id: String, quantity: Int, tags: List[String])
case class SampleResponse(success: Boolean, message: Option[String])

class OBPResourceDocJsonSpec extends AnyFlatSpec with Matchers {

  // Define implicits using the macro
  implicit val sampleRequestSchema: Schema[SampleRequest] = Json.schema[SampleRequest]
  implicit val sampleResponseSchema: Schema[SampleResponse] = Json.schema[SampleResponse]

  "OBPResourceDocJson.generateSchema" should "generate correct Draft-04 schema for a case class" in {
    val schema: CirceJson = OBPResourceDocJson.generateSchema[SampleRequest]

    val schemaObj = schema.asObject.get
    
    // Handle Schema with Definitions (Draft-04 style)
    // The root might contain $ref, and properties are in definitions
    val properties = if (schemaObj.contains("definitions")) {
      val definitions = schemaObj("definitions").flatMap(_.asObject).get
      // Assuming only one definition is generated for the simple case class
      definitions.values.head.asObject.get("properties").flatMap(_.asObject).get
    } else {
      schemaObj("properties").flatMap(_.asObject).get
    }
    
    properties("id").flatMap(_.asObject).flatMap(_("type").flatMap(_.asString)) should be(Some("string"))
    properties("quantity").flatMap(_.asObject).flatMap(_("type").flatMap(_.asString)) should be(Some("integer"))
    
    // Verify array handling
    val tagsProp = properties("tags").flatMap(_.asObject).get
    tagsProp("type").flatMap(_.asString) should be(Some("array"))
    tagsProp("items").flatMap(_.asObject).flatMap(_("type").flatMap(_.asString)) should be(Some("string"))
    
    // Verify required fields
    // If using definitions, 'required' is inside the definition
    val requiredSource = if (schemaObj.contains("definitions")) {
       val definitions = schemaObj("definitions").flatMap(_.asObject).get
       definitions.values.head.asObject.get
    } else {
       schemaObj
    }
    val required = requiredSource("required").flatMap(_.asArray).getOrElse(Vector.empty).flatMap(_.asString)
    required should contain allOf ("id", "quantity", "tags")
  }

  it should "handle Option types as optional fields in schema" in {
    val schema: CirceJson = OBPResourceDocJson.generateSchema[SampleResponse]
    
    val schemaObj = schema.asObject.get
    
    val requiredSource = if (schemaObj.contains("definitions")) {
       val definitions = schemaObj("definitions").flatMap(_.asObject).get
       definitions.values.head.asObject.get
    } else {
       schemaObj
    }
    
    val required = requiredSource("required").flatMap(_.asArray).map(_.flatMap(_.asString)).getOrElse(Vector.empty)
    
    // 'success' should be required, 'message' should NOT be in required list
    required should contain("success")
    required should not contain "message"
  }
}
