package me.oraclebox.auth.service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.springframework.data.mongodb.core.mapping.Document

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        "id",
        "username",
        "password",
        "email",
        "active"
])
class Account {
    @JsonProperty("id")
    String id;
    @JsonProperty("username")
    String username;
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    String password;
    @JsonProperty("email")
    String email;
    @JsonProperty("active")
    Boolean active;
    @JsonProperty("verified") // Email verification passed
    Boolean verified;
    @JsonIgnore
    String via;
    @JsonIgnore
    Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
