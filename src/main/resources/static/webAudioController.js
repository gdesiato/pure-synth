let context;
let oscillator;
let currentWaveform = 'sine'; // default waveform

document.getElementById('playTone').addEventListener('click', function() {
    playTone(440, currentWaveform);
});

document.getElementById('stopTone').addEventListener('click', stopTone);

const waveformButtons = document.querySelectorAll('.waveformButton');
waveformButtons.forEach(button => {
    button.addEventListener('click', function() {
        setWaveformType(this.dataset.waveform);
    });
});

function initAudioContext() {
    // Initialize Audio Context
    context = new (window.AudioContext || window.webkitAudioContext)();
}

function playTone(frequency, waveformType) {
    if (!context) {
        initAudioContext();
    }

    // Stop the oscillator if it's already playing
    if (oscillator) {
        oscillator.stop();
    }

    oscillator = context.createOscillator();
    oscillator.type = waveformType;
    console.log('Oscillator type set to:', oscillator.type);
    oscillator.frequency.setValueAtTime(frequency, context.currentTime);
    oscillator.connect(context.destination);
    oscillator.start();
}

function stopTone() {
    if (oscillator) {
        oscillator.stop();
        oscillator = null;
    }
}

function setWaveformType(waveformType) {
    console.log('Setting waveform to:', waveformType);
    currentWaveform = waveformType;
    if (oscillator) {
        stopTone(); // Stop the current oscillator
        playTone(440, waveformType); // Start a new oscillator with the new waveform type
    }
}


