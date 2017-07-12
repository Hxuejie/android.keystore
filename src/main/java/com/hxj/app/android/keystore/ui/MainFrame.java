package com.hxj.app.android.keystore.ui;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hxj.app.android.keystore.KeyStore;

public class MainFrame extends JFrame {

	private static final long	serialVersionUID	= 1L;

	private JTextField			filePath;
	private JButton				browse;
	private JButton				submit;
	private JTextArea			result;
	private JPasswordField		password;

	public MainFrame() throws HeadlessException {
		super("Android.KeyStore v1.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createViewAndLayout();
		listen();
	}

	private void createViewAndLayout() {
		JPanel p = new JPanel(null);
		p.setSize(400, 300);
		getContentPane().add(p);
		setSize(405, 300);
		setLocationRelativeTo(null);

		// KeyStore路径
		JLabel lab = new JLabel("KeyStore文件路径:");
		lab.setSize(120, 30);
		p.add(lab);
		filePath = new JTextField();
		filePath.setLocation(0, 30);
		filePath.setSize(320, 30);
		p.add(filePath);
		browse = new JButton("浏览...");
		browse.setSize(80, 30);
		browse.setLocation(320, 30);
		p.add(browse);

		// 密码输入框
		lab = new JLabel("KeyStore密码:");
		lab.setSize(120, 30);
		lab.setLocation(0, 60);
		p.add(lab);
		password = new JPasswordField();
		password.setSize(200, 30);
		password.setLocation(0, 90);
		p.add(password);

		// 获取信息按钮
		submit = new JButton("获取信息");
		submit.setSize(150, 30);
		submit.setLocation(220, 90);
		p.add(submit);

		// 结果显示
		result = new JTextArea();
		result.setSize(400, 110);
		JScrollPane sp = new JScrollPane(result);
		sp.setLocation(0, 130);
		sp.setSize(400, 110);
		p.add(sp);

		// Copyright
		JLabel cr = new JLabel("Copyright 2017 hxuejie@126.com", JLabel.CENTER);
		cr.setSize(400, 20);
		cr.setLocation(0, 245);
		p.add(cr);
		
	}

	private void listen() {
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setMultiSelectionEnabled(false);
				fc.showDialog(MainFrame.this, "选择");
				File file = fc.getSelectedFile();
				if (file != null) {
					setFilePath(file.getAbsolutePath());
				}
			}
		});

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fp = filePath.getText().trim();
				if ("".equals(fp)) {
					JOptionPane.showMessageDialog(MainFrame.this, "请选择KeyStore文件", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String pwd = new String(password.getPassword());
				if ("".equals(pwd)) {
					JOptionPane.showMessageDialog(MainFrame.this, "请输入密码", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					KeyStore ks = new KeyStore(fp);
					ks.read(pwd);
					setResult("MD5:\r\n" + ks.getMd5() + "\r\n" + "SHA1:\r\n" + ks.getSha1());
				} catch (FileNotFoundException er) {
					setResult("文件没有找到:" + fp);
				} catch (Exception er) {
					setResult("获取信息失败:" + er.getMessage());
				}
			}
		});
	}

	private void setFilePath(String path) {
		filePath.setText(path);
	}

	private void setResult(String str) {
		result.setText(str);
	}
}
