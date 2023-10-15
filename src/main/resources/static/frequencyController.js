document.addEventListener('DOMContentLoaded', function() {
    const frequencySlider = document.getElementById('frequencySlider');
    const frequencyDisplay = document.getElementById('currentFrequency');

    frequencySlider.addEventListener('input', function() {
        console.log('Slider moved');
        updateFrequencyDisplay(this.value);
    });

    function updateFrequencyDisplay(value) {
        frequencyDisplay.textContent = `Current Frequency: ${value}Hz`;
    }

    function setFrequency() {
        const frequency = frequencySlider.value;
        updateFrequencyDisplay(frequency);

        fetch('/audio/setFrequency', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'frequency=' + frequency
        })
        .then(response => response.text())
        .then(data => {
            console.log(data);
        });
    }

    // call this once to initialize the frequency display
    updateFrequencyDisplay(frequencySlider.value);
});