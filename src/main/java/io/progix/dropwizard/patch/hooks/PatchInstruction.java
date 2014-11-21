package io.progix.dropwizard.patch.hooks;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing single operation a PATCH request can contain as defined in
 * RFC 6902.
 * 
 * @author tariq
 *
 */
public class PatchInstruction {

	@JsonProperty("op")
	@NotNull
	private PatchOperation operation;

	@NotNull
	private String path;

	private List<Object> value;

	@NotNull
	private String from;

	public PatchInstruction(PatchOperation operation, String path, List<Object> value, String from) {
		this.operation = operation;
		this.path = path;
		this.value = value;
		this.from = from;
	}

	public PatchOperation getOperation() {
		return operation;
	}

	public String getPath() {
		return path;
	}

	public Object getValue() {
		return value;
	}

	public String getFrom() {
		return from;
	}

}
