window.setContentView = function setContentView() {
    // Get the vaadin-form-layout element by its ID
    const formLayout = document.getElementById('my-top-form-layout');

    // Check if the shadow DOM is supported
    if (formLayout.shadowRoot) {
        // Access the shadow DOM
        const shadowRoot = formLayout.shadowRoot;

        // Get the divs inside the shadow DOM
        const innerDivs = shadowRoot.querySelectorAll('div');

        // Iterate over the inner divs and apply styles
        innerDivs.forEach(innerDiv => {
            // Add your custom styles to each inner div
            innerDiv.style.display = 'flex';
            innerDiv.style.alignItems = 'stretch';
            innerDiv.style.flexDirection = 'row';
            innerDiv.style.alignContent = 'center';
            innerDiv.style.justifyContent = 'center';
            innerDiv.style.flexWrap = 'wrap';
        });
    } else {
        console.error('Shadow DOM is not supported.');
    }
}
