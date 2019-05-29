package yonsei.app.hw.command;

import io.vertx.core.json.JsonObject;

public interface JsonFunc {
	JsonObject func(JsonObject param);
}
