
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhenhai
 */
public class StateTableEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel editor = new JPanel();
    private JProgressBar processBar = new JProgressBar();
    private JButton button = new JButton();
    public static final String TEXT = "...";
    public static final int WIDTH = 20;
    protected JPanel editorComponent = new JPanel();
    protected EditorDelegate delegate;
    protected int clickCountToStart = 0;
    protected Object value;

    public StateTableEditor() {
        delegate = new EditorDelegate();
        editor.setLayout(new BorderLayout());
        editor.add(processBar, BorderLayout.WEST);
        editor.add(button, BorderLayout.CENTER);
//        processBar.setStringPainted(true);
        button.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals("enabled")){
                    stopCellEditing();
                }
            }
        });
    }

//    protected AbstractCustomerEditor() {
//        button = new JButton(TEXT);
//        button.setPreferredSize(new Dimension(AbstractCustomerEditor.WIDTH, button.getPreferredSize().height));
//        button.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                value = getSelectObject(value);
//                textField.setText(getText(value));
//                AbstractCustomerEditor.this.stopCellEditing();
//            }
//        });
//        delegate = new EditorDelegate();
//        editorComponent.setLayout(new BorderLayout());
//        editorComponent.add(textField, BorderLayout.CENTER);
//        editorComponent.add(button, BorderLayout.EAST);
//    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
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
        return editor;
    }

    @Override
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        super.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        super.removeCellEditorListener(l);
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    protected class EditorDelegate implements ActionListener, ItemListener,
            Serializable {

        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            stopCellEditing();
        }
    }

}
