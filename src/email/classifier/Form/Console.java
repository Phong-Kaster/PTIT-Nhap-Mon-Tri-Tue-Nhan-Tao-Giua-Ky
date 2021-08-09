/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email.classifier.Form;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Nguyễn Đăng Hậu - N18DCCN060
 * @author Nguyễn Thành Phong - N18DCCN147
 */
public class Console {
    final JFrame frame = new JFrame();
    JTextArea textArea = new JTextArea(24, 80);
    JScrollPane scroll = new JScrollPane(textArea); 
    public Console() {
        textArea.setEditable(false);
        textArea.setCaretPosition(textArea.getDocument().getLength());
        System.setOut(new PrintStream(new OutputStream() {
          @Override
          public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
          }
    }));


    frame.add(scroll);
    frame.setLocationRelativeTo(null);
    frame.setTitle("Console");
  }
  public void init() {
    frame.pack();
    frame.setVisible(true);
  }
  public JFrame getFrame() {
    return frame;
  }

  public void printOnConsole(String s){
     this.textArea.append(s);
  }
  public void clearAll(){
     this.textArea.setText("");
  }
}
