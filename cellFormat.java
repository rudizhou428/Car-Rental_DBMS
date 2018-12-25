import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

public class cellFormat
extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		Component c = 
				super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus,
						row, column);

		// Only for specific cell
		if (row == 0) {
			c.setFont((new Font("default", Font.BOLD, 24)));
			Color color=new Color(132,112,255);
			c.setForeground(color) ;
		}
		else
		{
			Color color=new Color(0,112,255);
			c.setForeground(color) ;
		}
		return c;
	}
}