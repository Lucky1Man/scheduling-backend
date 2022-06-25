package com.online.scheduling.schedule.implementations.json.serealize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.online.scheduling.schedule.entities.UserAccount;
import com.online.scheduling.user.entities.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer(){
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("email" ,user.getEmail());
        jsonGenerator.writeEndObject();
    }
}
