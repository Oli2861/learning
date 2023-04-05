# Patterns
## Creational patterns
### Factory method pattern
- Abstract base class
  - Creating objects derived from an (abstract) base class ("product") using a factory method.
- Factory methods are methods creating an instance of the product class and is usually part of a creator - class.
- Creator classes 
  - Usually extend an abstract base class and serve the purpose of providing an instance of a concrete product by implementing a factory method.
- Logic which is not dependent on a concrete product is kept within the base-creator-class and is limited to using the functionality of the base-product.

### Singleton
Create an object which is limited to one instance.
- The instance is usually managed by the class definition and stored in a static variable.
- Access to this variable should be managed in a thread-safe manner.

### Multiton
Similar to the singleton pattern but with a fixed number of n - instances. Instances are managed by a data structure i.e. List or HashMap as a part of the class definition.

### Abstract factory
An Abstract factory consists of an 
- Abstract factory: 
  - Abstract class which defines the signature of factory methods children-classes (concrete factories) have to fulfill. 
- Concrete factories: 
  - Implement the factory methods and return instances of the product class.
--> The abstract factory pattern thereby defines an abstract factory which extended by concrete factories creating subclasses of the types specified in the abstract factory.

### Builder
- Director: 
  - Starts building the object 
  - Defines the order in which the parts are built
  - Provides result of the build process
- Builder: 
  - Defines the interface for creating parts of a product (build steps, build method)
- Concrete builder: 
  - Constructs and assembles parts of the concrete product by implementing the Builder interface
- Product: 
  - The complex object that is being built

### Prototype
- Prototype: 
  - Defines the interface for cloning itself --> clone() method that provides a shallow clone
    - **Shallow** clone: 
      - Copies the object and its **references** to other objects (--> properties of the children reference the same object)
      - If the referenced object is mutable, the clone and the original object will share the same reference
- Concrete prototype: 
  - Implements the operation for cloning itself, can do so by defaulting to parent implementation