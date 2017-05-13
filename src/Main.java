import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

class Main extends JPanel implements KeyListener {
  //Keeps track of which key is pressed
  private boolean[] keyDown = new boolean[250];
  //Holds which key plays what note
  private HashMap<Character, Integer> notes = new HashMap<Character, Integer>();

  //initiate synthesizer variables
  Synthesizer syn;
  MidiChannel[] midChannel;
  Instrument[] instrument;
  int instNum = 88; //change this to change instruments
  int midiChannel = 7;

  public Main() {
    this.setPreferredSize(new Dimension(400, 100));
    addKeyListener(this);

    setKeys(notes);

    try {
      syn = MidiSystem.getSynthesizer();
      syn.open();
      midChannel = syn.getChannels();
      midChannel[midiChannel].programChange(instNum);
      instrument = syn.getAvailableInstruments();
      syn.loadInstrument(instrument[50]);
    } catch (MidiUnavailableException ex) {
      ex.printStackTrace();
    }
  }

  public void addNotify() {
    super.addNotify();
    requestFocus();
  }

  public void keyTyped(KeyEvent e) {}

  public void keyPressed(KeyEvent e) {
    //Adds key to the keyDown hashmap if key is pressed
    if (keyDown[e.getKeyCode()]) {
      return;
    } else {
      //play a note if the key has an assigned note
      if (notes.containsKey(e.getKeyChar()))
        makeSound(notes.get(e.getKeyChar()), 550);
      keyDown[e.getKeyCode()] = true;
    }
  }

  public void keyReleased(KeyEvent e) {
    //Remove the key from the keyDown hashmap if key is released
    keyDown[e.getKeyCode()] = false;
    //If the key played a note, stop that note
    if (notes.containsKey(e.getKeyChar()))
      stopSound(notes.get(e.getKeyChar()));
  }

  public void paintComponent(Graphics g) {
    //Clear the window
    g.clearRect(0, 0, getWidth(), getHeight());
    
    boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    if(!caps){
      g.drawString("Turn Capslock on!", 50, getHeight() / 3);
    }
    
    //Draw keys that are held down
    g.drawString("The key pressed is " + getKeyDown(), 50, getHeight() / 2);
    
    g.drawString("Keys:"+notes.keySet().toString(), 50, getHeight() * 2 / 3);
    repaint();
  }

  public String getKeyDown() {
    //this function returns the keys that are currently pressed
    String keys = "";
    for (int i = 0; i < keyDown.length; i++) {
      if (keyDown[i]) {
        keys += KeyEvent.getKeyText(i);
      }
    }

    return keys;
  }
  public void makeSound(int noteNumber, int velocity) {
    //Plays a note with specified note number
    this.midChannel[midiChannel].noteOn(noteNumber, velocity);
  }

  public void stopSound(int noteNumber) {
    //Stops specified note number
    this.midChannel[midiChannel].noteOff(noteNumber);
  }

  public void setKeys(HashMap<Character, Integer> map) {
    // Maps notes to keys
    map.put('A', 48);
    map.put('W', 49);
    map.put('S', 50);
    map.put('E', 51);
    map.put('D', 52);
    map.put('R', 53);
    map.put('F', 54);
    map.put('G', 55);   //middle C
    map.put('Y', 56);
    map.put('H', 57);
    map.put('U', 58);
    map.put('J', 59);
    map.put('K', 60);
    map.put('O', 61);
    map.put('L', 62);
    map.put('P', 63);
    map.put(';', 64);
    map.put('[', 65);

  }

  public static void main(String[] args) {
    JFrame f = new JFrame("KeyTone");
    f.getContentPane().add(new Main());
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.pack();
    f.setVisible(true);
  }
}
