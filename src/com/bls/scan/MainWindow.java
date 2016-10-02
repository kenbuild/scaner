package com.bls.scan;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import java.awt.event.KeyAdapter;

import javax.swing.JScrollPane;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private Connection conn=null;
	
	JButton btnNewButton;
	JLabel jl_material;
	JLabel jl_name;
	JLabel jl_code;
	JLabel jl_length;
	JLabel jl_width;
	JLabel jl_quantity;
	JLabel jl_stripes;
	JLabel jl_banding;
	JLabel jl_hole;
	JLabel jl_package;
	JLabel jl_remark;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private DataTable table;
	
	Vector<String> columnNames=new Vector<>();
	Vector<Vector<String>> tableValues=new Vector<>();
	DefaultTableModel tableModel = new DefaultTableModel(tableValues,columnNames);
	
	public void initLabel()
	{
		jl_material.setText("");
		jl_name.setText("");
		jl_code.setText("");
		jl_length.setText("");
		jl_width.setText("");
		jl_quantity.setText("");
		jl_stripes.setText("");
		jl_banding.setText("");
		jl_hole.setText("");
		jl_package.setText("");
		jl_remark.setText("");
	}

	public Connection getConn()
	{
		if(conn == null)
		{
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");
				String ip = XMLSetting.getIp();
				String port = XMLSetting.getPort();
				String db = XMLSetting.getDb();
				String user = XMLSetting.getUser();
				String pass = XMLSetting.getPass();
				String connString="jdbc:mysql://"+ip+":"+port+"/"+db;
				//conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/scan", "root", "123");
				conn=DriverManager.getConnection(connString, user, pass);
				System.out.println("连接相关："+connString+"   "+user+"   "+pass);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public void query()
	{
		try 
		{
			String requirement = textField.getText().trim();
			if(!requirement.equals(""))
			{
				Connection con = getConn();
				String sql="select material,name,code,length,width,quantity,stripes,banding,hole,package,remark from goods where code='"+requirement+"'";
				PreparedStatement statement = con.prepareStatement(sql);
				ResultSet res=statement.executeQuery();
				
				int isnull=0;
				if(res.next())
				{
					//查询
					jl_material.setText(res.getString(1));
					jl_name.setText(res.getString(2));
					jl_code.setText(res.getString(3));
					jl_length.setText(res.getString(4));
					jl_width.setText(res.getString(5));
					jl_quantity.setText(res.getString(6));
					jl_stripes.setText(res.getString(7));
					jl_banding.setText(res.getString(8));
					jl_hole.setText(res.getString(9));
					jl_package.setText(res.getString(10));
					jl_remark.setText(res.getString(11));
					isnull++;
					
					//判断是否已标记分拣
					String code=res.getString(3);
					PreparedStatement is_scan_sql = con.prepareStatement("select code,tag from scan where code='"+code+"'");
					ResultSet scan_res = is_scan_sql.executeQuery();
					int is_scan=0;
					if(scan_res.next())
					{
						is_scan=1;
					}
					
					//如未标记，则标记已分拣
					if(is_scan==0)
					{
						PreparedStatement insert_scan = con.prepareStatement("INSERT INTO scan(code,tag) VALUES(?,?)");
						insert_scan.setString(1, code);
						insert_scan.setString(2, "是");
						insert_scan.executeUpdate();
						System.out.println("INSERT INTO scan(code,tag) VALUES('"+ code + "','" + "是" + "')");
					}
					
					scan_res.close();
					is_scan_sql.close();
				}
				
				//是否查出数据，没查出则将label设置为空
				if(isnull==0)
				{
					initLabel();
				}
				
				res.close();
				statement.close();
				
			}
			else
			{
				initLabel();
			}
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("\u626B\u7801\u5DE5\u5177");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1600, 1000);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);    //窗口大小为全屏
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u5E38\u7528");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("\u5BFC\u5165");
		menuItem.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fileChooser = new JFileChooser();// 创建文件选择对话框
				// 显示文件选择对话框
				int dia = fileChooser.showOpenDialog(getContentPane());
				// 判断用户单击的是否为“打开”按钮
				if (dia == JFileChooser.APPROVE_OPTION) {
					// 获得选中的文件对象
					File excel_file = fileChooser.getSelectedFile();
					textField.requestFocus();
					
					FileInputStream input;
					try {
						input = new FileInputStream(excel_file);
						HSSFWorkbook wb = new HSSFWorkbook(new BufferedInputStream(input));
						int sheet_numbers = wb.getNumberOfSheets();
						System.out.println("a.xls有"+sheet_numbers+"个工作簿");
						
						HSSFSheet sheet = wb.getSheetAt(0);
						String shett_name = sheet.getSheetName();
						System.out.println("第1个工作簿的名称是："+shett_name);
						
						int rows_num = sheet.getLastRowNum();
						System.out.println("此工作簿共有"+rows_num+"行");
						
						Row row = sheet.getRow(1);
						int columns_num = row.getLastCellNum();
						System.out.println("此工作簿共有"+columns_num+"列");
						
						Cell  cell = null ;//row.getCell(3);
						String cell_value = null;
						Connection con = getConn();
						PreparedStatement statement = con.prepareStatement("INSERT INTO goods(material,name,code,length,width,quantity,stripes,banding,hole,package,remark) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
						for(int i=1;i<=rows_num;i++)
						{
							row = sheet.getRow(i);
							System.out.print(i+"行: ");
							for(int j=0;j<columns_num;j++)
							{
								cell = row.getCell(j);
								switch ( cell.getCellType())
								{
								case XSSFCell.CELL_TYPE_STRING:
									cell_value = cell.getStringCellValue();
									break;
								case XSSFCell.CELL_TYPE_NUMERIC:
									cell_value = cell.getNumericCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_BLANK:
									cell_value = "";
									break;
								}
								System.out.print(cell_value+"     ");
								statement.setString(j+1, cell_value.trim());
							}
							System.out.println();
							statement.executeUpdate();
						}
						
						wb.close();
						input.close();
						JOptionPane.showMessageDialog(null, "导入成功");
						
					} catch (Exception e1) 
					{
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "导入失败，请检查Excel文件格式是否符合标准");
					}
				}
			}
		});
		menu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("\u6E05\u9664\u6570\u636E");
		menuItem_1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Object[] options = {"确认", "撤销"};
		        int result = JOptionPane.showOptionDialog(null,
		                "本操作将会删除已有数据，请确认是否删除！","Warning", JOptionPane.DEFAULT_OPTION,
		                JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		        if(result == 0)
		        {
		        	try
		        	{
		        		Connection con = getConn();
						Statement stmt = con.createStatement();
						stmt.executeUpdate("delete from goods");
					} catch (Exception e1)
		        	{
						e1.printStackTrace();
					}
		        }
			}
		});
		
		JMenuItem menuItem_2 = new JMenuItem("\u5BFC\u51FA");  //导出按钮
		menuItem_2.addActionListener(new ActionListener() 
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) 
			{
				//默认文件名称为年月日时分秒
				Date date = new Date();
				String year=String.format("%tY", date);
				String month=String.format("%tm", date);
				String day=String.format("%td", date);
				String hour=String.format("%tH", date);
				String minute=String.format("%tM", date);
				String second=String.format("%tS", date);
				String datatime=year+month+day+hour+minute+second;
				File file=new File("./"+datatime+".xls");
				
				JFileChooser fileChooser = new JFileChooser();// 创建文件选择对话框
				fileChooser.setSelectedFile(file);
				// 显示文件选择对话框
				int dia = fileChooser.showSaveDialog(getContentPane());
				
				if(dia==JFileChooser.APPROVE_OPTION)
				{
					File excel_file_export = fileChooser.getSelectedFile();
					try
					{
						OutputStream out = new FileOutputStream(excel_file_export);
						
						//创建xls文件和工作簿
						HSSFWorkbook workbook = new HSSFWorkbook();
						HSSFSheet sheet = workbook.createSheet("Sheet1");
						
						//设置表格样式
						sheet.setDefaultColumnWidth((short) 20);
						sheet.setDefaultRowHeightInPoints((float) 17);
						HSSFCellStyle style = workbook.createCellStyle();
						style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						HSSFFont font = workbook.createFont();
						font.setFontHeightInPoints((short) 12);
						style.setFont(font);
						
						
						HSSFRow row = sheet.createRow(0);
						String[] header = {"分拣状态","材料","名称","代号","长度","宽度","数量","纹理方向","封边信息","排孔信息","包号","备注"};
						for(int i=0;i<header.length;i++)
						{
							HSSFCell cell = row.createCell(i);
							HSSFRichTextString text = new HSSFRichTextString(header[i]);
							cell.setCellValue(text);
							cell.setCellStyle(style);
						}
						
						Connection con=getConn();
						String sql="select CASE b.tag WHEN '是' THEN '是' ELSE '否' END as tag,a.material,a.name,a.code,a.length,a.width,a.quantity,a.stripes,a.banding,a.hole,a.package,a.remark from goods a left join scan b on a.code=b.code ";
						PreparedStatement statement = con.prepareStatement(sql);
						ResultSet res=statement.executeQuery();
						
						int row_num = 1;
						System.out.println("写入文件：");
						while(res.next())
						{
							HSSFRow row_content = sheet.createRow(row_num);
							System.out.print(row_num+"行:");
							for(int i=0;i<12;i++)
							{
								HSSFCell cell = row_content.createCell(i);
								HSSFRichTextString text = new HSSFRichTextString(res.getString(i+1));
								cell.setCellValue(text);
								cell.setCellStyle(style);
								System.out.print(res.getString(i+1)+"   ");
							}
							System.out.println();
							row_num++;
						}
						
						System.out.println("保存"+excel_file_export.getPath());
						workbook.write(out);
						
						res.close();
						workbook.close();
						out.close();
						
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
		menu.add(menuItem_2);
		menu.add(menuItem_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				if(key==KeyEvent.VK_ENTER)
				{
					query();
					textField.setText("");
					textField.requestFocus();
				}
			}
		});
		textField.setBounds(59, 20, 177, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("\u5206\u62E3");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				query();
				textField.setText("");
				textField.requestFocus();
			}
			
		});
		button.setBounds(246, 20, 60, 23);
		contentPane.add(button);
		
		JLabel label = new JLabel("\u6750\u6599\uFF1A");
		label.setFont(new Font("黑体", Font.PLAIN, 18));
		label.setBounds(40, 60, 55, 23);
		contentPane.add(label);
		
		jl_material = new JLabel(".");
		jl_material.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_material.setBounds(105, 60, 327, 21);
		contentPane.add(jl_material);
		
		jl_code = new JLabel(".");
		jl_code.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_code.setBounds(105, 95, 327, 21);
		contentPane.add(jl_code);
		
		JLabel label_2 = new JLabel("\u4EE3\u53F7\uFF1A");
		label_2.setFont(new Font("黑体", Font.PLAIN, 18));
		label_2.setBounds(40, 95, 55, 23);
		contentPane.add(label_2);
		
		jl_name = new JLabel(".");
		jl_name.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_name.setBounds(550, 60, 218, 21);
		contentPane.add(jl_name);
		
		JLabel label_4 = new JLabel("\u540D\u79F0\uFF1A");
		label_4.setFont(new Font("黑体", Font.PLAIN, 18));
		label_4.setBounds(485, 60, 55, 23);
		contentPane.add(label_4);
		
		jl_quantity = new JLabel(".");
		jl_quantity.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_quantity.setBounds(550, 95, 218, 21);
		contentPane.add(jl_quantity);
		
		JLabel label_6 = new JLabel("\u6570\u91CF\uFF1A");
		label_6.setFont(new Font("黑体", Font.PLAIN, 18));
		label_6.setBounds(485, 95, 55, 23);
		contentPane.add(label_6);
		
		jl_length = new JLabel(".");
		jl_length.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_length.setBounds(105, 130, 327, 21);
		contentPane.add(jl_length);
		
		JLabel label_8 = new JLabel("\u957F\u5EA6\uFF1A");
		label_8.setFont(new Font("黑体", Font.PLAIN, 18));
		label_8.setBounds(40, 130, 55, 23);
		contentPane.add(label_8);
		
		jl_width = new JLabel(".");
		jl_width.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_width.setBounds(550, 130, 218, 21);
		contentPane.add(jl_width);
		
		JLabel label_10 = new JLabel("\u5BBD\u5EA6\uFF1A");
		label_10.setFont(new Font("黑体", Font.PLAIN, 18));
		label_10.setBounds(485, 130, 55, 23);
		contentPane.add(label_10);
		
		jl_stripes = new JLabel(".");
		jl_stripes.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_stripes.setBounds(105, 165, 327, 21);
		contentPane.add(jl_stripes);
		
		JLabel label_12 = new JLabel("\u7EB9\u7406\u65B9\u5411\uFF1A");
		label_12.setFont(new Font("黑体", Font.PLAIN, 18));
		label_12.setBounds(10, 165, 90, 23);
		contentPane.add(label_12);
		
		jl_package = new JLabel(".");
		jl_package.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_package.setBounds(550, 165, 218, 21);
		contentPane.add(jl_package);
		
		JLabel label_14 = new JLabel("\u5305\u53F7\uFF1A");
		label_14.setFont(new Font("黑体", Font.PLAIN, 18));
		label_14.setBounds(485, 165, 55, 23);
		contentPane.add(label_14);
		
		jl_banding = new JLabel(".");
		jl_banding.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_banding.setBounds(105, 200, 663, 21);
		contentPane.add(jl_banding);
		
		JLabel label_16 = new JLabel("\u5C01\u8FB9\uFF1A");
		label_16.setFont(new Font("黑体", Font.PLAIN, 18));
		label_16.setBounds(40, 200, 55, 23);
		contentPane.add(label_16);
		
		jl_hole = new JLabel(".");
		jl_hole.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_hole.setBounds(105, 235, 663, 21);
		contentPane.add(jl_hole);
		
		JLabel label_18 = new JLabel("\u6392\u5B54\uFF1A");
		label_18.setFont(new Font("黑体", Font.PLAIN, 18));
		label_18.setBounds(40, 235, 55, 23);
		contentPane.add(label_18);
		
		jl_remark = new JLabel(".");
		jl_remark.setFont(new Font("黑体", Font.PLAIN, 18));
		jl_remark.setBounds(105, 270, 663, 21);
		contentPane.add(jl_remark);
		
		JLabel label_20 = new JLabel("\u5907\u6CE8\uFF1A");
		label_20.setFont(new Font("黑体", Font.PLAIN, 18));
		label_20.setBounds(40, 270, 55, 23);
		contentPane.add(label_20);
		
		JLabel label_1 = new JLabel("\u8BA2\u5355\u53F7\uFF1A");
		label_1.setFont(new Font("黑体", Font.PLAIN, 18));
		label_1.setBounds(700, 20, 72, 23);
		contentPane.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				btnNewButton.doClick();
			}
		});
		textField_1.setColumns(10);
		textField_1.setBounds(762, 20, 120, 21);
		contentPane.add(textField_1);
		
		JLabel label_3 = new JLabel("\u5305\u53F7\uFF1A");
		label_3.setFont(new Font("黑体", Font.PLAIN, 18));
		label_3.setBounds(892, 20, 55, 23);
		contentPane.add(label_3);
		
		textField_2 = new JTextField();
		textField_2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				btnNewButton.doClick();
			}
		});
		textField_2.setColumns(10);
		textField_2.setBounds(939, 20, 120, 21);
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				btnNewButton.doClick();
			}
		});
		textField_3.setColumns(10);
		textField_3.setBounds(1120, 20, 120, 21);
		contentPane.add(textField_3);
		
		JLabel label_5 = new JLabel("\u4EE3\u53F7\uFF1A");
		label_5.setFont(new Font("黑体", Font.PLAIN, 18));
		label_5.setBounds(1069, 20, 55, 23);
		contentPane.add(label_5);
		
		btnNewButton = new JButton("\u67E5\u8BE2");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				tableValues.clear();  //删除原有表格数据
				table.getRowSorter().allRowsChanged();  //通知表格数据模型，所有行已改变，以免因已排序发生错误
				table.getRowSorter().setSortKeys(null);  //取消排序
				
				String sql = "select CASE b.tag WHEN '是' THEN '是' ELSE '否' END as tag,a.code,a.package,a.quantity,a.name,a.material,a.length,a.width,a.stripes,a.banding,a.hole,a.remark from goods a left join scan b on a.code=b.code where 1=1 ";
				String orderID = textField_1.getText().trim();
				if(!orderID.equals(""))
				{
					orderID=orderID.split("-")[0];
					sql=sql+"and a.code like '"+orderID+"-%' ";
				}
				String package1 = textField_2.getText().trim();
				if(!package1.equals(""))
				{
					sql=sql+"and a.package = '"+package1+"' ";
				}
				String code = textField_3.getText().trim();
				if(!code.equals(""))
				{
					sql=sql+"and a.code = '"+code+"' ";
				}
				sql=sql+"ORDER BY a.package,b.tag";
				
				System.out.println(sql);
				try 
				{
					PreparedStatement queryGoods = getConn().prepareStatement(sql);
					ResultSet resGoods= queryGoods.executeQuery();
					while(resGoods.next())
					{
						Vector<String> row=new Vector<>();
						row.add(resGoods.getString(1));
						row.add(resGoods.getString(2));
						row.add(resGoods.getString(3));
						row.add(resGoods.getString(4));
						row.add(resGoods.getString(5));
						row.add(resGoods.getString(6));
						row.add(resGoods.getString(7));
						row.add(resGoods.getString(8));
						row.add(resGoods.getString(9));
						row.add(resGoods.getString(10));
						row.add(resGoods.getString(11));
						row.add(resGoods.getString(12));
						tableModel.addRow(row);
					}
					resGoods.close();
				} catch (Exception e1)
				{
					e1.printStackTrace();
				}
				
				table.getRowSorter().allRowsChanged(); //通知表格数据模型，所有行已改变，以免因已排序发生错误
				table.updateUI();  //刷新表格
			}
		});
		btnNewButton.setBounds(1250, 20, 93, 23);
		contentPane.add(btnNewButton);
		
		
		columnNames.add("分拣状态");
		columnNames.add("代号");
		columnNames.add("包号");
		columnNames.add("数量");
		columnNames.add("名称");
		columnNames.add("材料");
		columnNames.add("长度");
		columnNames.add("宽度");
		columnNames.add("纹理方向");
		columnNames.add("封边信息");
		columnNames.add("排孔信息");
		columnNames.add("备注");
		
		
		table = new DataTable(tableModel);
		table.setSize(700, 400);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);  //设置第一列列宽
		table.getColumnModel().getColumn(1).setPreferredWidth(100); 
		table.getColumnModel().getColumn(5).setPreferredWidth(300); 
		table.getColumnModel().getColumn(9).setPreferredWidth(500); 
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(670, 60, 680, 400);
		contentPane.add(scrollPane);
		
	}
}
