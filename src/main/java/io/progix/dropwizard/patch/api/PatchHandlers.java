package io.progix.dropwizard.patch.api;

import io.progix.dropwizard.patch.api.handlers.AddHandler;
import io.progix.dropwizard.patch.api.handlers.CopyHandler;
import io.progix.dropwizard.patch.api.handlers.MoveHandler;
import io.progix.dropwizard.patch.api.handlers.RemoveHandler;
import io.progix.dropwizard.patch.api.handlers.ReplaceHandler;
import io.progix.dropwizard.patch.api.handlers.TestHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class PatchHandlers {

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
