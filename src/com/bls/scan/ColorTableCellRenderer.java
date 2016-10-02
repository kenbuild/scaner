package com.bls.scan;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorTableCellRenderer extends DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Object tag = table.getValueAt(row, 0);
		setBackground(Color.WHITE);  //Ä¬ÈÏÎª°×É«µ×
		if(tag.equals("·ñ"))
		{
			setBackground(Color.YELLOW);
		}
		return this;
	}
}
