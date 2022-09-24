# Patterns
## Creational patterns
### Factory method pattern
Creating objects derived from an (abstract) base class ("product") using a factory method.
Factory methods are methods creating an instance of the product class and is usually part of a creator - class. Creator classes usually extend an abstract base class and serve the purpose of providing an instance of a concrete product by implementing a factory method. Logic which is not dependent on a concrete product is kept within the base-creator-class and is limited to using the functionality of the base-product.
