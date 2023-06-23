package dev.kurumiDisciples.javadex.api.entities.relationship;

import javax.json.JsonArray;
import javax.json.JsonObject;
import dev.kurumiDisciples.javadex.api.entities.enums.RelationshipType;

import java.util.*;

public class RelationshipMap extends HashMap<RelationshipType, List<UUID>> {
    private final JsonArray relationshipArray;

    public RelationshipMap(JsonArray relationshipArray) {
        this.relationshipArray = relationshipArray;
        this.putAll(convertToMap(relationshipArray));
    }

    private static Map<RelationshipType, List<UUID>> convertToMap(JsonArray relationshipArray) {
        Map<RelationshipType, List<UUID>> map = new HashMap<>();
        for (JsonObject relationship : relationshipArray.getValuesAs(JsonObject.class)) {
            String typeId = relationship.getString("type");
            UUID id = UUID.fromString(relationship.getString("id"));
            RelationshipType type = RelationshipType.fromString(typeId);
            if (type == null) {
                continue;
            }
            map.computeIfAbsent(type, k -> new ArrayList<>()).add(id);
        }
        return map;
    }
}
