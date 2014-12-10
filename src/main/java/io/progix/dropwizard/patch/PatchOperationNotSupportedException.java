/**
 * 
 */
package io.progix.dropwizard.patch;

import io.progix.dropwizard.patch.explicit.PatchOperation;

/**
 * This exception is thrown when a PATCH operation is implemented within a
 * resource.
 * 
 * @author Tariq Bugrara
 *
 */
public class PatchOperationNotSupportedException extends RuntimeException {

	private static final long serialVersionUID = -300115785663781825L;

	public PatchOperationNotSupportedException() {
		super();
	}

	/**
	 * @param operation
	 *            The operation implementation not supported for this resource.
	 */
	public PatchOperationNotSupportedException(PatchOperation operation) {
		this(operation, null);
	}

	/**
	 * @param operation
	 *            The operation implementation not supported for this resource.
	 * @param t
	 */
	public PatchOperationNotSupportedException(PatchOperation operation, Throwable t) {
		super("The PATCH operation " + operation.name() + " is not supported for this resource.", t);
	}

}
