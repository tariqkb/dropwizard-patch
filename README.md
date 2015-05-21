Dropwizard Patch Implementation
================

About
-----
This small framework provides an implementation for HTTP Patch for resources in [Dropwizard](http://dropwizard.io).
This  implementation is based off of [RFC6902](https://tools.ietf.org/html/rfc6902), a proposed standard for
updating resources.

How does JSON Patch work?
-------------------------
[RFC6902](https://tools.ietf.org/html/rfc6902) proposes a method of patching to consist of multiple patch instructions. Each patch instruction
contains information about how a resource should be changed. Below is an example patch document:

```json
[
    {"op": "add",    "path": "/pets/0", "value:": "Mogget"},
    {"op": "remove", "path": "/pets"}
]
```

### What does the path represent?
The `path` property of a patch instruction references the property of the resource to modify. In the above case, the
add operation will add the `value` to the 0th element of the `pets` array. Likewise, the remove operation will remove
the `pets` array entirely. The path format is specified in [RFC6901](https://tools.ietf.org/html/rfc6901).

### Operations
There are six patch operations listed in the specification. For your convenience, here is a brief summary of each operation. Please note that this is
not an official summary and if you'd like a more detailed explanation, check out the actual document ([RFC6902](https://tools.ietf.org/html/rfc6902)).

####add
Depending on what part of a resource you point to with the `path` property, this operation can be used in three ways.

- Add an element to an array
- Add a non-existent property and value
- Replace an existent property with a value

####copy
The `copy` operation can be used to copy any part of a resource to another part of that resource.

####replace
This operation simply replaces a property or element with a given value.

####remove
The `remove` operation removes a property or element of a resource in its entirety. Do not confuse this with writing
`null` to a property; use the `replace` operation with a value of `null` to accomplish this. This operation however is
useful for removing elements of an array.

####move
The move operation can be used to move any part of a resource to another part of that resource.

####test
This operation can be used to validate a part of a resource either before or after other patch operations.

Getting started
---------------
```xml
<dependency>
    <groupId>io.progix.dropwizard</groupId>
    <artifactId>dropwizard-patch</artifactId>
    <version>0.2.2</version>
</dependency>
```

There are three ways an operation can be executed 

- Default: automatically applies an operation to a Java object by serializing, patching, and deserializing
- Contextual: Like default, but instead of automatically patching, simply provides the operation information and target object for you to manipulate
- Basic: Like contextual, but does not target a object

BasicJsonPatch
--------------
This mode of patching can be achieved by setting a `BasicJsonPatch` instance as your entity in the resource method. An
example is shown below:

```java
@PATCH
@Path("/{id}")
public void patchUser(@PathParam("id") int id, BasicJsonPatch request) throws PatchTestFailedException {
    request.setAdd(new AddOperation() {

        @Override
        public void add(JsonPath path, JsonPatchValue value) {
            ...
        }
    });

    request.apply();
}
```

In this example, the `JsonPatch#setAdd()` method is used to set an `AddOperation`, which will contain the logic for the
`add` operation. For each patch instruction in this request, the code inside the `AddOperation#add` method will execute.

The `BasicJsonPatch` also has methods for all other operations listed above, exposing the relevant information.

Note that `BasicJsonPatch#apply()` is required to be called before the resource method returns for any patching
to be preformed!

ContexualJsonPatch
------------------
This mode of patching can be achieved by setting a `ContexualJsonPatch` instance as your entity in the resource method. An
example is shown below:

```java
@PATCH
@Path("/{id}")
public void patchUser(@PathParam("id") int id, ContexualJsonPatch<Person> request) throws PatchTestFailedException {


    request.setAdd(new ContextualAddOperation<Person>() {

        @Override
        public void add(Person person, JsonPath path, JsonPatchValue value) {
            ...
        }
    });
    
    Person person = personDao.findById(id);
    
    Person patchedPerson = request.apply(person);
}
```

Notice that `ContextualJsonPatch` uses a generic type to determine what object should be targeted by the patch operation.

The `Person` object that is exposed in `ContextualAddOperation#add()` represents the target of the operation. Note that 
this a copied object (by value) and not a copied reference. Any changes made to the person object in this method will not 
modify the targeted object directly.

`ContextualJsonPatch#apply(T)` is where the targeted object should be passed in. Applying the patch will return the patched 
object.

DefaultJsonPatch
----------------
This mode of patching can be achieved by setting a `DefaultJsonPatch` instance as your entity in the resource method. An
example is shown below:

```java
@PATCH
@Path("/{id}")
public void patchUser(@PathParam("id") int id, DefaultJsonPatch<Person> request) throws PatchTestFailedException {

    Person person = personDao.findById(id);
    
    Person patchedPerson = request.apply(person);
}
```

Notice that `DefaultJsonPatch` uses a generic type to determine what object should be targeted by the patch operation.

`DefaultJsonPatch#apply(T)` is where the targeted object should be passed in. Applying the patch will return the patched 
object.

This method of patching will use Jackson to serialize the target object, apply the operation automatically, and return 
the deserialized patched object. If the patch transforms the serialized JSON document in such a way that prevents 
deserialization, an error is thrown.

Notice that you do not need to set a `AddOperation` or any other operation handlers. `DefaultJsonPatch` comes 
preconfigured with operation handlers that perform a JSON patch. However, if you would like to have more control over
one or more operations, `DefaultJsonPatch` extends `ContextualJsonPatch`. You can still call the operation handler 
setters to override the default JSON patching handlers.

###JsonPath
`JsonPath` instances contain path information as defined in [RFC6901](https://tools.ietf.org/html/rfc6901) and wrap
around Jackson's `JsonPointer`. The path is split into segments that can be referenced by index. In this class, helper
methods are exposed to make patching easier and more concise.

Building on the example shown above, displayed are a few ways you can use `JsonPath`

```java
request.setRemove(new RemoveOperation() {
    @Override
    public void remove(JsonPath path) {
    
        if (path.property(0).is("pets")) {
        
            if(path.element(1).exists()) {
                int index = path.element(1).val();
                user.getPets().remove(index);
            }
            
        }
        
    }
});
```

As expected, the only information a `remove` operation needs is the `JsonPath`, which is provided in the
`remove` method of the `RemoveHandler`. We use the `path` to get the first segment as a string property with
`JsonPath#property(int)` and check if it's equal to `pets` using the `JsonPathProperty#is(String)` method.

Because `pets` is an array, we should expect the next segment (if any) to contain an integer. To get segments you
know will be integers, the `JsonPath#element(int)` is exposed. Using its `exists()` method, we can confirm the
segment at this index is actually an integer index. At this point, we can extract the actual index referenced in the
`JsonPath` using the `val()` method and update the appropriate data.

The `JsonPathElement#isEndOfArray()` is exposed for the special character `'-'` to symbolize the end of an array.

Note that both `JsonPath#property(int)` and `JsonPath#element(int)` will never return null! They will return a
`JsonPathProperty` and `JsonPathElement`, respectively. The `exists()` method for each can be used to determine what
type the segment is. For each index passed to these methods, only one will return true.

`JsonPath` can also perform a direct equality check to avoid long if-else blocks using `JsonPatch#is(String)`. This 
method requires the given string path to start with a `/`. An important feature/caveat in this method is matching elements.
Because we won't always know the index a `JsonPath` targets, you can use the `#` character in the path to represent
"any number". Because of this, if you need to use the `#` character, prepend it with a `~` (`~#`). See the previous 
example using this method instead:

```java
request.setRemove(new RemoveOperation() {
    @Override
    public void remove(JsonPath path) {
    
        if(path.is("/pets/#")) {
            int index = path.element(1).val();
            user.getPets().remove(index);
        }
        
    }
});
```

####Invalid paths
To provide better client-side information for a path you do not implement patching for, the `JsonPath#endsAt(int)`
method is provided. This method will return true if the index given is the final segment of the path, false if the
path continues. This can be useful to ensure the path is exactly what you expect.

`InvalidPatchPatchException` is also provided to be thrown when you choose not to support a specific path. The above
example is shown below with better error handling (assuming we only allow the array elements of `pets` to be modified.

```java
request.setRemove(new RemoveOperation() {
    @Override
    public void remove(JsonPath path) {
        if (path.property(0).is("pets")) {
            if(path.element(1).exists() && path.endsAt(1)) {
                int index = path.element(1).val();
                user.getPets().remove(index);
            } else {
                throw new InvalidPatchPatchException();
            }
        } else {
            throw new InvalidPatchPatchException();
        }
    }
});
```

###JsonPatchValue
Like `JsonPath`, there is a helper class to store values in a patch instruction. `JsonPatchValue` can store a list of
values or an single value (RFC6902 does not require always using an array for the `value` property).

Because of this, the `JsonPatchValue#many(Class)` method returns a list of all values and `JsonPatchValue#one
(Class)` returns a single value (throws an IndexOutOfBoundsException if there are more than one or no values). The
`Class` passed into these methods is the class the objects for this path you expect. Internally, Jackson is used to
deserialize the objects into the target class.

Complete example
----------------
In the absence of a dedicated sample, you may reference the `UserResource` class used for tests.

FAQ
---
###Why does the do the various test operations return a boolean?
The boolean determines if the test fails or succeeds. If the test fails, a `PatchTestFailedException` is
automatically thrown. Note that you should not return false if the `JsonPath` is invalid, see [Invalid paths]
(#invalid-paths).

###I'm using the BasicJsonPatch in my resource, but nothing happens after a call is made?
Make sure you call `BasicJsonPatch#apply()` before you return within the resource.