
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import sun.swing.DefaultLookup;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhenhai
 */
public class StateTableRenderder implements TableCellRenderer {

    private StatePanel render = new StatePanel();
    private JProgressBar processBar = new JProgressBar();
    private JButton button = new JButton();

    public StateTableRenderder() { 
        render.setLayout(new BorderLayout());
        render.add(processBar, BorderLayout.WEST);
        render.add(button, BorderLayout.CENTER);
        processBar.setStringPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        State state = (State) value;
        button.setAction(state.getApkItem());
        switch (state.state) {
            case State.Default:
                processBar.setEnabled(true);
                button.setEnabled(true);
                processBar.setValue(0);
                break;
            case State.Uploading:
                processBar.setValue(state.getProcess());
                processBar.setEnabled(true);
                button.setEnabled(false);
                break;
            case State.Uploaded:
                processBar.setEnabled(true);
                button.setEnabled(true);
                processBar.setValue(0);
                break;
        }
        return render;
    }

}
