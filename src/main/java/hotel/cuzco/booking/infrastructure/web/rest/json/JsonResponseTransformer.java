package hotel.cuzco.booking.infrastructure.web.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonResponseTransformer implements ResponseTransformer {

    private final ObjectMapper objectMapper;

    public JsonResponseTransformer() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }
}
