let context;
let oscillator;
let currentWaveform = 'sine'; // default waveform
let currentFrequency = 440; // default frequency
let gainNode; // volume
let panner;

document.getElementById('playTone').addEventListener('click', function() {
    playTone(440, currentWaveform);
});

document.getElementById('frequency').addEventListener('input', function() {
    setFrequency(this.value);
    document.getElementById('frequencyValue').textContent = this.value;
});

document.getElementById('stopTone').addEventListener('click', stopTone);

document.getElementById('volume').addEventListener('input', function() {
    setVolume(this.value);
    document.getElementById('volumeValue').textContent = this.value;
});

const waveformButtons = document.querySelectorAll('.waveformButton');
waveformButtons.forEach(button => {
    button.addEventListener('click', function() {
        setWaveformType(this.dataset.waveform);
    });
});

document.getElementById('pan').addEventListener('input', function() {
    setPan(this.value);
});

document.getElementById('resetPan').addEventListener('click', function() {
    setPan(null, true);
});



function initAudioContext() {
    context = new (window.AudioContext || window.webkitAudioContext)();
    gainNode = context.createGain();
    panner = context.createStereoPanner();

    gainNode.connect(panner);
    panner.connect(context.destination);
    setVolume(0.1); // Set the initial volume to 0.1
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
    oscillator.connect(gainNode); // Connect the oscillator to the gainNode
    gainNode.connect(context.destination); // Connect the gainNode to the destination
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
        oscillator.type = waveformType;
    }
    waveformButtons.forEach(button => {
        if (button.dataset.waveform === waveformType) {
            button.classList.add('active');
        } else {
            button.classList.remove('active');
        }
    });
}

function setFrequency(frequency) {
    currentFrequency = frequency;
    if (oscillator) {
        oscillator.frequency.setValueAtTime(frequency, context.currentTime);
    }
}

function setVolume(volume) {
    if (gainNode) {
        gainNode.gain.setValueAtTime(volume, context.currentTime);
    }
    document.getElementById('volume').value = volume; // Update the slider's value
    document.getElementById('volumeValue').textContent = volume; // Update the displayed volume value
}

function setPan(value, reset = false) {
    if (reset) {
        value = 0;
        document.getElementById('pan').value = 0;
        document.getElementById('panValue').textContent = 0;
    } else {
        document.getElementById('panValue').textContent = value;
    }

    panner.pan.setValueAtTime(value, context.currentTime);
}



