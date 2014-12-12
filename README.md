Dropwizard Patch Implementation
================

About
-----
This Dropwizard bundle provides an implementation for HTTP Patch for resources in [Dropwizard](dropwizard.github.io). This implementation is based off of
[RFC6902](https://tools.ietf.org/html/rfc6902), a proposed standard for updating resources. Currently, only an explicit mode of patching is supported
by this implementation. A more implicit mode of patching is in the works that doesn't require as much code to update a resource.

How does JSON Patch work?
-------------------------
[RFC6902](https://tools.ietf.org/html/rfc6902) proposes a method of patching to consist of multiple patch instructions. Each patch instruction
contains information about how a resource should be changed.

    Example patch document
    [
        {"op": "add",    "path": "/pets/0", "value:": "Mogget"},
        {"op": "remove", "path": "/pets"}
    ]

### What's the path mean?
The `path` property of a patch instruction references the property of the resource to patch. In the above case, the add operation will add the `value`
to the 0th element of the `pets` array. Likewise, the remove operation will remove the `pets` array entirely. The path format is specified in
[RFC6901](https://tools.ietf.org/html/rfc6901).

### Operations
There are six patch operations listed in the specification. For your convenience, here is a brief summary of each operation. Please note that this is
not an official summary and if you'd like more a more detailed explanation, check out the actual document ([RFC6902](https://tools.ietf.org/html/rfc6902)).

####add
Depending on what part of a resource you point to with the `path` property, this operation can be used in three ways.

- Add an element to an array
- Add a non-existent property and value
- Replace an existent property with a value

####copy
The copy operation can be used to copy any part of a resource to another part.

####move
The move operation can be used to move any part of a resource to another part.

####remove
The remove operation removes a property of a resource in its entirety. Don't confuse this with writing null to a property, use the replace
operation with a value of null to accomplish this. This operation however is useful for remove elements of an array.

####replace
This operation simply replaces a property or element with a value given.

####test
This operation can be used to validate a part of a resource either before or after other patch instructions.

Getting started
---------------
To integrate this implementation into your Dropwizard project, add the `PatchBundle` in the `initialize()` method of your `Application` class.

    import io.progix.dropwizard.patch.PatchBundle;

    @Override
    public void initialize(final Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(new PatchBundle());
    }

Explicit patching
-----------------
This mode of patching can be achieved by setting a `PatchRequest` instance as your entity in the resource method annotated with `@PATCH`