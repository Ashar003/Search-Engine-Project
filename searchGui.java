import java.io.*;
import java.util.*;
import java.awt.*; 
import javax.swing.*;
import java.awt.event.*;
//import statements

//searchGui

public class searchGui extends JFrame implements ActionListener {
	// JTextField 
	static JTextField t; //type

	// JFrame
	static JFrame f; //frame

	JPanel mainPane;
	private final JPanel topPanel;       // container panel for the top
    private final JPanel bottomPanel;    // container panel for the bottom
	// private final JPanel textPanel;
	private JScrollPane scrollPane ; //scrollpane for search results

	// JButtons
	static JButton x; // choose location for query
	static JButton b; // submit queries
	static JButton c;// corpus
	static JButton s;// use your own stopword
	// static JButton save;// save
	// label to display text
	static JLabel l;
	static JTextArea tf; // text area that displays what's typed in

	//
	static JSplitPane p; 
	
	// static JSplitPane text; 

	


	// file chooser
	static JFileChooser f1;

	// essential stuff

	// invertedIndex
	private invertedIndex instance;

	// stopWord
	private stopWord sW;

	// query
	private query qW;
	

	//CHECKS TO SEE if everything is loaded

	// queryOutputFIle
	private Boolean QueryOutputFile = false;
	// corpus
	private  Boolean isCorpusLoaded = false;
	//stopWord
	private  Boolean isStopWord = false;

	searchGui() {
		// create a new frame to store text field and button
		f = new JFrame("Search Engine");

		// create a label to display text
		l = new JLabel("nothing entered");

		// create a new button
		b = new JButton("Submit Query");
		x = new JButton("Choose Output Location");
		c = new JButton("Upload Your Corpus");

		s = new JButton("Upload Your StopWords");
	
		// addActionListener to button
		b.addActionListener(this);
		c.addActionListener(this);
		s.addActionListener(this);
		x.addActionListener(this);

		///
		mainPane = new JPanel();
		topPanel = new JPanel();         // our top component
		bottomPanel = new JPanel(); 
		scrollPane = new JScrollPane(bottomPanel);
		// textPanel = new JPanel();

		// create a object of JTextField with 16 columns
		t = new JTextField(5);

		// create a panel to add buttons and textfield
		p = new JSplitPane();
		// text = new JSplitPane();

		// add buttons and textfield to panel
		mainPane.add(t);
		mainPane.add(b);
		mainPane.add(c);
		mainPane.add(s);
		mainPane.add(l);
		mainPane.add(x);
		
		topPanel.add(mainPane);
		// p.setLayout(new GridLayout(1,0)); 

		// add panel to frame
		f.add(p);
		// f.add(text);

		// set the size of frame
		f.setSize(600, 600);
		f.getContentPane().setLayout(new GridLayout());
		f.getContentPane().add(p);
		p.setOrientation(JSplitPane.VERTICAL_SPLIT); 
		// text.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // we want it to split the window verticaly
        p.setDividerLocation(200);                    // the initial position of the divider is 200 (our window is 400 pixels high)
		p.setTopComponent(topPanel);  
		// text.setTopComponent(textPanel); 
		getContentPane().add(BorderLayout.CENTER, scrollPane);
		//                // at the top we want our "topPanel"
		p.setBottomComponent(bottomPanel); 
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		// bottomPanel.add(scrollPane); 
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		// setTFBlank();

		String s = e.getActionCommand(); //get the event action in terms of a string

		if (s.equals("Choose Output Location")) {
			if (QueryOutputFile != true) {
				// System.out.println(" Please choose where you want the queryoutput saved.");
				JFileChooser j = new JFileChooser();
				j.setDialogTitle("Specify a file to save");
				j.showSaveDialog(this);
					try {
						File queryLoc = j.getSelectedFile();
						String filePath = queryLoc.getPath();
						//add a .txt to the file
							if (!filePath.toLowerCase().endsWith(".txt")) {
								queryLoc = new File(filePath + ".txt"); 

					}
					qW = new query(queryLoc); // initalize here, and send in file location
					
						try {
							FileWriter fw = new FileWriter(queryLoc);
							QueryOutputFile = true;
							fw.close(); 
						} catch (IOException error) {
							System.out.println(error);
						}
					} catch (Exception stTry) {
						System.out.println(stTry);
						// l.setText("the user cancelled the operation");
						//TODO: handle exception
					}
					
				}

		}//Choose Output Location

		if (s.equals("Submit Query")) {
			if (!QueryOutputFile) {
				JOptionPane.showMessageDialog(this, "Please choose a destination for your queries.");
			}
			if (!isStopWord) {
				JOptionPane.showMessageDialog(this, "Please upload the stopwords.");
			}
			if (!isCorpusLoaded) {
				JOptionPane.showMessageDialog(this, "Please upload the corpus.");
			}else{

			l.setText(t.getText()); //shows what you typed
			qW.somethingWasTyped(t.getText()); //send to hashMap
			qW.saveQueries(t.getText()); // output to file
			qW.PorterAlgorithm(); // porter algoirthm on query so words can be examined with the document's words

			// instance.crossExamine();
			// System.out.println("done cross examining");
			displayFiles(instance.findDocuments()); // instance is a instance of invertedIndex class. Here findDocuments is run on the query given.
			// instance.findDocuments();

		}
	}//submitQuery

		// if the user cancelled the operation
		// else {
		// 	l.setText("the user cancelled the operation");

		// }

		// //

		// // set the text of the label to the text of the field
		// // l.setText(t.getText());
		// // qW.somethingWasTyped(t.getText());
		// // qW.saveQueries();

		// // set the text of field to blank
		// t.setText(" ");

		if (s.equals("Upload Your Corpus")) {

			if (!QueryOutputFile) {
				JOptionPane.showMessageDialog(this, "Please choose a destination for your queries.");
				
			}
			if (!isStopWord) {
				JOptionPane.showMessageDialog(this, "Please upload the stopwords.");
			} else {

				// create an object of JFileChooser class
				JFileChooser j = new JFileChooser(new java.io.File("."));
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// if the user selects a file
				if (j.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)

				{
					// set the label to the path of the selected file

					l.setText(j.getCurrentDirectory().getAbsolutePath());
					isCorpusLoaded = true; 
					instance = new invertedIndex(j.getSelectedFile().getAbsolutePath(), sW, qW);

				}
				// if the user cancelled the operation
				else

					l.setText("the user cancelled the operation");
			}

		} // Uploadcorpus

		if (s.equals("Upload Your StopWords")) {
			// create an object of JFileChooser class
			// System.out.println(
			// 		" If you have your own stop words list, and would like to use the provided stop words list, drop the file in ther directory stopWords, otherwise just click on your file."
			// 				+ "If you don't have your own stop words list, please click on directory called stopWords.");
			JFileChooser j = new JFileChooser(new java.io.File("."));
			j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			// if the user selects a file
			if (j.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)

			{
				// set the label to the path of the selected file
				l.setText(j.getSelectedFile().getAbsolutePath());
				sW = new stopWord(j.getSelectedFile().getAbsolutePath());
				isStopWord = true;
			}
			// if the user cancelled the operation
			else
				l.setText("the user cancelled the operation");

		} // stopword
	}//actionPerformed


