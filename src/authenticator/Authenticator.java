/**
 * @author angela razzell 
 * amended from this password demo example: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/PasswordDemoProject/src/components/PasswordDemo.java
 */

package authenticator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class Authenticator extends JPanel
                          implements ActionListener {
    private static final String OK = "ok";
    private static final String HELP = "help";
    private String concat1;
    private String concat2;
    private JLabel qLabel;
    
    private final JFrame controllingFrame; //needed for dialogs
    private final JPasswordField secretField;
    private final JPasswordField passwordField;
    private JTextField questionField;
    Whirlpool w = new Whirlpool();
    //private int userNo = 0;

        
    private void setConcat1(String answer) {
       this.concat1 = answer;
    }

    private String getConcat1() {
       return concat1;
    }
    
    public Authenticator(JFrame f, int userNo, String question, String answer) {
        controllingFrame = f; 
        
        secretField = new JPasswordField(15);
        secretField.setActionCommand(OK);
        
        passwordField = new JPasswordField(15);
        passwordField.setActionCommand(OK);
        
        setConcat1(answer);
        
        JLabel secretLabel = new JLabel("Shared Secret: ");
        secretLabel.setLabelFor(secretField);
        
        JComponent buttonPane = createButtonPanel();

        //Lay out everything.
        JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));

        textPane.add(secretLabel);
        textPane.add(secretField);
        //First/initiating user only:
        if (userNo == 0) {
        //Use the default FlowLayout.

            questionField = new JTextField(15); //can be blank
            qLabel = new JLabel("Question: ");
            qLabel.setLabelFor(questionField);

            textPane.add(qLabel);
            textPane.add(questionField);
            
            JLabel ansLabel = new JLabel("Answer: ");
            ansLabel.setLabelFor(passwordField);
            textPane.add(ansLabel);
            
        } else {
            JLabel ansLabel = new JLabel(question);
            ansLabel.setLabelFor(passwordField);
            textPane.add(ansLabel);
        }

        textPane.add(passwordField);
        secretField.addActionListener(this);
        passwordField.addActionListener(this);
        add(textPane);
        add(buttonPane);
        
    }
    
    protected JComponent createButtonPanel() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JButton okButton = new JButton("OK");
        JButton helpButton = new JButton("Help");

        okButton.setActionCommand(OK);
        helpButton.setActionCommand(HELP);
        okButton.addActionListener(this);
        helpButton.addActionListener(this);

        p.add(okButton);
        p.add(helpButton);

        return p;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        String input1 = Arrays.toString(secretField.getPassword());
        String input2;
        input2 = Arrays.toString(passwordField.getPassword());
        if (!OK.equals(cmd)) { //The user has asked for help.
            JOptionPane.showMessageDialog(controllingFrame,
                    "Enter the password you have agreed with your contact.\n"
                            + "User 1 asks a question you both know the answer to,\n"
                            + "in order to verify user 2's identity.");
        } else { //process the input
            if ("[]".equals(input1)) {
                JOptionPane.showMessageDialog(controllingFrame,
                        "Secret not populated. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            } else if ("[]".equals(input2)) {
                JOptionPane.showMessageDialog(controllingFrame,
                        "Password not populated. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String input3 = questionField.getText();
                    //send request to user 2
                    JOptionPane.showMessageDialog(controllingFrame,
                            "Hashing together secret and password...\n"
                                    + "Authentication request sent to other user."
                                    + getConcat1());
                    //concat1 = input1 + input2;
                    setConcat1(w.hashString(input1+input2));
                    createAndShowGUI(1, input3, getConcat1());
                }
                
                catch (NullPointerException err) {
                    concat2 = w.hashString(input1 + input2);
                    if (!isMatch(getConcat1().toCharArray(),concat2.toCharArray())) {
                        JOptionPane.showMessageDialog(controllingFrame,
                                "No match, try again");
                    } else {
                        JOptionPane.showMessageDialog(controllingFrame,
                                "Successfully Verified");                        
                    }

                }
                
                
            }

            //System.out.println(input,padding);
            //Zero out the possible password, for security.
            Arrays.fill(secretField.getPassword(), '0');

            passwordField.selectAll();
            resetFocus();
            
        }
    }
    
    private static boolean isMatch(char[] input1, char[] input2) {
        return Arrays.equals (input1, input2);
    }
    
    /*private static boolean isFieldBlank(String input) {
        return input.isEmpty();
    }*/

    //Must be called from the event dispatch thread.
    protected void resetFocus() {
        secretField.requestFocusInWindow();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
        private static void createAndShowGUI(int userNo, String question, String answer) {
        //Create and set up the window.
        JFrame frame = new JFrame("Ang-thenticator: User " + (userNo+1));
        //JFrame frame2 = new JFrame("Ang-thenticator request");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        //Create and set up the content pane.
        final Authenticator newContentPane = new Authenticator(frame,userNo,question,answer);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Make sure the focus goes to the right component
        //whenever the frame is initially given the focus.
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                newContentPane.resetFocus();
            }
        });
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Whirlpool w = new Whirlpool();
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            
            createAndShowGUI(0,"","");
        });
      
    }
    
}
