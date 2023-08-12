/*
// Numbers
let num = 1.2345;
console.log(num.toFixed(2))

let s = "1123"
console.log(Number(++s) + 2)
console.log(Number(s++) + 2)

let a = 20;
let b = 100;
b /= a;
console.log(b)

a = "10"
b = 10
console.log(a == b)
console.log(a === b) // Also considers whether the datatype is the same.

// Strings Concatenation
a = "10"
b = "20"
let c = a + b
console.log(c)

c = `${a}_${b}`
console.log(c)

// Concatinating Number to String
console.log("test" + 10)
a = "10"

// Checking types
console.log(typeof a)
a = 10
console.log(typeof a)

hey = "Hello World"
for (c in hey)
    console.log(hey[c])

console.log(hey.includes("Hello"))
console.log(hey.indexOf("Hello"))

// Arrays
let arr = ["Pizza", 1, [1, 2, 3]]
console.log(arr.length)
console.log(arr.pop())
console.log(arr.shift())


arr = ["Pizza", 1, [1, 2, 3]]
for (let element in arr) console.log(element)

arr = [2, 1, [1, 2, 3]]
console.log(
    arr.map(curr => 
        curr + 1
    ).filter( curr =>
        curr != 1
    )
)


arr = ["Pizza", 1, [1, 2, 3]]
console.log(arr[-1])

console.log(1 < 2 ? "yes" : "no")

for (i in arr) console.log(i)
for (i of arr) console.log(i)

function say(str="Dunno what to say") {
    console.log(str)
}

say("Hi")

say(
    ["a", "b", "c"].map(
        (function(element) {
            return element.toUpperCase()
        })
    )
)

say(["a", "b", "c"].map(element => element.toUpperCase()))

// Outer (not innere) function gets hoisted.
console.log(makeCalculation("square", 2))
console.log(makeCalculation("factorial", 12))

function makeCalculation(type, num) {
    // Inner functions dont get hoisted --> only callable after declaration.
    function square(num) {
        return num * num
    }
    const factorial = function fac(n) {
        return n < 2 ? 1 : n * fac(n-1)
    }

    if(type === "square")
        return square(num)
    else
        return factorial(num)
}

function outer(x) {
    function inner(y) {
        return x + y
    }
    return inner
}
const fnInner = outer(2)
console.log(fnInner(3))
console.log(outer(3)(2))

// Arguments are maintained in some array-like structure.
function concat() {
    result = ""
    seperator = arguments[0]
    for (arg of arguments) 
        if (arg === seperator) continue 
        else result += arg + seperator
    return result
}
console.log(concat(", ", "Hey", "Hallo", "Moin!", "Hello there!"))

// Restargs / arg list
function concat(seperator, ...args) {
    result = ""
    for (arg of args) 
        if (arg === seperator) continue 
        else result += arg + seperator
    return result
}
console.log(concat(", ", "Hey", "Hallo", "Moin!", "Hello there!"))

// Every function has a this value --> different for nested functions therefore assign it to a constant ("self")
function Person() {
    const self = this
    self.age = 19
    
    function incrementAge() {
        self.age++ 
    }
    // Expose function
    self.incrementAge = incrementAge


}
const p = new Person()
console.log(p.age)
p.incrementAge()
console.log(p.age)


// Initialize objects directly with values
let obj = {x: 3, y: 4}
console.log(obj)
// Intialize object without values & assigning them later
obj = {}
obj.x = 3
obj.y = 5
console.log(obj)
// Direct property access
console.log(obj.x)
console.log(obj.y)
// Access with key
console.log(obj["x"])
console.log(obj["y"])

// Destructuring 
let [first, second, third] = [1, 2, 3]
console.log(first)
console.log(second)
console.log(third)

function logDec2bin(dec) {
    console.log((dec >>> 0).toString(2))
}
// Shifting
let a  = 5
logDec2bin(a)
logDec2bin(a<<2)
logDec2bin(2<<3)
logDec2bin(a<<2<<3)

console.log(new Date())
console.log(new Date(1998, 10, 17, 10, 10))

// Print unicode
console.log("\u{00A9}")
console.log("\u00A9")
const a = "test" // String
const b = new String("test") // Object wrapper around primitive string
console.log(typeof a)
console.log(typeof b)

console.log(eval(new String("2+2")))
console.log(typeof eval(new String("2+2")))
console.log(eval(eval(new String("2+2"))))
console.log(eval("2+2"))

// No array data type but predefined object
const arr = new Array(1, 2, 3, 4)
for(elm of arr) console.log(elm)

// Joins to string with seperator
console.log(arr.join(","))

// Adds element at end & returns length
console.log(arr.push(5))
console.log(arr)
// Last element
console.log(arr.pop())
// First element
console.log(arr.shift())
// Add as first
console.log(arr.unshift(1000))
console.log(arr)

const arr1 = new Array(1, 2, 3, 4).flatMap((item) => [item, item.toString()]).reduce((acc, curr, idx, arr) => acc + curr)
console.log(arr1)
console.log(typeof arr1) // Is now a string since half of the elements were a string and then added in reduce

// Map
const ppl = new Map()
ppl.set("Günni", 29)
ppl.set("Günni", 30)
console.log(ppl)
console.log(ppl["Günni"])

console.log(ppl.has("Günni"))
for( let [k, v] of ppl)
    console.log(`${k} is ${v} years old years old.`)

const ppl1 = new Set()
ppl1.add("Günni")
console.log(ppl1.has("Günni"))
console.log(ppl1)
ppl1.delete("Günni")
console.log(ppl1)

*/
// Objects
// Initializer
let obj = {
    p1: "value 1",
    5: "value 2",
    "9": "value 3"
}



