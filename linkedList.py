class LinkedList:

    def __init__(self, *, first = None):
        self.head = None if first == None else Item(first)
        self.size = 0 if first == None else 1
        

    def __str__(self):
        s = f"Head: {self.head} Size: {self.size} Items: ["
        for index, item in enumerate(self):
            if index > 0:
                s += ", "
            s += str(item)
        s += "]"
        return s

    def __iter__(self):
        return LinkedListIterator(self.head)

    def append(self, payload):
        """Append an item to the list"""
        item = Item(payload)
        if self.head == None:
            self.head = item 
        else:
            curr = self.head
            while curr != None:
                if curr.nxt is None:
                    curr.nxt = item
                    item.prev = curr
                    self.size += 1
                    return curr.nxt
                else:
                    curr = curr.nxt

    def appendAll(self, iterable):
        """Append all items of an given iterable to the end of the current list"""
        last = self.head
        while last := last.nxt:
            if last.nxt is None:
                break

        for payload in iterable:
            item = Item(payload)
            last.nxt = item
            item.prev = last
            last = item

    
    def get(self, index):
        """Get the item at the specified index"""
        for idx, item in enumerate(self):
            if idx == index:
                return item.payload
            else:
                continue
    
    def delete(self, index):
        """Delete the item at the specified index"""
        if self.head is None:
            return None
        item = self.head
        currIndex = 0
        while item := item.nxt:
            currIndex += 1
            if currIndex == index:
                item.prev.nxt = item.nxt
                item.nxt.prev = item.prev
                self.size -= 1
                return item.payload

    def indexOf(self, payload):
        """Get the index of the first occurence of the specified payload"""
        for index, item in self:
            if payload == item.payload:
                return index

    def subList(self, start, stop):
        """Get a sublist of the current list"""
        newList = LinkedList()
        for index, item in enumerate(self):
            if index >= start and index <= stop:
                newList.append(item)
        return newList
    
    def getSize(self):
        """Get the amount of stored items"""
        return self.size


class LinkedListIterator:
    def __init__(self, head):
        self.curr = head

    def __iter__(self):
        return self

    def __next__(self):
        if self.curr is None:
            raise StopIteration
        else:
            old = self.curr
            self.curr = self.curr.nxt
            return old.payload

class Item:
    def __init__(self, payload):
        self.payload = payload
        self.nxt = None
        self.prev = None

    def __iter__(self):
        return self

    def __str__(self):
        return f"Payload: {self.payload}"