	//This method allows appending of files to the gui
	public void displayFiles(HashSet<Integer> fin) {
		StringBuilder str = new StringBuilder();
		tf = new JTextArea();
		bottomPanel.remove(tf);
		int ans;
		Iterator<Integer> finalAns = fin.iterator(); 
		int count = 1;
		// scrollPane= new JScrollPane(bottomPanel);
			// scrollPane = new JScrollPane(bottomPanel);  //Let all scrollPanel has scroll bars
			// scrollPane.setPreferredSize(new Dimension(1000, 900));


			while(finalAns.hasNext()){ //iterator method
				// for (Integer fileNumber: instance.sources.keySet()){
			// if(fileNumber == (finalAns.next())){
				 ans= finalAns.next(); 
					if(instance.sources.containsKey(ans)){ //if the document number is valid
					
					// System.out.println(ans + "I'm here");
					str.append("This is document "+ count++ +" " + instance.sources.get(ans).getName() + "\n"); //append to string
					// tf.setText(instance.sources.get(ans).getName());
				}

				// System.out.println(finalAns.next());


			// tf.setText(instance.sources.get(finalAns.next()).getName());
			// System.out.println(instance.sources.get(finalAns.next()).getName() + "found");
		}
		bottomPanel.add(tf); // 
		qW.saveQueries(str.toString()); // output to file
	   
		tf.setText(str.toString()); //append to gui
		
	}//displayFiles

	// public void setTFBlank(){ //sets the textField to blank
	// 	// // set the text of field to blank
	// 	t.setText(" ");
	// }
			
	  }//searchGui
	


	


	

   
    

	