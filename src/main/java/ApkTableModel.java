
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhenhai
 */
public class ApkTableModel extends AbstractTableModel {

    private Vector<ApkItem> apkItems = new Vector<>();

    public ApkTableModel() {
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return java.util.ResourceBundle.getBundle("Bundle").getString("fileName");
        } else {
            return java.util.ResourceBundle.getBundle("Bundle").getString("file_state");
        }
    }

    public ApkItem getApkItem(int row) {
        return apkItems.get(row);
    }

    public void addApkFile(File file) {
        final ApkItem apkItem = new ApkItem(file);
        apkItems.add(apkItem);
        apkItem.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (evt.getPropertyName().equals("uploadProcess")) {
                            float p = (float) evt.getNewValue();
                            int n = apkItems.indexOf(apkItem);
                            if (p > 0.5) {
                                fireTableChanged(new TableModelEvent(ApkTableModel.this, n - 1, n,
                                        1, TableModelEvent.UPDATE));
                            } else {
                                fireTableChanged(new TableModelEvent(ApkTableModel.this, n - 1, n,
                                        1, TableModelEvent.UPDATE));
                            }
                        }
                    }
                });

            }
        });
        fireTableRowsInserted(apkItems.size() - 1, apkItems.size());
    }

    @Override
    public int getRowCount() {
        return apkItems.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return State.class;
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ApkItem item = apkItems.get(rowIndex);
        if (columnIndex == 0) {
            return item.getFile().getName();
        } else if (columnIndex == 1) {
            return item.getApkState();
        }
        return null;
    }

}
