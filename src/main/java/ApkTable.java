
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DropMode;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhenhai
 */
public class ApkTable extends JTable {

    private ApkTableModel apkTableModel;
    protected List<Action> actions;
    protected JPopupMenu popupMenu;

    public ApkTable(ApkTableModel apkTableModel) {
        super(apkTableModel);
        this.apkTableModel = apkTableModel;
//        setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
        getTableHeader().setReorderingAllowed(false);
        setDefaultRenderer(State.class, new StateTableRenderder());
        setDefaultEditor(State.class, new StateTableEditor());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setFillsViewportHeight(true);
        setRowHeight(30);
        initDND();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    getPopupMenu().show(ApkTable.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    getPopupMenu().show(ApkTable.this, e.getX(), e.getY());
                }
            }

        });
    }

    public ApkTable() {
        this(new ApkTableModel());
    }

    private void initDND() {
        setDragEnabled(true);
        setDropMode(DropMode.INSERT_ROWS);
        setTransferHandler(new TransferHandler() {

            public boolean canImport(TransferHandler.TransferSupport support) {
                // for the demo, we'll only support drops (not clipboard paste)
                if (!support.isDrop()) {
                    return false;
                }

                // we only import Strings
                if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return false;
                }

                return true;
            }

            public boolean importData(TransferHandler.TransferSupport support) {
                // if we can't handle the import, say so
                if (!canImport(support)) {
                    return false;
                }

                // fetch the drop location
                JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

                int row = dl.getRow();
                List<File> data;

                // fetch the data and bail if this fails
                try {
                    data = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File f : data) {
                        apkTableModel.addApkFile(f);
                    }
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        });

    }

    /**
     * @return the actions
     */
    public List<Action> getActions() {
        if (actions == null) {
            actions = new ArrayList<>();
            actions.add(new AbstractAction(java.util.ResourceBundle.getBundle("Bundle").getString("openFile")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setMultiSelectionEnabled(true);
//                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                            "JPG & GIF Images", "apk", "APK");
//                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File[] fs = chooser.getSelectedFiles();
                        for (File f : fs) {
                            apkTableModel.addApkFile(f);
                        }
                    }
                }
            });
            actions.add(new AbstractAction(java.util.ResourceBundle.getBundle("Bundle").getString("startDownload")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] rs = getSelectedRows();
                    for (int r : rs) {
                        apkTableModel.getApkItem(r).actionPerformed(e);
                    }
                }
            });
        }
        return actions;
    }

    /**
     * @param popupMenu the popupMenu to set
     */
    public JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            List<Action> as = getActions();
            for (Action a : as) {
                popupMenu.add(a);
            }
        }
        return popupMenu;
    }

}
