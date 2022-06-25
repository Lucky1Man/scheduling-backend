package com.online.scheduling.schedule.implementations.json.serealize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.online.scheduling.schedule.entities.UserAccount;

import java.io.IOException;

public class UserAccountSerializer extends StdSerializer<UserAccount> {

    public UserAccountSerializer(){
        this(null);
    }

    public UserAccountSerializer(Class<UserAccount> t) {
        super(t);
    }

    @Override
    public void serialize(
            UserAccount userAccount,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", userAccount.getId());
        jsonGenerator.writeStringField("owner" ,userAccount.getOwner().getEmail());
        jsonGenerator.writeEndObject();
    }
}
