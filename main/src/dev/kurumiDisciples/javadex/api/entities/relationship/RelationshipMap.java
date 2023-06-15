package dev.kurumiDisciples.javadex.api.entities.relationship;

import javax.json.JsonArray;
import javax.json.JsonObject;

import dev.kurumiDisciples.javadex.api.entities.enums.RelationshipType;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class RelationshipMap extends HashMap<RelationshipType, List<UUID>> {
    private final JsonArray relationshipArray;

    public RelationshipMap(JsonArray relationshipArray) {
        this.relationshipArray = relationshipArray;
        this.putAll(convertToMap(relationshipArray)); // This line is added.
    }

    private static HashMap<RelationshipType, List<UUID>> convertToMap(JsonArray relationshipArray) {
        HashMap<RelationshipType, List<UUID>> map = new HashMap<>();
        for (JsonObject relationship : relationshipArray.getValuesAs(JsonObject.class)) {
            String typeId = relationship.getString("type");
            UUID id = UUID.fromString(relationship.getString("id"));
            RelationshipType type = RelationshipType.fromString(typeId);
            if (type == null) {
                continue; // or throw an exception, depending on your requirements
            }
            List<UUID> uuids = map.getOrDefault(type, new ArrayList<>());
            uuids.add(id);
            map.put(type, uuids);
        }
        return map;
    }
}
