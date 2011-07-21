package model;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import view.BPMObserver;
import view.BeatObserver;

// NOSSO MODELO CONCRETO 

// NOSSO SUBJECT CONCRETO
// È O OBSERVABLE, QUEM SERA OBSERVADO PELA VISUALIZACAO (VIEW)

public class BeatModel implements BeatModelInterface, MetaEventListener {

	private  Sequencer sequencer;
	private ArrayList<BeatObserver> beatObservers;
	private ArrayList<BPMObserver> bpmObservers ;
	
	private int bpm = 90;
	
	private Sequence sequence;
	private Track track;
	
	public BeatModel() {
		beatObservers = new ArrayList<BeatObserver>();
		bpmObservers = new ArrayList<BPMObserver>();
	}
	
	@Override
	public void meta(MetaMessage message) {
		if (message.getType() == 47) {
			beatEvent();
			sequencer.start();
			setBPM(getBPM());
		}
	}

	@Override
	public void initialize() {
		setUpMidi();
		buildTrackAndStart();
	}

	@Override
	public void on() {
		sequencer.start();		
		setBpm(90);
	}

	@Override
	public void off() {
		setBPM(0);
		sequencer.stop();
	}

	@Override
	public void setBPM(int bpm) {
		this.bpm = bpm;
		sequencer.setTempoInBPM(bpm);
		notifyBPMObservers();
	}
	@Override
	public void notifyBPMObservers() {
		Iterator<BPMObserver> iterator = this.bpmObservers.iterator();
		while (iterator.hasNext()) {
			BPMObserver bpmObserver = (BPMObserver) iterator.next();
			bpmObserver.updateBPM();
		}
	}
	@Override
	public void notifyBeatObservers() {
		Iterator<BeatObserver> iterator = this.beatObservers.iterator();
		while (iterator.hasNext()) {
			BeatObserver beatObserver = (BeatObserver) iterator.next();
			beatObserver.updateBeat();
		}
	}

	@Override
	public int getBPM() {
		return this.bpm;
	}
	
	
	@Override
	public void registerObserver(BeatObserver beatObserver) {
		this.beatObservers.add(beatObserver);		
	}

	@Override
	public void removeObserver(BeatObserver beatObserver) {
		int i = this.beatObservers.indexOf(beatObserver);
		if(i>=0){
			this.beatObservers.remove(i);	
		}		
	}

	@Override
	public void registerObserver(BPMObserver bpmObserver) {
		this.bpmObservers.add(bpmObserver);
	}

	@Override
	public void removeObserver(BPMObserver bpmObserver) {
		int i = this.bpmObservers.indexOf(bpmObserver);
		if(i>=0){
			this.bpmObservers.remove(i);
		}		
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	public void setSequencer(Sequencer sequencer) {
		this.sequencer = sequencer;
	}

	public ArrayList<BeatObserver> getBeatObservers() {
		return beatObservers;
	}

	public void setBeatObservers(ArrayList<BeatObserver> beatObservers) {
		this.beatObservers = beatObservers;
	}

	public ArrayList<BPMObserver> getBpmObservers() {
		return bpmObservers;
	}

	public void setBpmObservers(ArrayList<BPMObserver> bpmObservers) {
		this.bpmObservers = bpmObservers;
	}
	
	public void beatEvent(){
		notifyBeatObservers();
	}

	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}
	
	public void buildTrackAndStart() {
		int[] trackList = {35, 0, 46, 0};
		sequence.deleteTrack(null);
		track = sequence.createTrack();
		makeTracks(trackList);
		track.add(makeEvent(192,9,1,0,4));
		try {
			sequencer.setSequence(sequence);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeTracks(int[] list) {
		for (int i = 0; i < list.length; i++) {
			int key = list[i];
			if (key != 0) {
				track.add(makeEvent(144,9,key, 100, i));
				track.add(makeEvent(128,9,key, 100, i+1));
			}
		}
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return event;
	}
	
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(getBPM());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
