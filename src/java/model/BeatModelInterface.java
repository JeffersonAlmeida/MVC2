package model;
import view.BPMObserver;
import view.BeatObserver;

// ALEM DE SER NOSSA INTERFACE DE MODELO,
// É TAMBÉM NOSSA INTERFACE SUBJECT
public interface BeatModelInterface {
	
	void initialize();
	void on();
	void off();
	void setBPM(int bpm);
	
	int getBPM();
	
	void registerObserver(BeatObserver beatObserver);
	void removeObserver(BeatObserver beatObserver);
	void registerObserver(BPMObserver beatObserver);
	void removeObserver(BPMObserver beatObserver);
	
	public void notifyBPMObservers();
	public void notifyBeatObservers(); 
	
}
