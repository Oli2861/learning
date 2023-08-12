function random(number) {
  return Math.floor(Math.random() * (number + 1));
}

function displayMessage(msg) {
    const body = document.body
    const panel = document.createElement("div")
    panel.setAttribute("class", "msgBox")
    body.append(panel)
    
    const msgElement = document.createElement("p")
    msgElement.textContent = msg
    panel.appendChild(msgElement)

    const closeButton = document.createElement("button")
    closeButton.textContent = "x"
    panel.appendChild(closeButton)
    closeButton.addEventListener(
        "click",
        () => panel.parentNode.removeChild(panel)
    )
    closeButton.addEventListener(
        "mouseover",
        () => document.body.style.backgroundColor = `rgb(${random(255)}, ${random(255)}, ${random(255)})`
    )

    const anotherButton = document.createElement("button")
    anotherButton.textContent = "o"
    panel.appendChild(anotherButton)
    anotherButton.addEventListener(
        "click",
        () => {
            panel.parentNode.removeChild(panel)
            console.log("Clicked o Button")
        }
    )

    // Event propagation: Event get propagated to parents. 
    // Order of adding the listeners seeems to affect the order in which they are called.
    panel.addEventListener(
        "click",
        () => {
            document.body.style.backgroundColor = `rgb(0, 0, 0)`
            console.log("Clicked panel")
        }
    )

}
displayMessage("Hello there!")


