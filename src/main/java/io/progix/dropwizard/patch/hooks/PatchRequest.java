package io.progix.dropwizard.patch.hooks;

import io.progix.dropwizard.patch.PatchOperationNotSupportedException;
import io.progix.dropwizard.patch.hooks.handlers.AddHandler;
import io.progix.dropwizard.patch.hooks.handlers.CopyHandler;
import io.progix.dropwizard.patch.hooks.handlers.MoveHandler;
import io.progix.dropwizard.patch.hooks.handlers.RemoveHandler;
import io.progix.dropwizard.patch.hooks.handlers.ReplaceHandler;
import io.progix.dropwizard.patch.hooks.handlers.TestHandler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TODO: Need to implement deserializer to convert an array into this object
 * 
 * @author tariq
 *
 */
public abstract class PatchRequest {

	private List<PatchInstruction> instructions;

	public PatchRequest(List<PatchInstruction> instructions) {
		super();
		this.instructions = instructions;
	}

	public List<PatchInstruction> getInstructions() {
		return instructions;
	}

	public void process() {
		for (PatchInstruction instruction : instructions) {
			switch (instruction.getOperation()) {
			case ADD:
				if (addHandler == null) {
					throw new PatchOperationNotSupportedException(PatchOperation.ADD);
				}
				break;
			case COPY:
				break;
			case MOVE:
				break;
			case REMOVE:
				break;
			case REPLACE:
				break;
			case TEST:
				break;
			default:
				break;
			}
		}
	}

	@JsonIgnore
	private AddHandler addHandler;

	@JsonIgnore
	private RemoveHandler removeHandler;

	@JsonIgnore
	private ReplaceHandler replaceHandler;

	@JsonIgnore
	private MoveHandler moveHandler;

	@JsonIgnore
	private CopyHandler copyHandler;

	@JsonIgnore
	private TestHandler testHandler;

	public void add(AddHandler handler) {
		this.addHandler = handler;
	}

	public void remove(RemoveHandler handler) {
		this.removeHandler = handler;
	}

	public void replace(ReplaceHandler handler) {
		this.replaceHandler = handler;
	}

	public void move(MoveHandler handler) {
		this.moveHandler = handler;
	}

	public void copy(CopyHandler handler) {
		this.copyHandler = handler;
	}

	public void test(TestHandler handler) {
		this.testHandler = handler;
	}

}
