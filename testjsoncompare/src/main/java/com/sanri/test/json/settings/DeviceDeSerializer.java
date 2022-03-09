package com.sanri.test.json.settings;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.IntNode;
import com.sanri.test.json.dto.Camera;
import com.sanri.test.json.dto.Device;
import com.sanri.test.json.dto.DoorAccess;

import java.io.IOException;

public class DeviceDeSerializer extends JsonDeserializer<Device> {

    @Override
    public Device deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        if(treeNode == null)return null;
        IntNode deviceTypeNode = (IntNode)treeNode.get("type");
        if(deviceTypeNode == null)return null;

        Number deviceType = deviceTypeNode.numberValue();
        if(deviceType.equals(1)){
            return jsonParser.readValueAs(DoorAccess.class);
        }
        return jsonParser.readValueAs(Camera.class);
    }
}
