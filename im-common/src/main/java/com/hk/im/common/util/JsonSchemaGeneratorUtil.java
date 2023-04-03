package com.hk.im.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import java.io.IOException;

public class JsonSchemaGeneratorUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  /**
   * 将 Java 对象转换为 JSON Schema 字符串
   *
   * @param object 需要转换的 Java 对象
   * @return JSON Schema 字符串
   */
  public static String generateJsonSchema(Object object) {
    try {
      JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(OBJECT_MAPPER);
      JsonSchema jsonSchema = jsonSchemaGenerator.generateSchema(object.getClass());
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate JSON schema for object", e);
    }
  }

  /**
   * 将 Java 对象转换为 JsonNode
   *
   * @param object 需要转换的 Java 对象
   * @return JsonNode 对象
   */
  public static JsonNode generateJsonSchemaAsJsonNode(Object object) {
    try {
      JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(OBJECT_MAPPER);
      JsonSchema jsonSchema = jsonSchemaGenerator.generateSchema(object.getClass());
      return OBJECT_MAPPER.valueToTree(jsonSchema);
    } catch (IOException e) {
      throw new RuntimeException("Failed to generate JSON schema for object", e);
    }
  }
}
