package br.com.leaf.class3.dynamo.controller;

import br.com.leaf.class3.dynamo.model.UsuarioRequest;
import br.com.leaf.class3.dynamo.model.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final DynamoDbClient dynamoDbClient;

    public UsuarioController(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody UsuarioRequest request) {

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(request.getId()).build());
        item.put("nome", AttributeValue.builder().s(request.getNome()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("academia-java-aws")
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")

    public ResponseEntity<UsuarioResponse> buscar(@PathVariable String id) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("academia-java-aws")
                .key(key)
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(getItemRequest).item();

        if (item == null || item.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UsuarioResponse response = new UsuarioResponse(
                item.get("id").s(),
                item.get("nome").s()
        );

        return ResponseEntity.ok(response);

    }
}