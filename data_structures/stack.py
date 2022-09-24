class Stack:

    def __init__(self, top):
        self.top = Item(top)

    def push(self, payload):
        item = Item(payload)
        item.nxt = self.top
        self.top = item

    def pop(self):
        oldTop = self.top
        self.top = self.top.nxt
        return oldTop.payload

    def remove(self, payload):
        if payload == self.top.payload:
            top = self.top.nxt if self.top.nxt != None else None

        curr = self.top
        prev = self.top

        while curr := curr.nxt:
            if curr.payload == payload:
                prev.nxt = curr.nxt
                return curr

    def __iter__(self):
        return StackIterator(self.top)


class StackIterator:
    
    def __init__(self, top):
        self.current = top

    def __iter__(self):
        return self

    def __next__(self):
        if self.current is None:
            raise StopIteration
        else:
            old = self.current
            self.current = self.current.nxt
            return old.payload

class Item:

    def __init__(self, payload):
        self.payload = payload
        self.nxt = None

    def __str__(self):
        return f"Payload: {self.payload} Next item: {self.nxt}"

