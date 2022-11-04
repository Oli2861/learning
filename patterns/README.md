# Patterns
## Creational patterns
### Factory method pattern
Creating objects derived from an (abstract) base class ("product") using a factory method.
Factory methods are methods creating an instance of the product class and is usually part of a creator - class. Creator classes usually extend an abstract base class and serve the purpose of providing an instance of a concrete product by implementing a factory method. Logic which is not dependent on a concrete product is kept within the base-creator-class and is limited to using the functionality of the base-product.

### Singleton
Create an object which is limited to one instance. The instance is usually managed by the class definition and stored in a static variable. Access to this variable should be managed in a thread-safe manner.

### Multiton
Similar to the singleton pattern but with a fixed number of n - instances. Instances are managed by a data structure i.e. List or HashMap as a part of the class definition.

### Abtract factory
An Abstract factory consists of an abstract class which defines the signature of factory methods children-classes (concrete factories) have to fulfill. 
The abstract factory pattern thereby defines an abstract factory which extended by concrete factories creating subclasses of the types specified in the abstract factory.

### Builder
