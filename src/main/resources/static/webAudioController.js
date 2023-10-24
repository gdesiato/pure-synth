let context;
let oscillator;

document.getElementById('playTone').addEventListener('click', function() {
    playTone(440);
});

document.getElementById('stopTone').addEventListener('click', stopTone);

function initAudioContext() {
    // Initialize Audio Context
    context = new (window.AudioContext || window.webkitAudioContext)();
}

function playTone(frequency) {
    if (!context) {
        initAudioContext();
    }

    // Stop the oscillator if it's already playing
    if (oscillator) {
        oscillator.stop();
    }

    oscillator = context.createOscillator();
    oscillator.type = 'sine'; // wave type
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