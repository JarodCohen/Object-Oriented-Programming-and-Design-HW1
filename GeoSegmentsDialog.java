package homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A JDailog GUI for choosing a GeoSegemnt and adding it to the route shown
 * by RoutDirectionGUI.
 * <p>
 * A figure showing this GUI can be found in homework assignment #1.
 */
public class GeoSegmentsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	// the RouteDirectionsGUI that this JDialog was opened from
	private RouteFormatterGUI parent;
	
	// a control contained in this 
	private JList<GeoSegment> lstSegments;
	
	/**
	 * Creates a new GeoSegmentsDialog JDialog.
	 * @effects Creates a new GeoSegmentsDialog JDialog with owner-frame
	 * 			owner and parent pnlParent
	 */
	public GeoSegmentsDialog(Frame owner, RouteFormatterGUI pnlParent) {
		// create a modal JDialog with the an owner Frame (a modal window
		// in one that doesn't allow other windows to be active at the
		// same time).
        super(owner, "Please choose a GeoSegment", true);
        this.parent = pnlParent;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Label at the top
        JLabel lblTitle = new JLabel("GeoSegments:");
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Create the JList
        DefaultListModel<GeoSegment> model = new DefaultListModel<>();
        for (GeoSegment s : ExampleGeoSegments.segments) {
            model.addElement(s);
        }
        lstSegments = new JList<>(model);
        lstSegments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstSegments.setVisibleRowCount(8);
        lstSegments.setFixedCellWidth(500);

        JScrollPane scrollPane = new JScrollPane(lstSegments);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton btnAdd = new JButton("Add");
        JButton btnCancel = new JButton("Cancel");

        JPanel pnlButtons = new JPanel(new BorderLayout());
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        leftButtons.add(btnAdd);
        rightButtons.add(btnCancel);

        pnlButtons.add(leftButtons, BorderLayout.WEST);
        pnlButtons.add(rightButtons, BorderLayout.EAST);
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(pnlButtons, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> addSelectedSegment());
        btnCancel.addActionListener(e -> dispose());

        pack(); // Sizes window to fit content
        setLocationRelativeTo(owner); // Center on screen
    }


	    private void addSelectedSegment() {
        GeoSegment seg = lstSegments.getSelectedValue();
        if (seg == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a segment first.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
		try{
        parent.addSegment(seg);
		}catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this,
					e.getMessage(),
					"Invalid segment",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		setVisible(false);
		dispose();
    }
}
