const frequencySlider = document.getElementById('frequencySlider');
const frequencyDisplay = document.getElementById('currentFrequency');
const frequencyInput = document.getElementById('frequencyInput');

// Update the display when the slider is moved
frequencySlider.addEventListener('input', function() {
    console.log("Slider moved. Current value:", this.value); // log the current value of the slider when it's moved
    updateFrequencyDisplay(this.value);
});

// Function to update the frequency display
function updateFrequencyDisplay(value) {
    frequencyDisplay.textContent = `Current Frequency: ${value}Hz`;
}

// Function to set frequency using the slider value and send it to the server
function setFrequency() {
    const frequency = frequencySlider.value;
    updateFrequencyDisplay(frequency);
    sendFrequencyToServer(frequency);
}

// Function to set specific frequency using the input field value and send it to the server
function setSpecificFrequency() {
    const specificFrequency = frequencyInput.value;
    updateFrequencyDisplay(specificFrequency);
    sendFrequencyToServer(specificFrequency);
}

// Function to set the frequency using the current value of the slider
function setSliderFrequency() {
    const sliderFrequency = frequencySlider.value;
    updateFrequencyDisplay(sliderFrequency);
    sendFrequencyToServer(sliderFrequency);
}

// Function to send frequency value to the server
function sendFrequencyToServer(frequencyValue) {
    fetch('/audio/setFrequency', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'frequency=' + frequencyValue
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.error('Error sending frequency to server:', error);
    });
}

// Initialize the frequency display
updateFrequencyDisplay(frequencySlider.value);
